package uj.java.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GvtCommit {
    public void commit(String... args) {

        GvtInit.isInitialized();

        String fileName = ArgsUtil.getFileName(args);
        String message = ArgsUtil.getMessage(args);

        if (fileName == null) {
            System.out.println("Please specify file to commit.");
            System.exit(50);
        }

        if (Files.notExists(Path.of(fileName))) {
            System.out.println("File " + fileName + " does not exist.");
            System.exit(51);
        }

        VersionInfo latestVerInfo = null;
        try {
            latestVerInfo = FileInUtil.readLatestVersionInfo();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("File " + fileName + " cannot be added, see ERR for details.");
            e.printStackTrace();
            System.exit(22);
        }


        if (!VersionInfoUtil.versionInfoContainsFile(fileName, latestVerInfo)) {
            System.out.println("File " + fileName + " is not added to gvt.");
        } else {

            if (message == null)
                message = "Committed file: " + fileName;
            else
                message = "Committed file: " + fileName + '\n' + message;

            var newVerInfo = new VersionInfo(latestVerInfo.versionNumber + 1, message);
            newVerInfo.copyListOfFiles(latestVerInfo.listOfAddedFiles);
            newVerInfo.updateFileReference(fileName);

            try {
                FileOutUtil.saveNewVersion(newVerInfo, fileName);
            } catch (IOException e) {
                System.out.println("File " + fileName + " cannot be committed, see ERR for details.");
                e.printStackTrace();
                System.exit(-52);
            }

            System.out.println("File " + fileName + " committed successfully.");
        }
    }
}
