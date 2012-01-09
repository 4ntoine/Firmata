package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.ReportDigitalPortMessage;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.SerialException;

import static name.antonsmirnov.firmata.BytesHelper.ENCODE_CHANNEL;

/**
 * MessageWriter for ReportDigitalPortMessage
 */
public class ReportDigitalPortMessageWriter implements IMessageWriter<ReportDigitalPortMessage> {

    public static final int COMMAND = 0xD0;

    public void write(ReportDigitalPortMessage message, ISerial serial) throws SerialException {
        serial.write(COMMAND | ENCODE_CHANNEL(message.getPort()));
        serial.write(message.isEnable() ? 1 : 0);
    }
}
