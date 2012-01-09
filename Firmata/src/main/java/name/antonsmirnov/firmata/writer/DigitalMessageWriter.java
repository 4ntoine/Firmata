package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.DigitalMessage;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.SerialException;

import static name.antonsmirnov.firmata.BytesHelper.*;

/**
 * MessageWriter for DigitalMessage
 */
public class DigitalMessageWriter implements IMessageWriter<DigitalMessage> {

    public static final int COMMAND = 0x90;

    public void write(DigitalMessage message, ISerial serial) throws SerialException {
        serial.write(COMMAND | ENCODE_CHANNEL(message.getPort()));
        serial.write(LSB(message.getValue()));
        serial.write(MSB(message.getValue()));
    }
}
