package uj.java.pwj2020.spreadsheet;

public class StringUtil {

    public static Parameters extractFormulaParameters(String formula) {
        return new Parameters(formula.substring(formula.indexOf('(') + 1, formula.indexOf(',')),
                formula.substring(formula.indexOf(',') + 1, formula.indexOf(')')));
    }

    public static int horizontalIndex(String str) {
        return Integer.parseInt(leftmostSubstringWithCharactersFromRange(str, '0', '9')) - 1;
    }

    public static int verticalIndex(String str) {
        return capitalLettersIndexToInt(leftmostSubstringWithCharactersFromRange(str, 'A', 'Z'));
    }

    private static int capitalLettersIndexToInt(String letters) {

        int outputIndex = 0;
        int multiplicand = 1;
        int base = 26;

        for (int i = letters.length() - 1; i >= 0; i--) {
            outputIndex += multiplicand * (letters.charAt(i) - 'A' + 1);
            multiplicand *= base;
        }

        return outputIndex - 1;
    }

    private static String leftmostSubstringWithCharactersFromRange(String input, char rangeBegin, char rangeEnd) {

        int begin = 0;

        while (begin < input.length() && !(input.charAt(begin) >= rangeBegin && input.charAt(begin) <= rangeEnd))
            begin++;

        int end = begin + 1;

        while (end < input.length() && input.charAt(end) >= rangeBegin && input.charAt(end) <= rangeEnd)
            end++;

        return input.substring(begin, end);
    }
}