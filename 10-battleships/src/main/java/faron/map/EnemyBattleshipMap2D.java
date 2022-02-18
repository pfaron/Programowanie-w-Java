package faron.map;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EnemyBattleshipMap2D {

    private enum BattleStatus {
        NO_SHOT, MISS, HIT, NEAR_HIT
    }

    record Coordinates(int row, int column) {

    }

    int height;
    int width;
    BattleStatus[][] battleStatuses;

    private void clearBattle() {
        for (BattleStatus[] row : battleStatuses)
            Arrays.fill(row, BattleStatus.NO_SHOT);
    }

    public EnemyBattleshipMap2D(int h, int w) {
        height = h;
        width = w;
        battleStatuses = new BattleStatus[height][width];

        clearBattle();
    }

    public void markAsMiss(int row, int column) {
        battleStatuses[row][column] = BattleStatus.MISS;
    }

    private void markAroundMast(Coordinates coords) {

        if (coords.row != 0) {
            markAsNearHitIfNoShot(new Coordinates(coords.row - 1, coords.column));

            if (coords.column != 0)
                markAsNearHitIfNoShot(new Coordinates(coords.row - 1, coords.column - 1));

            if (coords.column != height - 1)
                markAsNearHitIfNoShot(new Coordinates(coords.row - 1, coords.column + 1));
        }

        if (coords.row != width - 1) {
            markAsNearHitIfNoShot(new Coordinates(coords.row + 1, coords.column));

            if (coords.column != 0)
                markAsNearHitIfNoShot(new Coordinates(coords.row + 1, coords.column - 1));

            if (coords.column != height - 1)
                markAsNearHitIfNoShot(new Coordinates(coords.row + 1, coords.column + 1));
        }

        if (coords.column != 0)
            markAsNearHitIfNoShot(new Coordinates(coords.row, coords.column - 1));

        if (coords.column != height - 1)
            markAsNearHitIfNoShot(new Coordinates(coords.row, coords.column + 1));

    }

    private void markAsNearHitIfNoShot(Coordinates coords) {
        if (battleStatuses[coords.row][coords.column] == BattleStatus.NO_SHOT)
            battleStatuses[coords.row][coords.column] = BattleStatus.NEAR_HIT;
    }

    private boolean isMast(Coordinates coords) {
        return battleStatuses[coords.row][coords.column] == BattleStatus.HIT;
    }

    private void markShipAsSunk(Coordinates coords, Set<Coordinates> alreadyChecked) {

        alreadyChecked.add(coords);

        markAroundMast(coords);

        if (coords.row != 0) {
            Coordinates temp = new Coordinates(coords.row - 1, coords.column);
            if (isMast(temp) && !alreadyChecked.contains(temp)) {
                markShipAsSunk(temp, alreadyChecked);
            }
        }

        if (coords.row != width - 1) {
            Coordinates temp = new Coordinates(coords.row + 1, coords.column);
            if (isMast(temp) && !alreadyChecked.contains(temp)) {
                markShipAsSunk(temp, alreadyChecked);
            }
        }

        if (coords.column != 0) {
            Coordinates temp = new Coordinates(coords.row, coords.column - 1);
            if (isMast(temp) && !alreadyChecked.contains(temp)) {
                markShipAsSunk(temp, alreadyChecked);
            }
        }

        if (coords.column != height - 1) {
            Coordinates temp = new Coordinates(coords.row, coords.column + 1);
            if (isMast(temp) && !alreadyChecked.contains(temp)) {
                markShipAsSunk(temp, alreadyChecked);
            }
        }

    }

    public void markAsHit(int row, int column) {
        battleStatuses[row][column] = BattleStatus.HIT;
    }

    public void markAsHitAndSunk(int row, int column) {
        battleStatuses[row][column] = BattleStatus.HIT;
        Set<Coordinates> alreadyChecked = new HashSet<>();
        markShipAsSunk(new Coordinates(row, column), alreadyChecked);
    }

    public String printAsStringWithQuestionMarks(){
        StringBuilder sb = new StringBuilder();

        for (BattleStatus[] row : battleStatuses) {
            for (BattleStatus bs : row) {
                if (bs == BattleStatus.HIT) sb.append("#");
                else if (bs == BattleStatus.MISS || bs == BattleStatus.NEAR_HIT) sb.append(".");
                else sb.append("?");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public String printAsStringWithoutQuestionMarks(){
        StringBuilder sb = new StringBuilder();

        for (BattleStatus[] row : battleStatuses) {
            for (BattleStatus bs : row) {
                if (bs == BattleStatus.HIT) sb.append("#");
                else sb.append(".");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}