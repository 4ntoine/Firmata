package name.antonsmirnov.firmata.message.factory.arduino;

import name.antonsmirnov.firmata.message.factory.BoardMessageFactory;

/**
 * Arduino Duemilanove board
 * http://arduino.cc/en/Main/ArduinoBoardDuemilanove
 */
public class Duemilanove extends BoardMessageFactory {

    public final static int MAX_PIN = 13;

    public Duemilanove() {
        super(MIN_PIN, MAX_PIN, arrayFromTo(0, 5), new int[] { 3,5,6,9,10,11 });
    }
}
