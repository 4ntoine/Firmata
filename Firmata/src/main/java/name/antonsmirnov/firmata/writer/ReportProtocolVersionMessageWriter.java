package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.ReportProtocolVersionMessage;
import name.antonsmirnov.firmata.serial.ISerial;

/**
 * MessageWriter for ReportProtocolVersionMessage
 */
public class ReportProtocolVersionMessageWriter implements IMessageWriter<ReportProtocolVersionMessage> {

    public static final int COMMAND = 0xF9;

    public void write(ReportProtocolVersionMessage message, ISerial serial) {
        serial.write(COMMAND);
    }
}
