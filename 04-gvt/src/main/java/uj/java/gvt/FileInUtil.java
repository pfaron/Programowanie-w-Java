package uj.java.gvt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileInUtil {

    public static VersionInfo readVersionInfoFromFile(long versionNumber) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(".gvt/" + versionNumber + "/version_info"));
        VersionInfo result = (VersionInfo) in.readObject();
        in.close();
        return result;
    }

    public static VersionInfo readLatestVersionInfo() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(".gvt/latest_version_info"));
        VersionInfo result = (VersionInfo) in.readObject();
        in.close();
        return result;
    }
}
