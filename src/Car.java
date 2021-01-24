import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT = 0;
    static CyclicBarrier preparation = new CyclicBarrier(MainClass.CARS_COUNT);
    static CountDownLatch raceFinish = new CountDownLatch(MainClass.CARS_COUNT);
    static CountDownLatch raceStart = new CountDownLatch(MainClass.CARS_COUNT);

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            raceStart.countDown();
            preparation.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
            if (i == race.getStages().size() - 1) {
                raceFinish.countDown();
            }
        }
    }
}
