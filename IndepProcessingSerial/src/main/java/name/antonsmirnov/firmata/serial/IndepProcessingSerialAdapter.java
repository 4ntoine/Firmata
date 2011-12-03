package name.antonsmirnov.firmata.serial;

import processing.serial.IndepProcessingSerial;

import java.util.Properties;

/**
 * "ISerial" adapter for RXTX "Serial" class
 */
public class IndepProcessingSerialAdapter implements ISerial {

    private IndepProcessingSerial indepProcessingSerial;

    public IndepProcessingSerial getIndepProcessingSerial() {
        return indepProcessingSerial;
    }

    public IndepProcessingSerialAdapter(IndepProcessingSerial indepProcessingSerial) {
        this.indepProcessingSerial = indepProcessingSerial;
    }

    public void setProperties(Properties props) {
        indepProcessingSerial.setProperties(props);
    }

    public void start() {
        indepProcessingSerial.start();
    }

    public void stop() {
        indepProcessingSerial.stop();
    }

    public void setDTR(boolean state) {
        indepProcessingSerial.setDTR(state);
    }

    public int available() {
        return indepProcessingSerial.available();
    }

    public void clear() {
        indepProcessingSerial.clear();
    }

    public int read() {
        return indepProcessingSerial.read();
    }

    public int last() {
        return indepProcessingSerial.last();
    }

    public char readChar() {
        return indepProcessingSerial.readChar();
    }

    public char lastChar() {
        return indepProcessingSerial.lastChar();
    }

    public byte[] readBytes() {
        return indepProcessingSerial.readBytes();
    }

    public int readBytes(byte[] outgoing) {
        return indepProcessingSerial.readBytes(outgoing);
    }

    public byte[] readBytesUntil(int interesting) {
        return indepProcessingSerial.readBytesUntil(interesting);
    }

    public int readBytesUntil(int interesting, byte[] outgoing) {
        return indepProcessingSerial.readBytesUntil(interesting, outgoing);
    }

    public String readString() {
        return indepProcessingSerial.readString();
    }

    public String readStringUntil(int interesting) {
        return indepProcessingSerial.readStringUntil(interesting);
    }

    public void write(int what) {
        indepProcessingSerial.write(what);
    }

    public void write(byte[] bytes) {
        indepProcessingSerial.write(bytes);
    }

    public void write(String what) {
        indepProcessingSerial.write(what);
    }
}
