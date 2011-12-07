package name.antonsmirnov.firmata.serializer;

import name.antonsmirnov.firmata.message.ReportAnalogPinMessage;
import name.antonsmirnov.firmata.serial.ISerial;

import static name.antonsmirnov.firmata.BytesHelper.ENCODE_CHANNEL;

/**
 * MessageSerializer for ReportAnalogPinMessage
 */
public class ReportAnalogPinMessageSerializer implements IMessageSerializer<ReportAnalogPinMessage> {

    public static final int COMMAND = 0xC0;

    public void writeToSerial(ReportAnalogPinMessage message, ISerial serial) {
        serial.write(COMMAND | ENCODE_CHANNEL(message.getPin()));
        serial.write(message.isEnable() ? 1 : 0);
    }
}
