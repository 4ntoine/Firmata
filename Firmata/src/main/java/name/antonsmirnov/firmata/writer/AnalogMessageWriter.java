package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.AnalogMessage;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.SerialException;

import static name.antonsmirnov.firmata.BytesHelper.*;

/**
 * MessageWriter for AnalogMessage
 */
public class AnalogMessageWriter implements IMessageWriter<AnalogMessage> {

    public static final int COMMAND = 0xE0;

    public void write(AnalogMessage message, ISerial serial) throws SerialException {
        serial.write(COMMAND | ENCODE_CHANNEL(message.getPin()));
        serial.write(LSB(message.getValue()));
        serial.write(MSB(message.getValue()));
    }
}
