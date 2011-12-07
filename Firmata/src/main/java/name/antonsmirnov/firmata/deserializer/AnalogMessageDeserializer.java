package name.antonsmirnov.firmata.deserializer;

import name.antonsmirnov.firmata.Firmata;
import name.antonsmirnov.firmata.message.AnalogMessage;
import name.antonsmirnov.firmata.serializer.AnalogMessageSerializer;

import static name.antonsmirnov.firmata.BytesHelper.DECODE_BYTE;
import static name.antonsmirnov.firmata.BytesHelper.DECODE_CHANNEL;

/**
 * MessageDeserializer for AnalogMessage
 */
public class AnalogMessageDeserializer implements IMessageDeserializer<AnalogMessage> {

    public boolean canHandle(byte[] buffer, int bufferLength, int command) {
        return command == AnalogMessageSerializer.COMMAND;
    }

    private boolean isHandling;

    public void startHandling() {
        isHandling = true;
        message = new AnalogMessage();
    }

    public void handle(byte[] buffer, int length) {
        if (length == 2) {
            message.setPin(DECODE_CHANNEL(buffer[0]));
        } else {
            message.setValue(DECODE_BYTE(buffer[1], buffer[2]));
            isHandling = false;
        }
    }

    public boolean finishedHandling() {
        return !isHandling;
    }

    private AnalogMessage message;

    public AnalogMessage getMessage() {
        return message;
    }

    public void fireEvent(Firmata.Listener listener) {
        listener.onAnalogMessageReceived(getMessage());
    }
}
