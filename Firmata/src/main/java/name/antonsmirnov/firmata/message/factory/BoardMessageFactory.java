package name.antonsmirnov.firmata.message.factory;

import name.antonsmirnov.firmata.message.*;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * MessageFactory implementation with pins config
 */
public abstract class BoardMessageFactory implements MessageFactory {

    public static final int MIN_PIN = 0;

    protected int minPin;
    protected int maxPin;
    protected int[] analogOutPins;
    protected int[] analogInPins;

    public int getMinPin() {
        return minPin;
    }

    public int getMaxPin() {
        return maxPin;
    }

    public int[] getAnalogInPins() {
        return analogInPins;
    }

    public int[] getAnalogOutPins() {
        return analogOutPins;
    }
    
    protected static int[] arrayFromTo(int from, int to) {
        int[] array = new int[to - from + 1];
        for (int i=0; i<array.length; i++) {
            array[i] = from++;
        }
        return array;
    }

    public BoardMessageFactory(int minPin, int maxPin, int[] analogInPins, int[] analogOutPins) {
        this.minPin = minPin;
        this.maxPin = maxPin;
        this.analogInPins = analogInPins;
        this.analogOutPins = analogOutPins;

        // sort() in order to allow use binarySearch()
        Arrays.sort(analogInPins);
        Arrays.sort(analogOutPins);
    }

    protected void validatePin(int pin) throws MessageValidationException {
        if (pin < minPin || pin > maxPin)
            throw new MessageValidationException(
                MessageFormat.format("Allowed pin values are [{0}-{1}]", minPin, maxPin));
    }

    protected void validateAnalogIn(int pin) throws MessageValidationException {
        int[] array = analogInPins;
        if (Arrays.binarySearch(array, pin) < 0)
            throw new MessageValidationException(
                    MessageFormat.format("Allowed analog in pins are [{0}]", arrayToString(array)));
    }

    protected void validateAnalogOut(int pin) throws MessageValidationException {
        int[] array = analogOutPins;
        if (Arrays.binarySearch(array, pin) < 0)
            throw new MessageValidationException(
                MessageFormat.format("Allowed analog out (PWM) pins are [{0}]", arrayToString(array)));
    }

    protected String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<array.length; i++) {
            if (i>0)
                sb.append(", ");
            sb.append(array[i]);
        }
        return sb.toString();
    }

    protected void validateMode(int mode) throws MessageValidationException {
        SetPinModeMessage.PIN_MODE enumValue = SetPinModeMessage.PIN_MODE.find(mode);
        if (enumValue == null)
            throw new MessageValidationException(
                MessageFormat.format("Allowed modes are [{0}]", SetPinModeMessage.PIN_MODE.values()));
    }

    protected void validateDigitalValue(int value) throws MessageValidationException {
        if (value != 0 && value != 1)
            throw new MessageValidationException("Allowed digital values are [0; 1]");
    }

    private void validateAnalogValue(int value) throws MessageValidationException {
        if (value < 0 || value > 256)
            throw new MessageValidationException("Allowed analog values are [0-256]");
    }

    public ReportDigitalPortMessage digitalRead(int pin) throws MessageValidationException {
        validatePin(pin);

        return new ReportDigitalPortMessage(pin, true);
    }

    public ReportAnalogPinMessage analogRead(int pin) throws MessageValidationException {
        validatePin(pin);
        validateAnalogIn(pin);

        return new ReportAnalogPinMessage(pin, true);
    }

    public SetPinModeMessage pinMode(int pin, int mode) throws MessageValidationException {
        validatePin(pin);
        validateMode(mode);

        // analog in
        if (mode == SetPinModeMessage.PIN_MODE.ANALOG.getMode())
            validateAnalogIn(pin);

        // analog out
        if (mode == SetPinModeMessage.PIN_MODE.PWM.getMode())
            validateAnalogOut(pin);

        return new SetPinModeMessage(pin, mode);
    }

    public DigitalMessage digitalWrite(int pin, int value) throws MessageValidationException {
        validatePin(pin);
        validateDigitalValue(value);

        return new DigitalMessage(pin, value);
    }

    public AnalogMessage analogWrite(int pin, int value) throws MessageValidationException {
        validatePin(pin);
        validateAnalogOut(pin);
        validateAnalogValue(value);

        return new AnalogMessage(pin, value);
    }

}
