package uj.java.gvt;

import java.io.Serializable;

public class FileLastReference implements Serializable {
    public String fileName;
    public long lastVersion;

    FileLastReference(String fn, long lv) {
        fileName = fn;
        lastVersion = lv;
    }
}