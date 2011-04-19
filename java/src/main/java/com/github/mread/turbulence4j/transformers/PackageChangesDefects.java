package com.github.mread.turbulence4j.transformers;

import static java.lang.Math.max;

public class PackageChangesDefects {

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
        // reverse
        //        return (calculatePPR() * 10000) - changes;
        return 0d - excessCost() - calculatePPR();
    }

    public double calculatePPR() {
        if (defects == 0)
            return 0d;
        if (changes == 0 && defects > 0)
            return 100d * defects;
        return ((double) defects) / changes;
    }

    // assumes acceptable PPR of 1.0
    public double excessCost() {
        return max((defects - changes) * 1500, 0);
    }
}
