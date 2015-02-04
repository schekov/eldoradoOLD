package part1.task3;

import part1.task3.exception.MilliliterOutOfBoundsException;
import part1.task3.exception.TemperatureOutOfBoundsException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class KettleImpl implements Kettle {

    private static final int DEFAULT_TIME_SPEED = 1000;
    private static final int WATER_SPECIFIC_HEAT = 4200;
    private static final int MIN_LIMIT_TEMPERATURE = 1;
    private static final int MAX_LIMIT_TEMPERATURE = 120;
    public static final int DEFAULT_CURRENT_TEMPERATURE = 20;
    public static final int DEFAULT_BOIL_TEMPERATURE = 100;

    private int timeSpeed;
    private int volume;
    private int power;
    private double temperature = DEFAULT_BOIL_TEMPERATURE;
    public double currTemperature = DEFAULT_CURRENT_TEMPERATURE;
    private int currWaterAmount;
    private volatile boolean isOn = true;

    public KettleImpl(int volumeMilliliters, int powerWatt) {
        this.power = powerWatt;
        this.volume = volumeMilliliters;
        this.timeSpeed = DEFAULT_TIME_SPEED;
    }

    public int getTimeSpeed() {
        return timeSpeed;
    }

    public void setTimeSpeed(int timeSpeed) {
        this.timeSpeed = timeSpeed;
    }

    public double getCurrTemperature() {
        return currTemperature;
    }

    public void setCurrTemperature(double currTemperature) {
        this.currTemperature = currTemperature;
    }

    @Override
    public void turnOn() throws MilliliterOutOfBoundsException, TemperatureOutOfBoundsException {
        if (currWaterAmount == 0) {
            throw new MilliliterOutOfBoundsException("is not enough water 0/" + volume);
        }
        warmingUpToTemperature(temperature);
    }

    @Override
    public void shutOff() {
        this.isOn = false;
    }

    @Override
    public void pourWater(int milliliters) throws MilliliterOutOfBoundsException {
        if ((milliliters + currWaterAmount) > volume) {
            throw new MilliliterOutOfBoundsException("can't to pour water more than " + volume + " milliliters");
        }
        this.currWaterAmount = milliliters;
    }

    @Override
    public void pourOutWater(int milliliters) throws MilliliterOutOfBoundsException {
        if ((currWaterAmount - milliliters) < 0) {
            throw new MilliliterOutOfBoundsException("the amount of water can't be less than zero");
        }
        this.currWaterAmount = currWaterAmount - milliliters;
    }

    @Override
    public void boil() throws TemperatureOutOfBoundsException {
        warmingUpToTemperature(DEFAULT_BOIL_TEMPERATURE);
    }

    @Override
    public void warmingUpToTemperature(double temperature) throws TemperatureOutOfBoundsException {
        if (temperature > MAX_LIMIT_TEMPERATURE || temperature < MIN_LIMIT_TEMPERATURE) {
            throw new TemperatureOutOfBoundsException("temperature cant be less than " + MIN_LIMIT_TEMPERATURE + " or be more than " + MAX_LIMIT_TEMPERATURE);
        }
        double tempPerSec = getTemperaturePerSecond();
        System.out.println("\nEstimated time to boil: " + getTimeToBoil() + " sec");
        System.out.println("Time speed: " + timeSpeed + " per 1000 real milliseconds");
        System.out.println(getCurTime() + ": warming up began");
        while (isOn && currTemperature <= temperature){
            try {
                TimeUnit.MILLISECONDS.sleep(timeSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double outTemperature = new BigDecimal(currTemperature).setScale(1, RoundingMode.UP).doubleValue();
            System.out.print("\r" + outTemperature + "° ");
            this.currTemperature += tempPerSec;
        }
        System.out.println("\n" + getCurTime() + ": done, water is warmed up");
        this.temperature = temperature;
    }

    private String getCurTime(){
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return format.format(date);
    }

    public double getTemperaturePerSecond() {
        double volumLiters = volume / 1000;
        return power / (volumLiters * WATER_SPECIFIC_HEAT);
    }

    private long getTimeToBoil(){
        return (long) ((temperature - currTemperature)/ getTemperaturePerSecond());
    }
}
