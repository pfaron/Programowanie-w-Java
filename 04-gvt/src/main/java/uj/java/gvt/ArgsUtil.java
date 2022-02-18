package uj.java.gvt;

public class ArgsUtil {
    public static String getFileName(String... args) {
        if (args.length < 2) {
            return null;
        }

        return args[1];
    }

    public static String getMessage(String... args) {
        int index = 0;
        while (++index < args.length)
            if (args[index].equals("-m"))
                if (index + 1 != args.length)
                    return args[index + 1];

        return null;
    }

    public static long getLastNumber(String... args) {
        int index = 0;
        long result = -1;
        while (++index < args.length)
            if (args[index].equals("-last"))
                break;

        if (index + 1 < args.length) {
            try {
                result = Long.parseLong(args[index + 1]);
            } catch (NumberFormatException ignored) {
            }
        }

        return result;
    }
}