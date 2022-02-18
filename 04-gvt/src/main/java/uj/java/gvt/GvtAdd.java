package uj.java.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GvtAdd {
    public void add(String... args) {

        GvtInit.isInitialized();

        String fileName = ArgsUtil.getFileName(args);
        String message = ArgsUtil.getMessage(args);

        if (fileName == null) {
            System.out.println("Please specify file to add.");
            System.exit(20);
        }

        if (Files.notExists(Path.of(fileName))) {
            System.out.println("File " + fileName + " not found.");
            System.exit(21);
        }

        VersionInfo latestVerInfo = null;
        try {
            latestVerInfo = FileInUtil.readLatestVersionInfo();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("File " + fileName + " cannot be added, see ERR for details.");
            e.printStackTrace();
            System.exit(22);
        }


        if (VersionInfoUtil.versionInfoContainsFile(fileName, latestVerInfo)) {
            System.out.println("File " + fileName + " already added.");
        } else {

            if (message == null)
                message = "Added file: " + fileName;
            else
                message = "Added file: " + fileName + '\n' + message;

            var newVerInfo = new VersionInfo(latestVerInfo.versionNumber + 1, message);
            newVerInfo.copyListOfFiles(latestVerInfo.listOfAddedFiles);
            newVerInfo.addFileReference(fileName);

            try {
                FileOutUtil.saveNewVersion(newVerInfo, fileName);
            } catch (IOException e) {
                System.out.println("File " + fileName + " cannot be added, see ERR for details.");
                e.printStackTrace();
                System.exit(22);
            }

            System.out.println("File " + fileName + " added successfully.");
        }
    }
}
