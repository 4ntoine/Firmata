package name.antonsmirnov.firmata.serializer;

import name.antonsmirnov.firmata.message.SystemResetMessage;
import name.antonsmirnov.firmata.serial.ISerial;

/**
 * MessageSerializer for SystemResetMessage
 */
public class SystemResetMessageSerializer implements IMessageSerializer<SystemResetMessage> {

    public static final int COMMAND = (byte)0xFF;

    public void writeToSerial(SystemResetMessage message, ISerial serial) {
        serial.write(COMMAND);
    }
}
