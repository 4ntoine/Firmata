package name.antonsmirnov.firmata.message.factory.arduino;

import name.antonsmirnov.firmata.message.factory.BoardMessageFactory;

/**
 * Arduino Diecimila board
 * http://arduino.cc/en/Main/ArduinoBoardDiecimila
 */
public class Diecimila extends BoardMessageFactory {

    public final static int MAX_PIN = 13;

    public Diecimila() {
        super(MIN_PIN, MAX_PIN, arrayFromTo(0, 5), new int[] { 3,5,6,9,10,11 });
    }
}
