package name.antonsmirnov.firmata.reader;

import name.antonsmirnov.firmata.IFirmata;
import name.antonsmirnov.firmata.message.FirmwareVersionMessage;
import name.antonsmirnov.firmata.message.ReportFirmwareVersionMessage;

/**
 * MessageReader for FirmwareVersionMessage
 */
public class FirmwareVersionMessageReader extends BaseSysexMessageReader<FirmwareVersionMessage> {

    public FirmwareVersionMessageReader() {
        super((byte)ReportFirmwareVersionMessage.COMMAND);
    }

    @Override
    protected FirmwareVersionMessage buildSysexMessage(byte[] buffer, int bufferLength) {
        FirmwareVersionMessage message = new FirmwareVersionMessage();
        message.setMajor(buffer[2]);
        message.setMinor(buffer[3]);
        // skip 4 first bytes - COMMAND_START, sysex command byte, major, minor
        message.setName(extractStringFromBuffer(buffer, 4, bufferLength - 2));
        return message;
    }

    public void fireEvent(IFirmata.Listener listener) {
        listener.onFirmwareVersionMessageReceived(message);
    }
}
