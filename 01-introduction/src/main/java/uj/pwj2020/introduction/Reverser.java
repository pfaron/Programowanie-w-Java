package uj.pwj2020.introduction;

public class Reverser {

    public String reverse(String input) {

        if (input == null)
            return null;

        return new StringBuilder(input.strip()).reverse().toString();
    }

    public String reverseWords(String input) {

        if (input == null)
            return null;

        int r = input.length();
        int l = r - 1;
        StringBuilder output = new StringBuilder();

        while (l >= 0) {
            if (input.charAt(l) == ' ') {
                if(l+1 != r)
                    output.append(input, l + 1, r).append(" ");
                r = l;
            }
            l--;
        }

        if(l+1 != r)
            output.append(input, 0, r);
        else
            output.deleteCharAt(output.lastIndexOf(" "));

        return output.toString();
    }

}