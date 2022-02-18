package uj.java.w3;

public class DefaultBattleshipGenerator implements BattleshipGenerator {

    @Override
    public String generateMap() {
        BattleshipMap2D map = new BattleshipMap2D(10, 10);

        map.placeNewShip(4);
        for (int i = 0; i < 2; i++)
            map.placeNewShip(3);
        for (int i = 0; i < 3; i++)
            map.placeNewShip(2);
        for (int i = 0; i < 4; i++)
            map.placeNewShip(1);

        return map.printAsString();
    }
}
