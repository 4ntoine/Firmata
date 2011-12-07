package name.antonsmirnov.firmata.deserializer;

import name.antonsmirnov.firmata.Firmata;
import name.antonsmirnov.firmata.message.FirmwareVersionMessage;
import name.antonsmirnov.firmata.message.ReportFirmwareVersionMessage;

/**
 * MessageDeserializer for FirmwareVersionMessage
 */
public class FirmwareVersionMessageDeserializer extends BaseSysexMessageDeserializer<FirmwareVersionMessage> {

    public FirmwareVersionMessageDeserializer() {
        super((byte)ReportFirmwareVersionMessage.COMMAND);
    }

    @Override
    protected FirmwareVersionMessage buildSysexMessage(byte[] buffer, int bufferLength) {
        FirmwareVersionMessage message = new FirmwareVersionMessage();
        message.setMajor(buffer[2]);
        message.setMinor(buffer[3]);
        // skip 4 first bytes - COMMAND_START, sysex command byte, major, minor
        message.setName(extractStringFromBuffer(buffer, 4, bufferLength -2));
        return message;
    }

    public void fireEvent(Firmata.Listener listener) {
        listener.onFirmwareVersionMessageReceived(message);
    }
}
