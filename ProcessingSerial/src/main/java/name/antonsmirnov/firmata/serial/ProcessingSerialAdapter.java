package name.antonsmirnov.firmata.serial;

import processing.serial.Serial;

import java.util.Properties;

/**
 * "ISerial" adapter for Processing "Serial" class
 */
public class ProcessingSerialAdapter implements ISerial {

    private Serial processingSerial;

    public Serial getProcessingSerial() {
        return processingSerial;
    }

    public ProcessingSerialAdapter(Serial processingSerial) {
        this.processingSerial = processingSerial;
    }

    public void setProperties(Properties props) {
        processingSerial.setProperties(props);
    }

    public void start() {
        // nothind (ProcessingSerial is opened in constructor)
    }

    public void stop() {
        processingSerial.stop();
    }

    public void setDTR(boolean state) {
        processingSerial.setDTR(state);
    }

    public int available() {
        return processingSerial.available();
    }

    public void clear() {
        processingSerial.clear();
    }

    public int read() {
        return processingSerial.read();
    }

    public int last() {
        return processingSerial.last();
    }

    public char readChar() {
        return processingSerial.readChar();
    }

    public char lastChar() {
        return processingSerial.lastChar();
    }

    public byte[] readBytes() {
        return processingSerial.readBytes();
    }

    public int readBytes(byte[] outgoing) {
        return processingSerial.readBytes(outgoing);
    }

    public byte[] readBytesUntil(int interesting) {
        return processingSerial.readBytesUntil(interesting);
    }

    public int readBytesUntil(int interesting, byte[] outgoing) {
        return processingSerial.readBytesUntil(interesting, outgoing);
    }

    public String readString() {
        return processingSerial.readString();
    }

    public String readStringUntil(int interesting) {
        return processingSerial.readStringUntil(interesting);
    }

    public void write(int what) {
        processingSerial.write(what);
    }

    public void write(byte[] bytes) {
        processingSerial.write(bytes);
    }

    public void write(String what) {
        processingSerial.write(what);
    }
}
