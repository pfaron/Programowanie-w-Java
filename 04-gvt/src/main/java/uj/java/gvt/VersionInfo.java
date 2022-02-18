package uj.java.gvt;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VersionInfo implements Serializable {

    public long versionNumber;
    public String commitMessage;
    public List<FileLastReference> listOfAddedFiles;

    public VersionInfo(long vn, String cm) {
        versionNumber = vn;
        commitMessage = cm;
        listOfAddedFiles = new ArrayList<>();
    }

    public void copyListOfFiles(List<FileLastReference> l) {
        listOfAddedFiles.addAll(l);
    }

    public void addFileReference(String fileName) {
        var lastReference = new FileLastReference(fileName, versionNumber);
        listOfAddedFiles.add(lastReference);
    }

    public void updateFileReference(String fileName) {
        for (FileLastReference lastReference : listOfAddedFiles)
            if (lastReference.fileName.equals(fileName))
                lastReference.lastVersion = versionNumber;
    }

    public void removeFileReference(String fileName) {
        listOfAddedFiles.removeIf(lastReference -> lastReference.fileName.equals(fileName));
    }

}