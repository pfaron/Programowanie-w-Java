package uj.java.kindergarten;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Kindergarten {

    private record KidInfo(String name, int hungerSpeed) {
    }

    private int numberOfKids;
    private Semaphore[] forks;
    private ChildImpl[] kids;

    public static void main(String[] args) throws IOException {
        init();

        final var fileName = args[0];
        System.out.println("File name: " + fileName);

        var kindergarten = new Kindergarten();
        kindergarten.readFromFileAndCreateObjects(fileName);
        kindergarten.letKidsEat();
    }

    private void letKidsEat() {
        for (var kid : kids)
            kid.startEating();
    }

    private void readFromFileAndCreateObjects(String fileName) throws FileNotFoundException {
        var input = new Scanner(new FileReader(fileName));

        numberOfKids = input.nextInt();

        forks = new Semaphore[numberOfKids];
        kids = new ChildImpl[numberOfKids];

        this.createForks();

        List<KidInfo> kidsInfo = readChildren(input);
        this.letKidsSit(kidsInfo);
    }

    private List<KidInfo> readChildren(Scanner input) {
        List<KidInfo> kidsInfo = new ArrayList<>();

        for (int i = 0; i < numberOfKids; i++)
            kidsInfo.add(new KidInfo(input.next(), input.nextInt()));

        Collections.shuffle(kidsInfo);

        return kidsInfo;
    }

    private void createForks() {
        for (int i = 0; i < numberOfKids; i++)
            forks[i] = new Semaphore(1);
    }

    private void letKidsSit(List<KidInfo> kidsInfo) {
        for (int i = 0; i < numberOfKids; i++) {
            KidForks kidForks;

            if (i % 2 == 0)
                kidForks = new KidForks(forks[i], forks[(i + 1) % numberOfKids]);
            else
                kidForks = new KidForks(forks[(i + 1) % numberOfKids], forks[i]);

            KidInfo kidInfo = kidsInfo.get(i);
            kids[i] = new ChildImpl(kidInfo.name(), kidInfo.hungerSpeed(), kidForks);
        }
    }

    private static void init() throws IOException {
        Files.deleteIfExists(Path.of("out.txt"));
        System.setErr(new PrintStream(new FileOutputStream("out.txt")));
        new Thread(Kindergarten::runKindergarten).start();
    }

    private static void runKindergarten() {
        try {
            Thread.sleep(10100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            List<String> errLines = Files.readAllLines(Path.of("out.txt"));
            System.out.println("Children cries count: " + errLines.size());
            errLines.forEach(System.out::println);
            System.exit(errLines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
