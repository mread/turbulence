package com.github.mread.turbulence4j.calculators;

public class AuthorValue {

    private final String author;
    private final Integer value;

    public AuthorValue(String author, Integer value) {
        this.author = author;
        this.value = value;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getValue() {
        return value;
    }

}
