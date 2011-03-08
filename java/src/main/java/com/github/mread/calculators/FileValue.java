package com.github.mread.calculators;

import java.io.File;
import java.io.IOException;

class FileValue implements Comparable<FileValue> {

    final File file;
    int value;

    public FileValue(File file, int value) {
        this.file = file;
        this.value = value;
    }

    public String getFilePath() {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getFilePath() + ": " + value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileValue other = (FileValue) obj;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!getFilePath().equals(other.getFilePath()))
            return false;
        if (value != other.value)
            return false;
        return true;

    }

    @Override
    public int compareTo(FileValue o) {
        return this.file.compareTo(o.file);
    }
}