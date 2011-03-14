package com.github.mread.turbulence4j.calculators;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AuthorFilenameKey {
    private final String author;
    private final String filename;

    public AuthorFilenameKey(String author, String filename) {
        this.author = author;
        this.filename = filename;
    }

    public String getAuthor() {
        return author;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(author).append(filename)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(author).append(filename).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        AuthorFilenameKey other = (AuthorFilenameKey) obj;

        return new EqualsBuilder().append(author, other.author).append(filename, other.filename).isEquals();
    }

}