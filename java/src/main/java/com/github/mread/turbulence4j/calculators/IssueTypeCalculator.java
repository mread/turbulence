package com.github.mread.turbulence4j.calculators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.xmlpull.v1.XmlPullParserException;

import com.github.mread.turbulence4j.analysisapi.Calculator;

public class IssueTypeCalculator implements Calculator<Map<String, Issue>> {

    private final File dataFile;
    private IssuesDAO issuesDAO;

    public IssueTypeCalculator(File dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public void calculate() {
        try {
            ZipInputStream inputStream = new ZipInputStream(new FileInputStream(dataFile));
            ZipEntry entry = inputStream.getNextEntry();
            checkDataExistsInZip(entry);
            issuesDAO = new IssuesDAO();
            DataParser dataParser = new DataParser(inputStream, issuesDAO);
            dataParser.parse();

            inputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void checkDataExistsInZip(ZipEntry entry) {
        if (!entry.getName().equals("raw-data-all-frame.xml")) {
            throw new RuntimeException("Expected to find \"raw-data-all-frame.xml\" in zip file but didn't");
        }
    }

    @Override
    public Map<String, Issue> getResults() {
        return issuesDAO.asMap();
    }

    @Override
    public void setRange(String range) {

    }

}
