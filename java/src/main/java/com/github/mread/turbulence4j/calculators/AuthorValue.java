package com.github.mread.turbulence4j.calculators;

public class AuthorValue {

    private final String author;
    private final Long value;

    public AuthorValue(String author, Long value) {
        this.author = author;
        this.value = value;
    }

    public String getAuthor() {
        return author;
    }

    public Long getValue() {
        return value;
    }

}
