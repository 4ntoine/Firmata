package name.antonsmirnov.firmata.serializer;

import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.serial.ISerial;

/**
 * Message serializer
 */
public interface IMessageSerializer<ConcreteMessage extends Message> {

    /**
     * Write command to Serial
     */
    void writeToSerial(ConcreteMessage message, ISerial serial);
}
