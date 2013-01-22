package name.antonsmirnov.firmata.message.factory.arduino;

import name.antonsmirnov.firmata.message.factory.BoardMessageFactory;

/**
 * Arduino Mega board
 * http://arduino.cc/en/Main/ArduinoBoardMega
 */
public class Mega extends BoardMessageFactory {

    public final static int MAX_PIN = 54;

    public Mega() {
        super(MIN_PIN, MAX_PIN, arrayFromTo(0, 15), union(arrayFromTo(2, 13), arrayFromTo(44, 46)));
    }
}
