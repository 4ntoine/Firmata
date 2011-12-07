package name.antonsmirnov.firmata.serializer;

import name.antonsmirnov.firmata.message.SysexMessage;
import name.antonsmirnov.firmata.serial.ISerial;

import static name.antonsmirnov.firmata.BytesHelper.ENCODE_STRING;

/**
 * MessageSerializer for SysexMessage and inheritors
 */
public class SysexMessageSerializer implements IMessageSerializer<SysexMessage> {

    public static final int COMMAND_START = 0xF0;
    public static final int COMMAND_END   = 0xF7;

    public void writeToSerial(SysexMessage message, ISerial serial) {
        serial.write(COMMAND_START);
        serial.write(message.getCommand());

        if (message.getData() != null) {
            byte[] dataBytes = ENCODE_STRING(message.getData());
            serial.write(dataBytes);
        }

        serial.write(COMMAND_END);
    }
}
