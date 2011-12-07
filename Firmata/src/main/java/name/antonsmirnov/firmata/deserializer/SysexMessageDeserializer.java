package name.antonsmirnov.firmata.deserializer;

import name.antonsmirnov.firmata.Firmata;
import name.antonsmirnov.firmata.message.SysexMessage;

/**
 * MessageDeserializer for SysexMessage
 */
public class SysexMessageDeserializer extends BaseSysexMessageDeserializer<SysexMessage> {

    public SysexMessageDeserializer() {
        super(null);
        // null means that 'no command byte specified'
    }

    @Override
    protected SysexMessage buildSysexMessage(byte[] buffer, int bufferLength) {
        SysexMessage message = new SysexMessage();
        message.setCommand(buffer[1]);
        // skip 2 first bytes - COMMAND_START and sysex command byte
        message.setData(extractStringFromBuffer(buffer, 2, bufferLength - 2));
        return message;
    }

    public void fireEvent(Firmata.Listener listener) {
        listener.onSysexMessageReceived(message);
    }
}
