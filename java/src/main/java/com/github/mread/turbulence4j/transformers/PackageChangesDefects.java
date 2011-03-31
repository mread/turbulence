package com.github.mread.turbulence4j.transformers;

import static java.lang.Math.max;

public class PackageChangesDefects {

    private static final double ACCEPTABLE_PPR = 1.0d;

    private final String packageName;
    private int changes;
    private int defects;

    public PackageChangesDefects(String packageName, int changes, int defects) {
        this.packageName = packageName;
        this.changes = changes;
        this.defects = defects;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getChanges() {
        return changes;
    }

    public int getDefects() {
        return defects;
    }

    public void setChanges(int changes) {
        this.changes = changes;
    }

    public void setDefects(int defects) {
        this.defects = defects;
    }

    public double getTotalValue() {
        return changes + defects;
    }

    public double getSortAscendingValue() {
        return 0d - calculatePPR();
    }

    public double calculatePPR() {
        if (defects == 0)
            return 0d;
        if (changes == 0 && defects > 0)
            return 100d * defects;
        return ((double) defects) / changes;
    }

    public double excessCost() {
        return max((defects - changes) * 1500, 0);
    }
}
