package uj.java.gvt;

import java.io.IOException;

public class GvtCheckout {
    public void checkout(String... args) {

        GvtInit.isInitialized();

        if (args.length < 2) {
            System.out.println("Version number not specified.");
            System.exit(40);
        }

        long versionNumber = 0;
        try {
            versionNumber = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            System.out.print("Invalid version number: " + args[1] + ".");
            System.exit(40);
        }

        VersionInfo latestVerInfo = null;
        try {
            latestVerInfo = FileInUtil.readLatestVersionInfo();
        } catch (IOException | ClassNotFoundException e) {
            CustomErrors.defaultError(e);
        }

        if (versionNumber > latestVerInfo.versionNumber || versionNumber < 0) {
            System.out.println("Invalid version number: " + args[1] + ".");
            System.exit(40);
        }

        VersionInfo versionInfo = null;
        try {
            versionInfo = FileInUtil.readVersionInfoFromFile(versionNumber);
        } catch (IOException | ClassNotFoundException e) {
            CustomErrors.defaultError(e);
        }

        try {
            FileOutUtil.copyBackFiles(versionInfo.listOfAddedFiles);
        } catch (IOException e) {
            CustomErrors.defaultError(e);
        }

        System.out.println("Version " + versionNumber + " checked out successfully.");

    }
}
