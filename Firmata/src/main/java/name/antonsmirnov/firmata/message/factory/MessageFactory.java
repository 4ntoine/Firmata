package name.antonsmirnov.firmata.message.factory;

import name.antonsmirnov.firmata.message.*;

/**
 * Builds Messages
 * (build SAFE messages with pins, modes validation according to hardware features)
 */
public interface MessageFactory {

    /**
     * Read digital value fom the pin
     *
     * @param pin pin
     * @return firmata message
     */
    ReportDigitalPortMessage digitalRead(int pin) throws MessageValidationException;

    /**
     * Read analog value from the pin
     *
     * @param pin pin
     * @return firmata message
     */
    ReportAnalogPinMessage analogRead(int pin) throws MessageValidationException;

    /**
     * Set a digital pin to input or output mode
     *
     * @param pin pin
     * @param mode message
     * @see SetPinModeMessage.PIN_MODE
     * @return firmata message
     */
    SetPinModeMessage pinMode(int pin, int mode) throws MessageValidationException;

    /**
     * Write to a digital pin
     *
     * @param pin ping
     * @param value digital value (0/1)
     * @return firmata message
     */
    DigitalMessage digitalWrite(int pin, int value) throws MessageValidationException;

    /**
     * Write an analog value (PWM-wave) to a pin.
     *
     * @param pin pin
     * @return firmata message
     */
    AnalogMessage analogWrite(int pin, int value) throws MessageValidationException;

}
