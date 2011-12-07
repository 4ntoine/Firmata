package name.antonsmirnov.firmata.deserializer;

import name.antonsmirnov.firmata.Firmata;
import name.antonsmirnov.firmata.message.DigitalMessage;
import name.antonsmirnov.firmata.serializer.DigitalMessageSerializer;

import static name.antonsmirnov.firmata.BytesHelper.DECODE_BYTE;
import static name.antonsmirnov.firmata.BytesHelper.DECODE_CHANNEL;

/**
 * MessageDeserializer for DigitalMessage
 */
public class DigitalMessageDeserializer implements IMessageDeserializer<DigitalMessage> {

    public boolean canHandle(byte[] buffer, int bufferLength, int command) {
        return command == DigitalMessageSerializer.COMMAND;
    }

    private boolean isHandling;

    public void startHandling() {
        isHandling = true;
        message = new DigitalMessage();
    }

    public void handle(byte[] buffer, int length) {
        if (length == 2) {
            message.setPort(DECODE_CHANNEL(buffer[0]));
        } else {
            message.setValue(DECODE_BYTE(buffer[1], buffer[2]));
            isHandling = false;
        }
    }

    public boolean finishedHandling() {
        return !isHandling;
    }

    private DigitalMessage message;

    public DigitalMessage getMessage() {
        return message;
    }

    public void fireEvent(Firmata.Listener listener) {
        listener.onDigitalMessageReceived(getMessage());
    }
}
