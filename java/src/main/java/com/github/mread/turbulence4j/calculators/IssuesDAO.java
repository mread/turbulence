package com.github.mread.turbulence4j.calculators;

import java.util.HashMap;
import java.util.Map;

public class IssuesDAO {

    private Map<String, Issue> store = new HashMap<String, Issue>();
    private Issue currentIssue;

    public void createIssue() {
        if (currentIssue != null) {
            throw new RuntimeException("Can't create a new issue until we've persisted the old one");
        }
        currentIssue = new Issue();
    }

    public void persistIssue() {
        Issue previousIssue = store.put(currentIssue.getKey(), currentIssue);
        if (previousIssue != null) {
            System.err.println("Already had an issue in store for: " + currentIssue.getKey());
        }
        currentIssue = null;
    }

    public void setKey(String key) {
        checkIssueExists("key");
        currentIssue.setKey(key);
    }

    public void setType(String type) {
        checkIssueExists("type");
        currentIssue.setType(type);
    }

    public void addRelease(String release) {
        checkIssueExists("release");
        currentIssue.addRelease(release);
    }

    private void checkIssueExists(String string) {
        if (currentIssue == null) {
            throw new RuntimeException("No current issue to set " + string + " on");
        }
    }

    public int getSize() {
        return store.size();
    }

    public Issue findIssueByKey(String key) {
        return store.get(key);
    }

    public Map<String, Issue> asMap() {
        return store;
    }

}
