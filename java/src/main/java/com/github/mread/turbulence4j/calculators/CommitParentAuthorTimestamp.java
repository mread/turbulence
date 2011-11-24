package com.github.mread.turbulence4j.calculators;

public class CommitParentAuthorTimestamp {

    private final String commit;
    private final String parent;
    private final String author;
    private final long timestamp;

    public CommitParentAuthorTimestamp(String commit, String parent, String author, long timestamp) {
        this.commit = commit;
        this.parent = parent;
        this.author = author;
        this.timestamp = timestamp;
    }

    public String getCommit() {
        return commit;
    }

    public String getParent() {
        return parent;
    }

    public String getAuthor() {
        return author;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
    	return "CommitParentAuthor: " + author + ", commit: " + commit + 
    	        ", parent: " + parent + ", timestamp: " + timestamp;
    }
    
}
