package part1.task3.test;

import org.junit.Before;
import org.junit.Test;
import part1.task3.KettleImpl;
import part1.task3.exception.MilliliterOutOfBoundsException;
import part1.task3.exception.TemperatureOutOfBoundsException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Timer;
import java.util.TimerTask;

public class KettleImplTest {

    private static final int VOLUME = 2000;
    private static final int POWER = 2000;
    private static final int WATER_MILLILITEERS = 1000;
    private static KettleImpl kettle;

    @Before
    public void setUp() throws MilliliterOutOfBoundsException {
        kettle = new KettleImpl(VOLUME,POWER);
        kettle.setTimeSpeed(10);
        kettle.pourWater(WATER_MILLILITEERS);
    }

    @Test
    public void testTurnOn() throws Exception {

    }

    @Test
    public void testShutOff() throws Exception {
        Thread turnOn = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    kettle.turnOn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        turnOn.start();

        final long timeToShutOff = 5000;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                kettle.shutOff();
                double temperature = KettleImpl.DEFAULT_CURRENT_TEMPERATURE + (timeToShutOff/1000)*kettle.getTemperaturePerSecond();
                double expectedTemperature = new BigDecimal(temperature).setScale(1, RoundingMode.UP).doubleValue();
                double lastTemperature = kettle.getCurrTemperature();
                if(expectedTemperature != lastTemperature) try {
                    throw new Exception("Expected temperature: " + expectedTemperature +
                            ", actual temperature: " + lastTemperature);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, timeToShutOff);

    }

    @Test
    public void testPourWater() throws Exception {
        kettle.pourWater(200);
    }

    @Test(expected = MilliliterOutOfBoundsException.class)
    public void testToPourWaterOnException1() throws Exception {
        kettle.pourWater(3000);
    }

    @Test
    public void testPourOutWater() throws Exception {
        kettle.pourOutWater(1000);
    }

    @Test(expected = MilliliterOutOfBoundsException.class)
    public void testToPourOutWaterOnException() throws Exception {
        kettle.pourOutWater(2000);
    }

    @Test
    public void testBoilToTemperature() throws Exception {
        kettle.warmingUpToTemperature(80);
    }

    @Test(expected = TemperatureOutOfBoundsException.class)
    public void testBoilToTemperatureOnException() throws Exception {
        kettle.warmingUpToTemperature(200);
    }
}