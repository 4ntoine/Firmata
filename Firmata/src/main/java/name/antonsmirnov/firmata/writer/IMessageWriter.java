package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.serial.ISerial;

/**
 * Message writer
 */
public interface IMessageWriter<ConcreteMessage extends Message> {

    /**
     * Write command to Serial
     */
    void write(ConcreteMessage message, ISerial serial);
}
