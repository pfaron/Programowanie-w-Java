package faron;

import faron.map.DefaultBattleshipGenerator;
import faron.map.EnemyBattleshipMap2D;
import faron.map.PlayerBattleshipMap2D;

public class Testing {

    public void runTests(){
        generatingNewPlayerMap();

        System.out.println("Test 1 completed");

        savingPlayerMapToFile();

        System.out.println("Test 2 completed");

        readingPlayerMapFromFile();

        System.out.println("Test 3 completed");

        shootFewBullets();

        System.out.println("Test 4 completed");

        checkEnemyBoard();

        System.out.println("Test 5 completed");
    }

    public void generatingNewPlayerMap(){
        PlayerBattleshipMap2D map = new DefaultBattleshipGenerator().generateMap();
        System.out.println(map.printAsString());
    }

    public void savingPlayerMapToFile(){
        PlayerBattleshipMap2D map = new DefaultBattleshipGenerator().generateMap();
        map.printToFile("standardMap10x10.txt");
        System.out.println(map.printAsString());
    }

    public void readingPlayerMapFromFile(){
        PlayerBattleshipMap2D map = new PlayerBattleshipMap2D(10, 10);
        map.readFromFile("standardMap10x10.txt");
        System.out.println(map.printAsString());
    }

    public void shootFewBullets(){
        PlayerBattleshipMap2D map = new PlayerBattleshipMap2D(10, 10);
        map.readFromFile("standardMap10x10.txt");
        System.out.println(map.printAsString());
        for(int i=0; i<4; i++){
            for(int j=0; j<10; j++){
                System.out.print(map.shoot(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println(map.printAsStringWithBattle());
    }

    public void checkEnemyBoard(){
        EnemyBattleshipMap2D map = new EnemyBattleshipMap2D(10, 10);
        map.markAsMiss(0,1);
        map.markAsHit(2,5);
        map.markAsMiss(2,6);
        map.markAsHitAndSunk(3,5);
        map.markAsHitAndSunk(5,5);
        map.markAsMiss(9,9);
        map.markAsHit(8,7);
        System.out.println(map.printAsStringWithQuestionMarks());
        System.out.println(map.printAsStringWithoutQuestionMarks());
    }
}
