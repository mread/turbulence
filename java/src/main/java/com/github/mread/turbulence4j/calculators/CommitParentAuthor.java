package com.github.mread.turbulence4j.calculators;

public class CommitParentAuthor {

    private final String commit;
    private final String parent;
    private final String author;

    public CommitParentAuthor(String commit, String parent, String author) {
        this.commit = commit;
        this.parent = parent;
        this.author = author;
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
    
    @Override
    public String toString() {
    	return "CommitParentAuthor: " + author + ", commit: " + commit + ", parent: " + parent;
    }
    
}
