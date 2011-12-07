package name.antonsmirnov.firmata.deserializer;

import name.antonsmirnov.firmata.Firmata;
import name.antonsmirnov.firmata.message.Message;

/**
 * Message serializer
 */
public interface IMessageDeserializer<ConcreteMessage extends Message> {

    /**
     * Can handle command
     * @param buffer incoming buffer
     * @param bufferLength current buffer length
     * @return true if it's his command message type
     */
    boolean canHandle(byte[] buffer, int bufferLength, int command);

    /**
     * Start handling message
     */
    void startHandling();

    /**
     * Handle next message byte
     * @param buffer incoming buffer
     * @param length current buffer length
     */
    public void handle(byte[] buffer, int length);

    /**
     * Has it finished message handling
     * @return is it has received all the message bytes
     */
    boolean finishedHandling();

    /**
     * Message if it finished handling
     * (check finishedHandling before)
     * @return
     */
    ConcreteMessage getMessage();

    /**
     * Invoke Firmata listener
     * @param listener
     */
    void fireEvent(Firmata.Listener listener);
}
