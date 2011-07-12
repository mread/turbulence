package com.github.mread.turbulence4j.calculators;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import com.thebuzzmedia.sjxp.XMLParser;
import com.thebuzzmedia.sjxp.rule.DefaultRule;

public class DataParser {

    public class IssueRule extends DefaultRule<IssuesDAO> {
        public IssueRule() throws IllegalArgumentException {
            super(Type.TAG, "/RawData/issueDataMap/entry/IssueData");
        }

        @Override
        public void handleTag(XMLParser<IssuesDAO> parser, boolean isStartTag, IssuesDAO issuesDAO) {
            if (isStartTag) {
                issuesDAO.createIssue();
            } else {
                issuesDAO.persistIssue();
            }
        }
    }

    public class IssueKeyRule extends DefaultRule<IssuesDAO> {
        public IssueKeyRule() throws IllegalArgumentException {
            super(Type.CHARACTER, "/RawData/issueDataMap/entry/IssueData/key");
        }

        @Override
        public void handleParsedCharacters(XMLParser<IssuesDAO> parser, String text, IssuesDAO issuesDAO) {
            issuesDAO.setKey(text);
        }
    }

    public class IssueTypeRule extends DefaultRule<IssuesDAO> {
        public IssueTypeRule() throws IllegalArgumentException {
            super(Type.CHARACTER, "/RawData/issueDataMap/entry/IssueData/type");
        }

        @Override
        public void handleParsedCharacters(XMLParser<IssuesDAO> parser, String text, IssuesDAO issuesDAO) {
            issuesDAO.setType(text);
        }
    }

    public class ReleaseRule extends DefaultRule<IssuesDAO> {
        public ReleaseRule() throws IllegalArgumentException {
            super(Type.CHARACTER, "/RawData/issueDataMap/entry/IssueData/releases/string");
        }

        @Override
        public void handleParsedCharacters(XMLParser<IssuesDAO> parser, String text, IssuesDAO issuesDAO) {
            issuesDAO.addRelease(text);
        }
    }

    private final InputStream inputStream;
    private final IssuesDAO issuesDAO;

    public DataParser(InputStream inputStream, IssuesDAO issuesDAO) {
        this.inputStream = inputStream;
        this.issuesDAO = issuesDAO;
    }

    public void parse() throws XmlPullParserException, IOException {

        XMLParser<IssuesDAO> xmlParser = new XMLParser<IssuesDAO>(
                new IssueRule(),
                new IssueKeyRule(),
                new IssueTypeRule(),
                new ReleaseRule());
        xmlParser.parse(inputStream, issuesDAO);

    }
}
