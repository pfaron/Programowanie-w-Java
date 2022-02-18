package uj.java.gvt;

//TODO
//printf zamiast print
//wiecej stalych
//zamienic serializable na customowy format
//można wszystkie pliki z poleceniami wyciągnąć do jednego folderu w strukturze


public class Gvt {
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please specify command.");
            System.exit(1);
        }

        String command = args[0];
        switch (command) {
            case "init" -> new GvtInit().init();
            case "add" -> new GvtAdd().add(args);
            case "detach" -> new GvtDetach().detach(args);
            case "checkout" -> new GvtCheckout().checkout(args);
            case "commit" -> new GvtCommit().commit(args);
            case "history" -> new GvtHistory().history(args);
            case "version" -> new GvtVersion().version(args);
            default -> {
                System.out.println("Unknown command " + command + ".");
                System.exit(1);
            }
        }

        System.exit(0);
    }
}