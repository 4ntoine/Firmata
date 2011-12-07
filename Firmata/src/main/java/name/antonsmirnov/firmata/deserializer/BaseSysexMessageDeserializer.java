package name.antonsmirnov.firmata.deserializer;

import name.antonsmirnov.firmata.message.SysexMessage;
import name.antonsmirnov.firmata.serializer.SysexMessageSerializer;

import static name.antonsmirnov.firmata.BytesHelper.DECODE_STRING;

/**
 * Base MessageDeserializer for SysexMessage
 */
public abstract class BaseSysexMessageDeserializer<ConcreteSysexMessage extends SysexMessage>
        implements IMessageDeserializer<ConcreteSysexMessage> {

    private Byte sysexCommand;
    
    public BaseSysexMessageDeserializer(Byte sysexCommand) {
        this.sysexCommand = sysexCommand;
    }

    public boolean canHandle(byte[] buffer, int bufferLength, int command) {
        return (bufferLength == 1 && buffer[0] == (byte)SysexMessageSerializer.COMMAND_START)  // is sysex message?
                ||
               (bufferLength == 2 && (sysexCommand == null || sysexCommand.equals(buffer[1]))) // is needed sysex command
                ||
               (bufferLength == 3 && sysexCommand != null);
    }

    protected boolean isHandling;

    public void startHandling() {
        isHandling = true;
    }

    protected ConcreteSysexMessage message;

    public void handle(byte[] buffer, int length) {
        byte incomingByte = buffer[length-1];

        if (incomingByte == (byte)SysexMessageSerializer.COMMAND_END) {
            isHandling = false;

            message = buildSysexMessage(buffer, length);
        }
    }

    /**
     * Build SysexMessage from incoming buffer
     *
     * @param buffer buffer (start from COMMAND_START byte, ends with COMMAND_END byte)
     * @param bufferLength buffer length
     * @return SysexMessage command or inherited message
     */
    protected abstract ConcreteSysexMessage buildSysexMessage(byte[] buffer, int bufferLength);

    public ConcreteSysexMessage getMessage() {
        return message;
    }

    public boolean finishedHandling() {
        return !isHandling;
    }

    // extract string from buffer
    protected String extractStringFromBuffer(byte[] buffer, int startIndex, int endIndex) {
        if ((endIndex - startIndex + 1) % 2 != 0)
            throw new RuntimeException("Sysex command data length should be even");

        return DECODE_STRING(buffer, startIndex, endIndex);
    }
}
