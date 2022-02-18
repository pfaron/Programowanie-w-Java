package faron;

public class Help {
    static public void displayHelp(){
        System.out.println("Usage:");
        System.out.println("java -jar *.jar -mode server -port N -map map-file [optional parameters]");
        System.out.println("java -jar *.jar -mode client -port N -address X -map map-file [optional parameters]");
        System.out.println("java -jar *.jar -generate map-file");
        System.out.println();
        System.out.println("Optional parameters:");
        System.out.println("-interface i - uses 'i' network interface (default: wlp3s0)");
    }
}
