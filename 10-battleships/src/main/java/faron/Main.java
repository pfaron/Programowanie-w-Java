package faron;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            CommandExecutor.execute(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Testing testing = new Testing();
        //testing.runTests();
    }
}
