package name.antonsmirnov.firmata;

import name.antonsmirnov.firmata.message.*;
import name.antonsmirnov.firmata.serial.SerialException;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper which remembers sent and received commands
 */
public class MessagesHistoryFirmataWrapper implements IFirmata, IFirmata.Listener {

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

    private List<Message> sentMessages = new ArrayList<Message>();
    private List<Message> receivedMessages = new ArrayList<Message>();

    protected void rememberReceivedMessage(Message message) {
        receivedMessages.add(message);
    }

    /**
     * Get sent messsages
     * @return sent messages
     */
    public List<Message> getSentMessages() {
        return sentMessages;
    }

    /**
     * Get received messages
     * @return received messages
     */
    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    /**
     * Get last received message
     * @return received message or NULL
     */
    public Message getLastReceivedMessage() {
        return receivedMessages.size() > 0
            ? receivedMessages.get(receivedMessages.size() - 1)
            : null;
    }

    public MessagesHistoryFirmataWrapper(IFirmata firmata) {
        this.firmata = firmata;
        firmata.addListener(this);

        clear();
    }

    /**
     * Clear history
     * (should be invoked on serial.stop())
     */
    public void clear() {
        sentMessages.clear();
        receivedMessages.clear();
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
        sentMessages.add(message);
    }

    public void onDataReceived(int incomingByte) {
        firmata.onDataReceived(incomingByte);
    }
}
