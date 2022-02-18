package faron;

import java.util.Random;

public class CoordinatesGenerator {
    int height;
    int width;

    public CoordinatesGenerator(int h, int w){
        height = h;
        width = w;
    }

    public Coordinates generate(){
        Random random = new Random();
        char column = (char) ('A' + random.nextInt(width));
        int row = random.nextInt(height) + 1;
        return new Coordinates(column, row);
    }
}
