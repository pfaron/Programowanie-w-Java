package uj.java.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GvtInit {
    public void init() {
        if (Files.isDirectory(Path.of(".gvt/"))) {
            System.out.println("Current directory is already initialized.");
            System.exit(10);
        }

        try {
            Files.createDirectory(Path.of(".gvt/"));
        } catch (IOException e) {
            System.out.println("Underlying system problem. See ERR for details.");
            e.printStackTrace();
            System.exit(10);
        }

        VersionInfo vi = new VersionInfo(0, "GVT initialized.");
        try {
            FileOutUtil.createLatestVersionInfoFile();
            FileOutUtil.saveNewVersion(vi, null);
        } catch (IOException e) {
            System.out.println("Underlying system problem. See ERR for details.");
            e.printStackTrace();
            System.exit(10);
        }

        System.out.println("Current directory initialized successfully.");
    }

    public static void isInitialized() {
        if (!Files.isDirectory(Path.of(".gvt/"))) {
            System.out.println("Current directory is not initialized. Please use \"init\" command to initialize.");
            System.exit(-2);
        }
    }
}
