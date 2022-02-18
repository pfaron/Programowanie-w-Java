package uj.java.kindergarten;

public final class ChildImpl extends Child {

    private final KidForks forks;

    public ChildImpl(String name, int hungerSpeedMs, KidForks forks) {
        super(name, hungerSpeedMs);
        this.forks = forks;
    }

    public void startEating() {
        new Thread(this::takeForksAndEat).start();
    }

    private void takeForksAndEat() {
        while (true) {

            try {
                forks.first().acquire();
                forks.second().acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            eat();

            forks.first().release();
            forks.second().release();

            // Żeby nie doszło do sytuacji, gdy grupa dzieci zawłaszcza widelce dla siebie,
            // każde dziecko, po spożyciu, nie podnosi widelców przez kolejne 100ms, czyli najkrótszy okres
            // (czas jedzenia), jaki musi minąć, zanim widelce znowu staną się dostępne.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}