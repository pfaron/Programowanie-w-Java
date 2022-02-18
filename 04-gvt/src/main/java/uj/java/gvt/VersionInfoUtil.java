package uj.java.gvt;

public class VersionInfoUtil {
    public static boolean versionInfoContainsFile(String fileName, VersionInfo verInfo) {
        for (var fileReference : verInfo.listOfAddedFiles)
            if (fileName.equals(fileReference.fileName))
                return true;

        return false;
    }
}
