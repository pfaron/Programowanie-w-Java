package faron;

public class CoordinatesUtil {
    static public int changeToIndex(char ch) {
        return ch - 'A';
    }

    static public char changeToLetter(int i) {
        return (char) ('A' + i);
    }

    static public Coordinates extractCoords(String text){
        return new Coordinates(text.charAt(0), Integer.parseInt(text.substring(1)));
    }
}
