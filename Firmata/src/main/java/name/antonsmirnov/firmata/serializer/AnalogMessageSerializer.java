package name.antonsmirnov.firmata.serializer;

import name.antonsmirnov.firmata.message.AnalogMessage;
import name.antonsmirnov.firmata.serial.ISerial;

import static name.antonsmirnov.firmata.BytesHelper.*;

/**
 * Serializer for AnalogMessage
 */
public class AnalogMessageSerializer implements IMessageSerializer<AnalogMessage> {

    public static final int COMMAND = 0xE0;

    public void writeToSerial(AnalogMessage message, ISerial serial) {
        serial.write(COMMAND | ENCODE_CHANNEL(message.getPin()));
        serial.write(LSB(message.getValue()));
        serial.write(MSB(message.getValue()));
    }
}
