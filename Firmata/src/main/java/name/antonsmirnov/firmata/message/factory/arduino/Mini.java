package name.antonsmirnov.firmata.message.factory.arduino;

import name.antonsmirnov.firmata.message.factory.BoardMessageFactory;

/**
 * Arduino Mini board
 * http://arduino.cc/en/Main/ArduinoBoardMini
 */
public class Mini extends BoardMessageFactory {

    public final static int MAX_PIN = 13;

    public Mini() {
        super(MIN_PIN, MAX_PIN, arrayFromTo(0, 7), new int[] { 3,5,6,9,10,11 });
    }
}
