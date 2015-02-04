package part1.task3;

import part1.task3.exception.MilliliterOutOfBoundsException;
import part1.task3.exception.TemperatureOutOfBoundsException;

public interface Kettle {

    void turnOn() throws MilliliterOutOfBoundsException, TemperatureOutOfBoundsException;

    void shutOff();

    void pourWater(int milliliters) throws MilliliterOutOfBoundsException;

    void pourOutWater(int milliliters) throws MilliliterOutOfBoundsException;

    void boil() throws MilliliterOutOfBoundsException, TemperatureOutOfBoundsException;

    void warmingUpToTemperature(double temperature) throws MilliliterOutOfBoundsException, TemperatureOutOfBoundsException;
}
