package name.antonsmirnov.firmata.deserializer;

import name.antonsmirnov.firmata.Firmata;
import name.antonsmirnov.firmata.message.StringSysexMessage;

/**
 * MessageDeserializer for StringSysexMessage
 */
public class StringSysexMessageDeserializer extends BaseSysexMessageDeserializer<StringSysexMessage> {

    public StringSysexMessageDeserializer() {
        super((byte)StringSysexMessage.COMMAND);
    }

    @Override
    protected StringSysexMessage buildSysexMessage(byte[] buffer, int bufferLength) {
        StringSysexMessage message = new StringSysexMessage();
        // skip 2 first bytes - COMMAND_START and sysex command byte
        message.setData(extractStringFromBuffer(buffer, 2, bufferLength - 2));
        return message;
    }

    public void fireEvent(Firmata.Listener listener) {
        listener.onStringSysexMessageReceived(getMessage());
    }
}
