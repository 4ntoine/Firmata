package name.antonsmirnov.firmata.deserializer;

import name.antonsmirnov.firmata.Firmata;
import name.antonsmirnov.firmata.message.ProtocolVersionMessage;
import name.antonsmirnov.firmata.serializer.ReportProtocolVersionMessageSerializer;

/**
 * MessageDeserializer for ProtocolVersionMessage
 */
public class ProtocolVersionMessageDeserializer implements IMessageDeserializer<ProtocolVersionMessage> {

    public boolean canHandle(byte[] buffer, int bufferLength, int command) {
        return bufferLength == 1
               &&
               buffer[0] == (byte)ReportProtocolVersionMessageSerializer.COMMAND;
    }

    private boolean isHandling;

    public void startHandling() {
        isHandling = true;
        message = new ProtocolVersionMessage();
    }

    public void handle(byte[] buffer, int length) {
        byte incomingByte = buffer[length-1];
        if (length == 2) {
            message.setMajor(incomingByte);
        } else {
            message.setMinor(incomingByte);
            isHandling = false;
        }
    }

    public boolean finishedHandling() {
        return !isHandling;
    }

    private ProtocolVersionMessage message;

    public ProtocolVersionMessage getMessage() {
        return message;
    }

    public void fireEvent(Firmata.Listener listener) {
        listener.onProtocolVersionMessageReceived(getMessage());
    }
}
