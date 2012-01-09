package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.SysexMessage;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.SerialException;

import static name.antonsmirnov.firmata.BytesHelper.ENCODE_STRING;

/**
 * MessageWriter for SysexMessage and inheritors
 */
public class SysexMessageWriter implements IMessageWriter<SysexMessage> {

    public static final int COMMAND_START = 0xF0;
    public static final int COMMAND_END   = 0xF7;

    public void write(SysexMessage message, ISerial serial) throws SerialException {
        serial.write(COMMAND_START);
        serial.write(message.getCommand());

        if (message.getData() != null) {
            byte[] dataBytes = ENCODE_STRING(message.getData());
            serial.write(dataBytes);
        }

        serial.write(COMMAND_END);
    }
}
