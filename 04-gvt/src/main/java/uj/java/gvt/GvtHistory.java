package uj.java.gvt;

import java.io.IOException;

public class GvtHistory {
    public void history(String... args) {

        GvtInit.isInitialized();

        long numberOfVersionsToShow = ArgsUtil.getLastNumber(args);

        VersionInfo latestVerInfo = null;
        try {
            latestVerInfo = FileInUtil.readLatestVersionInfo();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Underlying system problem. See ERR for details.");
            e.printStackTrace();
            System.exit(-3);
        }

        if (numberOfVersionsToShow == -1 || numberOfVersionsToShow > latestVerInfo.versionNumber + 1)
            printAllHistory(latestVerInfo.versionNumber);
        else
            printLatestHistory(latestVerInfo.versionNumber, numberOfVersionsToShow);

    }

    private void printAllHistory(long numberOfVersions) {
        long currentVersion = 0;

        while (currentVersion <= numberOfVersions) {
            printSingleVersion(currentVersion);

            currentVersion++;
        }
    }

    private void printSingleVersion(long currentVersion) {
        VersionInfo tempInfo = null;
        try {
            tempInfo = FileInUtil.readVersionInfoFromFile(currentVersion);
        } catch (IOException | ClassNotFoundException e) {
            CustomErrors.defaultError(e);
        }

        String commitMessage;
        if (tempInfo.commitMessage.contains("\n"))
            commitMessage = tempInfo.commitMessage.substring(0, tempInfo.commitMessage.indexOf('\n'));
        else
            commitMessage = tempInfo.commitMessage;

        System.out.println(tempInfo.versionNumber + ": " + commitMessage);
    }

    private void printLatestHistory(long numberOfAllVersions, long numberOfVersionsToShow) {
        long currentVersion = numberOfAllVersions;

        while (numberOfVersionsToShow-- > 0) {
            printSingleVersion(currentVersion);

            currentVersion--;
        }
    }
}