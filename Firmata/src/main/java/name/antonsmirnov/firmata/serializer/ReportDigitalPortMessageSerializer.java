package name.antonsmirnov.firmata.serializer;

import name.antonsmirnov.firmata.message.ReportDigitalPortMessage;
import name.antonsmirnov.firmata.serial.ISerial;

import static name.antonsmirnov.firmata.BytesHelper.ENCODE_CHANNEL;

/**
 * MessageSerializer for ReportDigitalPortMessage
 */
public class ReportDigitalPortMessageSerializer implements IMessageSerializer<ReportDigitalPortMessage> {

    public static final int COMMAND = 0xD0;

    public void writeToSerial(ReportDigitalPortMessage message, ISerial serial) {
        serial.write(COMMAND | ENCODE_CHANNEL(message.getPort()));
        serial.write(message.isEnable() ? 1 : 0);
    }
}
