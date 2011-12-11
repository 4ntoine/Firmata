package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.SetPinModeMessage;
import name.antonsmirnov.firmata.serial.ISerial;

/**
 * MessageWriter for SetPinModeMessage
 */
public class SetPinModeMessageWriter implements IMessageWriter<SetPinModeMessage> {

    public static final int COMMAND = 0xF4;

    public void write(SetPinModeMessage message, ISerial serial) {
        serial.write(COMMAND);
        serial.write(message.getPin());
        serial.write(message.getMode());
    }
}
