package com.github.mread.calculators;

import java.io.File;
import java.io.IOException;

public class FileValue implements Comparable<FileValue> {

    final File file;
    int value;

    public FileValue(String fileName, int value) {
        this.file = new File(fileName);
        this.value = value;
    }

    public FileValue(File file, int value) {
        this.file = file;
        this.value = value;
    }

    public String getFile() {
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
        try {
            return file.getCanonicalPath() + ": " + value;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        } else
            try {
                if (!file.getCanonicalPath().equals(other.file.getCanonicalPath()))
                    return false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        if (value != other.value)
            return false;
        return true;

    }

    @Override
    public int compareTo(FileValue o) {
        return this.file.compareTo(o.file);
    }
}