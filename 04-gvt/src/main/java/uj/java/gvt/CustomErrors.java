package uj.java.gvt;

public class CustomErrors {
    public static void defaultError(Exception e) {
        System.out.println("Underlying system problem. See ERR for details.");
        e.printStackTrace();
        System.exit(-3);
    }
}
