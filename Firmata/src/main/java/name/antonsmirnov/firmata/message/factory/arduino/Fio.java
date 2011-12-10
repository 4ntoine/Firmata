package name.antonsmirnov.firmata.message.factory.arduino;

import name.antonsmirnov.firmata.message.factory.BoardMessageFactory;

/**
 * Arduino Fio board
 * http://arduino.cc/en/Main/ArduinoBoardFio
 */
public class Fio extends BoardMessageFactory {

    public final static int MAX_PIN = 13;

    public Fio() {
        super(MIN_PIN, MAX_PIN, arrayFromTo(0, 7), new int[] { 3,5,6,9,10,11 });
    }
}
