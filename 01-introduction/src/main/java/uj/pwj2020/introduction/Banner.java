package uj.pwj2020.introduction;

public class Banner {

    private StringBuilder[] stringBuilderOutput;

    private enum CharType {
        LETTER, SPACE, NONE
    }

    private void initEmptyOutput(int size) {
        stringBuilderOutput = new StringBuilder[size];
        for (int i = 0; i < size; i++)
            stringBuilderOutput[i] = new StringBuilder();

    }

    private void appendSpaceCharacter() {
        for (StringBuilder sb : stringBuilderOutput)
            sb.append("    ");
    }

    private void appendAlphabetCharacter(CharType prevCharType, char currChar) {
        if (prevCharType == CharType.LETTER)
            for (StringBuilder sb : stringBuilderOutput)
                sb.append(" ");

        for (int j = 0; j < stringBuilderOutput.length; j++)
            stringBuilderOutput[j].append(Alphabet.latinLetters[currChar - 'A'][j]);

    }

    private void appendTextToOutput(String input) {
        var prevCharType = CharType.NONE;

        for (var currChar : input.toUpperCase().toCharArray()) {

            if (currChar == ' ') {

                appendSpaceCharacter();
                prevCharType = CharType.SPACE;

            } else if ('A' <= currChar && currChar <= 'Z') {

                appendAlphabetCharacter(prevCharType, currChar);
                prevCharType = CharType.LETTER;

            } else {
                System.out.println("Error: '" + currChar + "' is not a valid character.");
            }
        }
    }

    private String[] convertOutputToString() {
        var stringOutput = new String[stringBuilderOutput.length];
        for (int i = 0; i < stringOutput.length; i++)
            stringOutput[i] = stringBuilderOutput[i].toString();

        return stringOutput;
    }

    public String[] toBanner(String input) {

        if (input == null)
            return new String[]{};

        initEmptyOutput(7);

        appendTextToOutput(input);

        return convertOutputToString();
    }
}