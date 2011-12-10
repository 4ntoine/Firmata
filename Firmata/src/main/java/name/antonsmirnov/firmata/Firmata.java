package name.antonsmirnov.firmata;

import name.antonsmirnov.firmata.deserializer.*;
import name.antonsmirnov.firmata.message.*;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serializer.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static name.antonsmirnov.firmata.BytesHelper.DECODE_COMMAND;

/**
 * Plain Java Firmata impl
 */
public class Firmata {

    private static final int BUFFER_SIZE = 64;

    /**
     * Listener for incoming messages from Arduino board
     */
    public static interface Listener {

        /**
         * AnalogMessage received event
         * @param message
         */
        void onAnalogMessageReceived(AnalogMessage message);

        /**
         * DigitalMessage received event
         * @param message
         */
        void onDigitalMessageReceived(DigitalMessage message);

        /**
         * FirmwareVersionMessage received event
         * @param message
         */
        void onFirmwareVersionMessageReceived(FirmwareVersionMessage message);

        /**
         *  ProtocolVersionMessage received event
         * @param message
         */
        void onProtocolVersionMessageReceived(ProtocolVersionMessage message);

        /**
         * SysexMessage received (NOT KNOWN SysexMessage inherited commands like ReportFirmwareVersionMessage, StringSysexMessage, etc)
         *
         * @param message
         */
        void onSysexMessageReceived(SysexMessage message);

        /**
         * StringSysexMessage received event
         */
        void onStringSysexMessageReceived(StringSysexMessage message);

        /**
         * Unknown byte received (no active Deserializers)
         * @param byteValue
         */
        void onUnknownByteReceived(int byteValue);
    }

    /**
     * Listener stub
     */
    public static class StubListener implements Listener {
        public void onAnalogMessageReceived(AnalogMessage message) {}
        public void onDigitalMessageReceived(DigitalMessage message) {}
        public void onFirmwareVersionMessageReceived(FirmwareVersionMessage message) {}
        public void onProtocolVersionMessageReceived(ProtocolVersionMessage message) {}
        public void onSysexMessageReceived(SysexMessage message) {}
        public void onStringSysexMessageReceived(StringSysexMessage message) {}
        public void onUnknownByteReceived(int byteValue) {}
    }

    private ISerial serial;

    public ISerial getSerial() {
        return serial;
    }

    public void setSerial(ISerial serial) {
        this.serial = serial;
    }

    public Firmata() {
        initSerializers();
        initDeserializers();
    }

    private Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private static Map<Class<? extends Message>, IMessageSerializer> serializers;

    private void initSerializers() {
        serializers = new HashMap<Class<? extends Message>, IMessageSerializer>();

        serializers.put(AnalogMessage.class, new AnalogMessageSerializer());
        serializers.put(DigitalMessage.class, new DigitalMessageSerializer());
        serializers.put(ReportAnalogPinMessage.class, new ReportAnalogPinMessageSerializer());
        serializers.put(ReportDigitalPortMessage.class, new ReportDigitalPortMessageSerializer());
        serializers.put(ReportProtocolVersionMessage.class, new ReportProtocolVersionMessageSerializer());
        serializers.put(SetPinModeMessage.class, new SetPinModeMessageSerializer());
        serializers.put(SystemResetMessage.class, new SystemResetMessageSerializer());

        // sysex messages
        SysexMessageSerializer sysexMessageSerializer = new SysexMessageSerializer();
        serializers.put(SysexMessage.class, sysexMessageSerializer);
        serializers.put(StringSysexMessage.class, sysexMessageSerializer);
        serializers.put(ReportFirmwareVersionMessage.class, sysexMessageSerializer);
    }

    private static List<IMessageDeserializer> deserializers;

    private IMessageDeserializer activeDeserializer;

    private void initDeserializers() {
        deserializers = new ArrayList<IMessageDeserializer>();
        potentialDeserializers = new ArrayList<IMessageDeserializer>();

        deserializers.add(new AnalogMessageDeserializer());
        deserializers.add(new DigitalMessageDeserializer());
        deserializers.add(new FirmwareVersionMessageDeserializer());
        deserializers.add(new ProtocolVersionMessageDeserializer());
        deserializers.add(new SysexMessageDeserializer());
        deserializers.add(new StringSysexMessageDeserializer());
    }

    /**
     * Constructor
     *
     * @param serial specify concrete ISerial implementation
     */
    public Firmata(ISerial serial) {
        this();
        setSerial(serial);
    }

    /**
     * Send message to Arduino board
     *
     * @param message concrete outcoming message
     */
    public void send(Message message) {
        IMessageSerializer serializer = serializers.get(message.getClass());
        if (serializer == null)
            throw new RuntimeException("Unknown message type: " + message.getClass());

        serializer.writeToSerial(message, serial);
    }

    private List<IMessageDeserializer> potentialDeserializers;

    private byte[] buffer = new byte[BUFFER_SIZE];

    private int bufferLength;

    private Message lastReceivedMessage;

    public Message getLastReceivedMessage() {
        return lastReceivedMessage;
    }

    public void onDataReceived(int incomingByte) {
        buffer[bufferLength++] = (byte)incomingByte;

        if (activeDeserializer == null) {
            // new message byte is received
            int command = DECODE_COMMAND(incomingByte);

            if (potentialDeserializers.size() == 0) {
                // first byte check
                findPotentialDeserializers(command);
            } else {
                // not first byte check
                // few potential deserializers found, so we should check the next bytes to define Deserializer
                filterPotentialDeserializers(command);
            }

            tryHandle();

        } else {
            // continue handling with activeDeserializer
            activeDeserializer.handle(buffer, bufferLength);

            if (activeDeserializer.finishedHandling()) {
                // message is ready
                lastReceivedMessage = activeDeserializer.getMessage();
                activeDeserializer.fireEvent(listener);
                reinitBuffer();
            }
        }
    }

    // pass the next bytes in order to define according deserializer
    private void filterPotentialDeserializers(int command) {
        List<IMessageDeserializer> newPotentialDeserializers = new ArrayList<IMessageDeserializer>();

        for (IMessageDeserializer eachPotentialDeserializer : potentialDeserializers)
            if (eachPotentialDeserializer.canHandle(buffer, bufferLength, command))
                newPotentialDeserializers.add(eachPotentialDeserializer);

        potentialDeserializers = newPotentialDeserializers;
    }

    private void tryHandle() {
        int potentialDeserializersCount = potentialDeserializers.size();
        switch (potentialDeserializersCount) {

            // unknown byte
            case 0:
                for (int i=0; i<bufferLength; i++)
                    listener.onUnknownByteReceived(buffer[i]);
                reinitBuffer();
                break;

            // the only one deserializer
            case 1:
                activeDeserializer = potentialDeserializers.get(0);
                activeDeserializer.startHandling();
                break;

            // default:
            //  (in case if few serializers are found, we should pass the next bytes to define final deserializer)
        }
    }

    private void reinitBuffer() {
        bufferLength = 0;
        activeDeserializer = null;
        potentialDeserializers.clear();
    }

    private void findPotentialDeserializers(int command) {
        for (IMessageDeserializer eachDeserializer : deserializers) {
            if (eachDeserializer.canHandle(buffer, bufferLength, command)) {
                potentialDeserializers.add(eachDeserializer);
            }
        }
    }

}
