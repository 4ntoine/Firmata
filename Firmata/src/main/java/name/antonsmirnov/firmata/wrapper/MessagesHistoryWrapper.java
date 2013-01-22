package name.antonsmirnov.firmata.wrapper;

import name.antonsmirnov.firmata.IFirmata;
import name.antonsmirnov.firmata.message.*;
import name.antonsmirnov.firmata.serial.SerialException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Wrapper which remembers sent and received commands
 *
 * Every message stores map<String, Object> of properties.
 * Concrete IMessageProperty inheritor sets and gets concrete property
 */
public class MessagesHistoryWrapper implements IFirmata, IFirmata.Listener {

    private IMessagePropertyManager[] propertyManagers;

    public MessagesHistoryWrapper(IFirmata firmata, IMessagePropertyManager... propertyManagers) {
        this.firmata = firmata;
        this.propertyManagers = propertyManagers;
        firmata.addListener(this);

        clear();
    }

    private IFirmata firmata;

    public void addListener(Listener listener) {
        firmata.addListener(listener);
    }

    public void removeListener(Listener listener) {
        firmata.removeListener(listener);
    }

    public boolean containsListener(Listener listener) {
        return firmata.containsListener(listener);
    }

    public void clearListeners() {
        firmata.clearListeners();
    }

    private List<MessageWithProperties> messages = new ArrayList<MessageWithProperties>();

    private DirectionMessagePropertyManager receivedPropertyManager = new DirectionMessagePropertyManager(true);
    private IMessageFilter receivedFilter = new DirectionMessageFilter(receivedPropertyManager);

    private DirectionMessagePropertyManager sentPropertyManager = new DirectionMessagePropertyManager(false);
    private IMessageFilter sentFilter = new DirectionMessageFilter(sentPropertyManager);

    protected void addCommonProperties(MessageWithProperties data) {
        for (IMessagePropertyManager eachPropertyManager : propertyManagers)
            eachPropertyManager.set(data);
    }

    protected void rememberReceivedMessage(Message message) {
        MessageWithProperties newData = new MessageWithProperties(message);

        // common properties
        addCommonProperties(newData);

        // 'incoming' property
        receivedPropertyManager.set(newData);

        messages.add(newData);
    }

    private void rememberSentMessage(Message message) {
        MessageWithProperties newData = new MessageWithProperties(message);

        // common properties
        addCommonProperties(newData);

        // 'incoming' property
        sentPropertyManager.set(newData);

        messages.add(newData);
    }

    private IMessageFilter stubMessageFilter = new StubMessageFilter();

    /**
     * Get all messages
     * @return messages
     */
    public List<MessageWithProperties> getMessages() {
        return getMessages(stubMessageFilter);
    }

    /**
     * Get filtered messages
     * @return messages
     */
    public List<MessageWithProperties> getMessages(IMessageFilter filter) {
        List<MessageWithProperties> filteredMessages = new CopyOnWriteArrayList<MessageWithProperties>();

        for (MessageWithProperties eachMessage : messages)
            if (filter.isAllowed(eachMessage))
                filteredMessages.add(eachMessage);

        return filteredMessages;
    }

    /**
     * Get received messages
     * @return received messages
     */
    public List<MessageWithProperties> getReceivedMessages() {
        return getMessages(receivedFilter);
    }

    /**
     * Get sent messages
     * @return sent messages
     */
    public List<MessageWithProperties> getSentMessages() {
        return getMessages(sentFilter);
    }

    /**
     * Get last received message
     * @return received message or NULL
     */
    public MessageWithProperties getLastReceivedMessageWithProperties() {
        List<MessageWithProperties> receivedMessages = getReceivedMessages();
        return receivedMessages.size() > 0
            ? receivedMessages.get(receivedMessages.size() - 1)
            : null;
    }

    /**
     * Clear history
     * (should be invoked on serial.stop())
     */
    public void clear() {
        messages.clear();
    }

    public void onAnalogMessageReceived(AnalogMessage message) {
        rememberReceivedMessage(message);
    }

    public void onDigitalMessageReceived(DigitalMessage message) {
        rememberReceivedMessage(message);
    }

    public void onFirmwareVersionMessageReceived(FirmwareVersionMessage message) {
        rememberReceivedMessage(message);
    }

    public void onProtocolVersionMessageReceived(ProtocolVersionMessage message) {
        rememberReceivedMessage(message);
    }

    public void onSysexMessageReceived(SysexMessage message) {
        rememberReceivedMessage(message);
    }

    public void onStringSysexMessageReceived(StringSysexMessage message) {
        rememberReceivedMessage(message);
    }

    public void onI2cMessageReceived(I2cReplyMessage message) {
        rememberReceivedMessage(message);
    }

    public void onUnknownByteReceived(int byteValue) {
        // nothing
    }

    public void send(Message message) throws SerialException {
        firmata.send(message);

        rememberSentMessage(message);
    }

    public void onDataReceived(int incomingByte) {
        firmata.onDataReceived(incomingByte);
    }
}
