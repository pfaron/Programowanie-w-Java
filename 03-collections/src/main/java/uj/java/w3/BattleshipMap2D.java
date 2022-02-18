package uj.java.w3;

import java.util.*;

public class BattleshipMap2D {

    private enum CellStatus {
        WATER, SHIP, CLOSE_TO_SHIP, SHIP_IN_MAKING
    }

    private enum Directions {
        UP, LEFT, DOWN, RIGHT, NONE
    }

    record Coordinates(int x, int y) {
    }

    int height;
    int width;
    CellStatus[][] cellStatuses;

    public BattleshipMap2D(int h, int w) {
        height = h;
        width = w;
        cellStatuses = new CellStatus[height][width];

        for (CellStatus[] row : cellStatuses)
            Arrays.fill(row, CellStatus.WATER);
    }

    private Directions getNewRandomDirection(Coordinates currCell) {

        List<Directions> possibleDirections = new ArrayList<>(Arrays.asList(Directions.UP, Directions.DOWN,
                Directions.LEFT, Directions.RIGHT));

        removeUnreachableDirections(possibleDirections, currCell);

        if (possibleDirections.size() == 0)
            return Directions.NONE;

        Collections.shuffle(possibleDirections);
        return possibleDirections.get(0);
    }

    private void removeUnreachableDirections(List<Directions> possibleDirections, Coordinates currCell) {

        if (currCell.x == 0 || cellStatuses[currCell.x - 1][currCell.y] != CellStatus.WATER)
            possibleDirections.remove(Directions.LEFT);

        if (currCell.x == width - 1 || cellStatuses[currCell.x + 1][currCell.y] != CellStatus.WATER)
            possibleDirections.remove(Directions.RIGHT);

        if (currCell.y == 0 || cellStatuses[currCell.x][currCell.y - 1] != CellStatus.WATER)
            possibleDirections.remove(Directions.UP);

        if (currCell.y == height - 1 || cellStatuses[currCell.x][currCell.y + 1] != CellStatus.WATER)
            possibleDirections.remove(Directions.DOWN);
    }

    private Coordinates getRandomWaterCoordinate() {

        Coordinates randCell;
        Random r = new Random();

        do {
            randCell = new Coordinates(r.nextInt(height), r.nextInt(width));
        } while (cellStatuses[randCell.x][randCell.y] != CellStatus.WATER);

        return randCell;
    }

    private Deque<Coordinates> getNewShipCoordinates(int shipSize) {

        Deque<Coordinates> coordinates = new ArrayDeque<>(shipSize);

        boolean shipCoordsFound = false;

        while (!shipCoordsFound) {

            removeTemporaryMasts(coordinates);

            coordinates.push(getRandomWaterCoordinate());
            createTemporaryMast(coordinates.peek());

            shipCoordsFound = getRemainingShipMastsCoords(coordinates, shipSize);

        }

        return coordinates;

    }

    private boolean getRemainingShipMastsCoords(Deque<Coordinates> coordinates, int shipSize) {

        while (coordinates.size() < shipSize) {

            Coordinates lastCoord = coordinates.peek();

            Directions newDir = getNewRandomDirection(lastCoord);

            switch (newDir) {
                case UP -> coordinates.push(new Coordinates(lastCoord.x, lastCoord.y - 1));
                case DOWN -> coordinates.push(new Coordinates(lastCoord.x, lastCoord.y + 1));
                case LEFT -> coordinates.push(new Coordinates(lastCoord.x - 1, lastCoord.y));
                case RIGHT -> coordinates.push(new Coordinates(lastCoord.x + 1, lastCoord.y));
                case NONE -> {
                    return false;
                }
            }

            createTemporaryMast(coordinates.peek());

        }

        return true;
    }

    private void removeTemporaryMasts(Deque<Coordinates> coords) {

        while (!coords.isEmpty()) {
            Coordinates tempCord = coords.pop();
            cellStatuses[tempCord.x][tempCord.y] = CellStatus.WATER;
        }
    }

    private void createTemporaryMast(Coordinates coord) {
        cellStatuses[coord.x][coord.y] = CellStatus.SHIP_IN_MAKING;
    }

    private void createShipFromCoordinates(Deque<Coordinates> coords) {

        for (Coordinates coord : coords) {
            cellStatuses[coord.x][coord.y] = CellStatus.SHIP;
            placeWaterAroundShip(coord);
        }
    }

    private void placeWaterAroundShip(Coordinates coords) {

        if (coords.x != 0) {
            placeWaterIfNoShip(new Coordinates(coords.x - 1, coords.y));

            if (coords.y != 0)
                placeWaterIfNoShip(new Coordinates(coords.x - 1, coords.y - 1));

            if (coords.y != height - 1)
                placeWaterIfNoShip(new Coordinates(coords.x - 1, coords.y + 1));
        }

        if (coords.x != width - 1) {
            placeWaterIfNoShip(new Coordinates(coords.x + 1, coords.y));

            if (coords.y != 0)
                placeWaterIfNoShip(new Coordinates(coords.x + 1, coords.y - 1));

            if (coords.y != height - 1)
                placeWaterIfNoShip(new Coordinates(coords.x + 1, coords.y + 1));
        }

        if (coords.y != 0)
            placeWaterIfNoShip(new Coordinates(coords.x, coords.y - 1));

        if (coords.y != height - 1)
            placeWaterIfNoShip(new Coordinates(coords.x, coords.y + 1));

    }

    private void placeWaterIfNoShip(Coordinates coords) {
        if (cellStatuses[coords.x][coords.y] != CellStatus.SHIP)
            cellStatuses[coords.x][coords.y] = CellStatus.CLOSE_TO_SHIP;
    }

    public void placeNewShip(int shipSize) {

        if (shipSize <= 0)
            System.out.println("The ship size has to be a positive integer.");
        else {
            Deque<Coordinates> coords = getNewShipCoordinates(shipSize);
            createShipFromCoordinates(coords);
        }
    }

    public String printAsString() {

        StringBuilder sb = new StringBuilder();

        for (CellStatus[] row : cellStatuses) {
            for (CellStatus cs : row) {
                if (cs == CellStatus.SHIP) sb.append("#");
                else sb.append(".");
            }
        }

        return sb.toString();
    }
}