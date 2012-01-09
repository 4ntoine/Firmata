package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.SerialException;

/**
 * Message writer
 */
public interface IMessageWriter<ConcreteMessage extends Message> {

    /**
     * Write command to Serial
     */
    void write(ConcreteMessage message, ISerial serial) throws SerialException;
}
