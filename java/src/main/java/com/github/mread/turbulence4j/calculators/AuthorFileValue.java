package com.github.mread.turbulence4j.calculators;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AuthorFileValue {

    private final String author;
    private final int churn;
    private final String filename;

    public AuthorFileValue(String author, int churn, String filename) {
        this.author = author;
        this.churn = churn;
        this.filename = filename;
    }

    public String getAuthor() {
        return author;
    }

    public int getChurn() {
        return churn;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(author)
                .append(churn)
                .append(filename)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        AuthorFileValue other = (AuthorFileValue) obj;

        return new EqualsBuilder()
                .append(author, other.author)
                .append(filename, other.filename)
                .append(churn, other.churn)
                .isEquals();
    }
}
