package name.antonsmirnov.firmata.serializer;

import name.antonsmirnov.firmata.message.SetPinModeMessage;
import name.antonsmirnov.firmata.serial.ISerial;

/**
 * MessageSerializer for SetPinModeMessage
 */
public class SetPinModeMessageSerializer implements IMessageSerializer<SetPinModeMessage> {

    public static final int COMMAND = 0xF4;

    public void writeToSerial(SetPinModeMessage message, ISerial serial) {
        serial.write(COMMAND);
        serial.write(message.getPin());
        serial.write(message.getMode());
    }
}
