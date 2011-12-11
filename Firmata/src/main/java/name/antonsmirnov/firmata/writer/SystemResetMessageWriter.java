package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.SystemResetMessage;
import name.antonsmirnov.firmata.serial.ISerial;

/**
 * MessageWriter for SystemResetMessage
 */
public class SystemResetMessageWriter implements IMessageWriter<SystemResetMessage> {

    public static final int COMMAND = (byte)0xFF;

    public void write(SystemResetMessage message, ISerial serial) {
        serial.write(COMMAND);
    }
}
