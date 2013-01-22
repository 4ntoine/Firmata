package name.antonsmirnov.firmata.message.factory.arduino;

import name.antonsmirnov.firmata.message.factory.BoardMessageFactory;

/**
 * Arduino Mega 2560 board
 * http://arduino.cc/en/Main/ArduinoBoardMega2560
 */
public class Mega2560 extends BoardMessageFactory {

    public final static int MAX_PIN = 54;

    public Mega2560() {
        super(MIN_PIN, MAX_PIN, arrayFromTo(0, 15), union(arrayFromTo(2, 13), arrayFromTo(44, 46)));
    }
}
