package faron.map;

import faron.Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class PlayerBattleshipMap2D implements BattleshipMap2D {

    private enum CellStatus {
        WATER, SHIP, CLOSE_TO_SHIP, SHIP_IN_MAKING
    }

    private enum BattleStatus {
        NO_SHOT, MISS, HIT
    }

    private enum Directions {
        UP, LEFT, DOWN, RIGHT, NONE
    }

    record Coordinates(int row, int column) {
    }

    int height;
    int width;
    CellStatus[][] cellStatuses;
    BattleStatus[][] battleStatuses;

    private void clearMap() {
        for (CellStatus[] row : cellStatuses)
            Arrays.fill(row, CellStatus.WATER);
    }

    private void clearBattle() {
        for (BattleStatus[] row : battleStatuses)
            Arrays.fill(row, BattleStatus.NO_SHOT);
    }

    public PlayerBattleshipMap2D(int h, int w) {
        height = h;
        width = w;
        cellStatuses = new CellStatus[height][width];
        battleStatuses = new BattleStatus[height][width];

        clearMap();
        clearBattle();
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

        if (currCell.row == 0 || cellStatuses[currCell.row - 1][currCell.column] != CellStatus.WATER)
            possibleDirections.remove(Directions.LEFT);

        if (currCell.row == width - 1 || cellStatuses[currCell.row + 1][currCell.column] != CellStatus.WATER)
            possibleDirections.remove(Directions.RIGHT);

        if (currCell.column == 0 || cellStatuses[currCell.row][currCell.column - 1] != CellStatus.WATER)
            possibleDirections.remove(Directions.UP);

        if (currCell.column == height - 1 || cellStatuses[currCell.row][currCell.column + 1] != CellStatus.WATER)
            possibleDirections.remove(Directions.DOWN);
    }

    private Coordinates getRandomWaterCoordinate() {

        Coordinates randCell;
        Random r = new Random();

        do {
            randCell = new Coordinates(r.nextInt(height), r.nextInt(width));
        } while (cellStatuses[randCell.row][randCell.column] != CellStatus.WATER);

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
                case UP -> coordinates.push(new Coordinates(lastCoord.row, lastCoord.column - 1));
                case DOWN -> coordinates.push(new Coordinates(lastCoord.row, lastCoord.column + 1));
                case LEFT -> coordinates.push(new Coordinates(lastCoord.row - 1, lastCoord.column));
                case RIGHT -> coordinates.push(new Coordinates(lastCoord.row + 1, lastCoord.column));
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
            cellStatuses[tempCord.row][tempCord.column] = CellStatus.WATER;
        }
    }

    private void createTemporaryMast(Coordinates coord) {
        cellStatuses[coord.row][coord.column] = CellStatus.SHIP_IN_MAKING;
    }

    private void createShipFromCoordinates(Deque<Coordinates> coords) {

        for (Coordinates coord : coords) {
            cellStatuses[coord.row][coord.column] = CellStatus.SHIP;
            placeWaterAroundShip(coord);
        }
    }

    private void placeWaterAroundShip(Coordinates coords) {

        if (coords.row != 0) {
            placeWaterIfNoShip(new Coordinates(coords.row - 1, coords.column));

            if (coords.column != 0)
                placeWaterIfNoShip(new Coordinates(coords.row - 1, coords.column - 1));

            if (coords.column != height - 1)
                placeWaterIfNoShip(new Coordinates(coords.row - 1, coords.column + 1));
        }

        if (coords.row != width - 1) {
            placeWaterIfNoShip(new Coordinates(coords.row + 1, coords.column));

            if (coords.column != 0)
                placeWaterIfNoShip(new Coordinates(coords.row + 1, coords.column - 1));

            if (coords.column != height - 1)
                placeWaterIfNoShip(new Coordinates(coords.row + 1, coords.column + 1));
        }

        if (coords.column != 0)
            placeWaterIfNoShip(new Coordinates(coords.row, coords.column - 1));

        if (coords.column != height - 1)
            placeWaterIfNoShip(new Coordinates(coords.row, coords.column + 1));

    }

    private void placeWaterIfNoShip(Coordinates coords) {
        if (cellStatuses[coords.row][coords.column] != CellStatus.SHIP)
            cellStatuses[coords.row][coords.column] = CellStatus.CLOSE_TO_SHIP;
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
            sb.append("\n");
        }

        return sb.toString();
    }

    public String printAsStringWithBattle() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (battleStatuses[i][j] == BattleStatus.HIT) {
                    sb.append("@");
                } else if (battleStatuses[i][j] == BattleStatus.MISS) {
                    sb.append("~");
                } else {
                    if (cellStatuses[i][j] == CellStatus.SHIP) {
                        sb.append("#");
                    } else {
                        sb.append(".");
                    }
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public void printToFile(String fileName) {
        try {
            PrintWriter out = new PrintWriter(fileName);
            out.write(printAsString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile(String fileName) {
        clearMap();
        clearBattle();

        try {
            File map = new File(fileName);
            Scanner sc = new Scanner(map);

            for (int i = 0; i < height; i++) {
                String line = sc.nextLine();
                for (int j = 0; j < width; j++) {
                    cellStatuses[i][j] = switch (line.charAt(j)) {
                        case '#' -> {
                            placeWaterAroundShip(new Coordinates(i, j));
                            yield CellStatus.SHIP;
                        }
                        default -> CellStatus.WATER;
                    };
                }
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private boolean isMast(Coordinates coords) {
        return cellStatuses[coords.row][coords.column] == CellStatus.SHIP;
    }

    private boolean shipSunk(Coordinates coords, Set<Coordinates> alreadyChecked) {

        alreadyChecked.add(coords);

        if (battleStatuses[coords.row][coords.column] != BattleStatus.HIT)
            return false;

        boolean check = true;

        if (coords.row != 0) {
            Coordinates temp = new Coordinates(coords.row - 1, coords.column);
            if (isMast(temp) && !alreadyChecked.contains(temp)) {
                check = check && shipSunk(temp, alreadyChecked);
            }
        }

        if (coords.row != width - 1) {
            Coordinates temp = new Coordinates(coords.row + 1, coords.column);
            if (isMast(temp) && !alreadyChecked.contains(temp)) {
                check = check && shipSunk(temp, alreadyChecked);
            }
        }

        if (coords.column != 0) {
            Coordinates temp = new Coordinates(coords.row, coords.column - 1);
            if (isMast(temp) && !alreadyChecked.contains(temp)) {
                check = check && shipSunk(temp, alreadyChecked);
            }
        }

        if (coords.column != height - 1) {
            Coordinates temp = new Coordinates(coords.row, coords.column + 1);
            if (isMast(temp) && !alreadyChecked.contains(temp)) {
                check = check && shipSunk(temp, alreadyChecked);
            }
        }

        return check;

    }

    private boolean lastShipSunk() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (cellStatuses[i][j] == CellStatus.SHIP && battleStatuses[i][j] != BattleStatus.HIT) {
                    return false;
                }
            }
        }
        return true;
    }

    public Commands shoot(int row, int column) {

        if (cellStatuses[row][column] != CellStatus.SHIP) {
            battleStatuses[row][column] = BattleStatus.MISS;
            return Commands.MISS;
        } else {
            battleStatuses[row][column] = BattleStatus.HIT;

            Set<Coordinates> alreadyChecked = new HashSet<>();
            if (shipSunk(new Coordinates(row, column), alreadyChecked)) {
                if (lastShipSunk()) {
                    return Commands.LAST_SUNK;
                }
                return Commands.HIT_SUNK;
            }

            return Commands.HIT;
        }
    }
}