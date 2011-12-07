package name.antonsmirnov.firmata.serializer;

import name.antonsmirnov.firmata.message.ReportProtocolVersionMessage;
import name.antonsmirnov.firmata.serial.ISerial;

/**
 * MessageSerializer for ReportProtocolVersionMessage
 */
public class ReportProtocolVersionMessageSerializer implements IMessageSerializer<ReportProtocolVersionMessage> {

    public static final int COMMAND = 0xF9;

    public void writeToSerial(ReportProtocolVersionMessage message, ISerial serial) {
        serial.write(COMMAND);
    }
}
