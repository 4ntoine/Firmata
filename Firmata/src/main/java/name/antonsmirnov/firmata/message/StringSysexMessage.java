package name.antonsmirnov.firmata.message;

import java.text.MessageFormat;

/**
 * String sysex message
 */
public class StringSysexMessage extends SysexMessage {

    public static final int COMMAND = 0x71;

    public StringSysexMessage() {
        super(COMMAND, null);
    }

    public StringSysexMessage(String data) {
        this();
        setData(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        StringSysexMessage message = (StringSysexMessage)obj;
        return message != null &&
               (
                   (message.getData() == null && getData() == null)
                   ||
                   (message.getData() != null && message.getData().equals(getData()))
               );
    }

    @Override
    public String toString() {
        return MessageFormat.format("StringSysexMessage[data=\"{0}\"]", getData());
    }
}
