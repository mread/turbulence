package com.github.mread.turbulence4j.calculators;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class IssuePackage {

    private String issueKey;
    private String packageName;

    public IssuePackage() {
    }

    public IssuePackage(String issueKey, String packageName) {
        this.issueKey = issueKey;
        this.packageName = packageName;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isWellFormed() {
        return issueKey != null && packageName != null;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(issueKey).append(packageName).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        IssuePackage other = (IssuePackage) obj;

        return new EqualsBuilder()
                .append(issueKey, other.issueKey)
                .append(packageName, other.packageName)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(issueKey)
                .append(packageName)
                .toString();
    }
}
