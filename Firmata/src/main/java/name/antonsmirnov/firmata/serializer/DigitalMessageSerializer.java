package name.antonsmirnov.firmata.serializer;

import name.antonsmirnov.firmata.message.DigitalMessage;
import name.antonsmirnov.firmata.serial.ISerial;

import static name.antonsmirnov.firmata.BytesHelper.*;

/**
 * Serializer for DigitalMessage
 */
public class DigitalMessageSerializer implements IMessageSerializer<DigitalMessage> {

    public static final int COMMAND = 0x90;

    public void writeToSerial(DigitalMessage message, ISerial serial) {
        serial.write(COMMAND | ENCODE_CHANNEL(message.getPort()));
        serial.write(LSB(message.getValue()));
        serial.write(MSB(message.getValue()));
    }
}
