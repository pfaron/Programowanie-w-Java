package uj.java.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GvtVersion {
    public void version(String... args) {

        GvtInit.isInitialized();

        if (args.length < 2)
            printLatestVersion();
        else {

            long versionNumber = 0;
            try {
                versionNumber = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid version number: " + args[1]);
                System.exit(60);
            }

            if (!Files.isDirectory(Path.of(".gvt/" + versionNumber))) {
                System.out.println("Invalid version number: " + args[1]);
                System.exit(60);
            }

            printSpecificVersion(versionNumber);
        }

    }

    private void printLatestVersion() {

        VersionInfo versionInfo = null;
        try {
            versionInfo = FileInUtil.readLatestVersionInfo();
        } catch (IOException | ClassNotFoundException e) {
            CustomErrors.defaultError(e);
        }

        System.out.print("Version: " + versionInfo.versionNumber + '\n' + versionInfo.commitMessage);

    }

    private void printSpecificVersion(long versionNumber) {

        VersionInfo versionInfo = null;
        try {
            versionInfo = FileInUtil.readVersionInfoFromFile(versionNumber);
        } catch (IOException | ClassNotFoundException e) {
            CustomErrors.defaultError(e);
        }

        System.out.print("Version: " + versionInfo.versionNumber + '\n' + versionInfo.commitMessage);

    }
}
