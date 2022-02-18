package uj.java.gvt;

import java.io.IOException;

public class GvtDetach {
    public void detach(String... args) {

        GvtInit.isInitialized();

        String fileName = ArgsUtil.getFileName(args);
        String message = ArgsUtil.getMessage(args);

        if (fileName == null) {
            System.out.println("Please specify file to detach.");
            System.exit(30);
        }

        VersionInfo latestVerInfo = null;
        try {
            latestVerInfo = FileInUtil.readLatestVersionInfo();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("File " + fileName + " cannot be detached, see ERR for details.");
            e.printStackTrace();
            System.exit(31);
        }

        if (!VersionInfoUtil.versionInfoContainsFile(fileName, latestVerInfo)) {
            System.out.println("File " + fileName + " is not added to gvt.");
        } else {

            if (message == null)
                message = "Detached file: " + fileName;
            else
                message = "Detached file: " + fileName + '\n' + message;

            var newVerInfo = new VersionInfo(latestVerInfo.versionNumber + 1, message);
            newVerInfo.copyListOfFiles(latestVerInfo.listOfAddedFiles);
            newVerInfo.removeFileReference(fileName);

            try {
                FileOutUtil.saveNewVersion(newVerInfo, null);
            } catch (IOException e) {
                System.out.println("File " + fileName + " cannot be detached, see ERR for details.");
                e.printStackTrace();
                System.exit(31);
            }

            System.out.println("File " + fileName + " detached successfully.");
        }
    }
}