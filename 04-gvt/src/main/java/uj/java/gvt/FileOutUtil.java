package uj.java.gvt;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class FileOutUtil {
    public static void saveNewVersion(VersionInfo versionInfo, String fileToBeCopied) throws IOException {
        saveVersionInfoToLatest(versionInfo);
        createNewVersionDirectory(versionInfo.versionNumber);
        copyLatestVersionInfoToVersionDirectory(versionInfo.versionNumber);

        if (fileToBeCopied != null)
            copyFileToVersionDirectory(fileToBeCopied, versionInfo.versionNumber);

    }

    private static void saveVersionInfoToLatest(VersionInfo versionInfo) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(".gvt/latest_version_info"));
        out.writeObject(versionInfo);
        out.close();
    }

    private static void createNewVersionDirectory(long versionNumber) throws IOException {
        Files.createDirectory(Path.of(".gvt/" + versionNumber));
    }

    private static void copyLatestVersionInfoToVersionDirectory(long versionNumber) throws IOException {
        Files.copy(Path.of(".gvt/latest_version_info"),
                Path.of(".gvt/" + versionNumber + "/version_info"));
    }

    private static void copyFileToVersionDirectory(String fileName, long versionNumber) throws IOException {
        Files.copy(Path.of(fileName), Path.of(".gvt/" + versionNumber + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyBackFiles(List<FileLastReference> listOfFileReferences) throws IOException {
        for (FileLastReference reference : listOfFileReferences)
            copyFromVersionDirectoryToCurrentDirectory(reference.fileName, reference.lastVersion);

    }

    public static void copyFromVersionDirectoryToCurrentDirectory(String fileName, long versionNumber) throws IOException {
        Files.copy(Path.of(".gvt/" + versionNumber + "/" + fileName), Path.of(fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void createLatestVersionInfoFile() throws IOException {
        Files.createFile(Path.of(".gvt/latest_version_info"));
    }

}
