package com.github.mread.turbulence4j.calculators;

import java.util.ArrayList;
import java.util.List;

public class Issue {

    private String key;
    private String type;
    private List<String> releases = new ArrayList<String>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addRelease(String release) {
        releases.add(release);
    }

}
