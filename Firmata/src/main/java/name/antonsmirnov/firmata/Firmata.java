package name.antonsmirnov.firmata;

import name.antonsmirnov.firmata.message.*;
import name.antonsmirnov.firmata.reader.*;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.ISerialListener;
import name.antonsmirnov.firmata.writer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static name.antonsmirnov.firmata.BytesHelper.DECODE_COMMAND;

/**
 * Plain Java Firmata impl
 */
public class Firmata implements ISerialListener {

    private static final Logger log = LoggerFactory.getLogger(Firmata.class);

    private static final int BUFFER_SIZE = 1024;

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
         * Unknown byte received (no active MessageReader)
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
        serial.setListener(this);
    }

    public Firmata() {
        initWriters();
        initReaders();
    }

    private Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private static Map<Class<? extends Message>, IMessageWriter> writers;

    private void initWriters() {
        writers = new HashMap<Class<? extends Message>, IMessageWriter>();

        writers.put(AnalogMessage.class, new AnalogMessageWriter());
        writers.put(DigitalMessage.class, new DigitalMessageWriter());
        writers.put(ReportAnalogPinMessage.class, new ReportAnalogPinMessageWriter());
        writers.put(ReportDigitalPortMessage.class, new ReportDigitalPortMessageWriter());
        writers.put(ReportProtocolVersionMessage.class, new ReportProtocolVersionMessageWriter());
        writers.put(SetPinModeMessage.class, new SetPinModeMessageWriter());
        writers.put(SystemResetMessage.class, new SystemResetMessageWriter());

        // sysex messages
        SysexMessageWriter sysexMessageWriter = new SysexMessageWriter();
        writers.put(SysexMessage.class, sysexMessageWriter);
        writers.put(StringSysexMessage.class, sysexMessageWriter);
        writers.put(ReportFirmwareVersionMessage.class, sysexMessageWriter);
    }

    private static List<IMessageReader> readers;

    private IMessageReader activeReader;

    private void initReaders() {
        readers = new ArrayList<IMessageReader>();
        potentialReaders = new ArrayList<IMessageReader>();

        readers.add(new AnalogMessageReader());
        readers.add(new DigitalMessageReader());
        readers.add(new FirmwareVersionMessageReader());
        readers.add(new ProtocolVersionMessageReader());
        readers.add(new SysexMessageReader());
        readers.add(new StringSysexMessageReader());
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
        IMessageWriter writer = writers.get(message.getClass());
        if (writer == null)
            throw new RuntimeException("Unknown message type: " + message.getClass());

        log.info("Sending {}", message);
        writer.write(message, serial);
    }

    private List<IMessageReader> potentialReaders;

    private byte[] buffer = new byte[BUFFER_SIZE];

    private int bufferLength;

    private Message lastReceivedMessage;

    protected synchronized void setLastReceivedMessage(Message message) {
        this.lastReceivedMessage = message;
        log.info("Received {}", lastReceivedMessage);
    }
    
    public synchronized Message getLastReceivedMessage() {
        return lastReceivedMessage;
    }

    public void onDataReceived(Object serialImpl) {
        if (serial.available() > 0) {
            int incomingByte = serial.read();
            onDataReceived(incomingByte);
        }
    }

    public void onDataReceived(int incomingByte) {
        buffer[bufferLength++] = (byte)incomingByte;

        if (activeReader == null) {
            // new message byte is received
            int command = DECODE_COMMAND(incomingByte);

            if (potentialReaders.size() == 0) {
                // first byte check
                findPotentialReaders(command);
            } else {
                // not first byte check
                // few potential readers found, so we should check the next bytes to define MessageReader
                filterPotentialReaders(command);
            }

            tryHandle();

        } else {
            // continue handling with activeReader
            activeReader.read(buffer, bufferLength);

            if (activeReader.finishedReading()) {
                // message is ready
                activeReader.fireEvent(listener);
                setLastReceivedMessage(activeReader.getMessage());
                reinitBuffer();
            }
        }
    }

    // pass the next bytes in order to define according reader
    private void filterPotentialReaders(int command) {
        List<IMessageReader> newPotentialReaders = new ArrayList<IMessageReader>();

        for (IMessageReader eachPotentialReader : potentialReaders)
            if (eachPotentialReader.canRead(buffer, bufferLength, command))
                newPotentialReaders.add(eachPotentialReader);

        potentialReaders = newPotentialReaders;
    }

    private void tryHandle() {
        int potentialReadersCount = potentialReaders.size();
        switch (potentialReadersCount) {

            // unknown byte
            case 0:
                for (int i=0; i<bufferLength; i++)
                    listener.onUnknownByteReceived(buffer[i]);
                reinitBuffer();
                break;

            // the only one reader
            case 1:
                activeReader = potentialReaders.get(0);
                activeReader.startHandling();
                log.info("Started reading with {} ...", activeReader.getClass().getSimpleName());
                break;

            // default:
            //  (in case if few writers are found, we should pass the next bytes to define final reader)
        }
    }

    private void reinitBuffer() {
        bufferLength = 0;
        activeReader = null;
        potentialReaders.clear();
    }

    private void findPotentialReaders(int command) {
        for (IMessageReader eachReader : readers) {
            if (eachReader.canRead(buffer, bufferLength, command)) {
                potentialReaders.add(eachReader);
            }
        }
    }

}
