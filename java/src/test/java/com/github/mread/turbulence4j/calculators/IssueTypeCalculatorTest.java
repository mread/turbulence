package com.github.mread.turbulence4j.calculators;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

public class IssueTypeCalculatorTest {

    @Test
    @Ignore("fails without sappho data")
    public void readsSapphoData() throws IOException, XmlPullParserException {

        File dataFile = new File("raw-data-all-frame-2010-12-14.zip");
        assertThat(dataFile.exists(), equalTo(true));

        IssueTypeCalculator issueTypeCalculator = new IssueTypeCalculator(dataFile);

        issueTypeCalculator.calculate();
        Map<String, Issue> results = issueTypeCalculator.getResults();

        assertThat(results.size(), greaterThan(0));

    }

}
