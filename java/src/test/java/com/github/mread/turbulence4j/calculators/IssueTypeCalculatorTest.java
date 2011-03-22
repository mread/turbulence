package com.github.mread.turbulence4j.calculators;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

public class IssueTypeCalculatorTest {

    @Test
    public void readsSapphoData() throws IOException, XmlPullParserException {

        File dataFile = new File("raw-data-all-frame-2010-12-14.zip");
        assertThat(dataFile.exists(), equalTo(true));

        ZipInputStream inputStream = new ZipInputStream(new FileInputStream(dataFile));

        ZipEntry entry = inputStream.getNextEntry();
        assertThat(entry.getName(), equalTo("raw-data-all-frame.xml"));

        DataParser dataParser = new DataParser(inputStream);
        dataParser.parse();

        inputStream.close();
    }

}
