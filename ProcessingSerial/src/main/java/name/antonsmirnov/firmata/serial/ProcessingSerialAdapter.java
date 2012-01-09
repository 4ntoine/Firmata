package name.antonsmirnov.firmata.serial;

import processing.serial.Serial;

/**
 * "ISerial" adapter for Processing "Serial" class
 */
public class ProcessingSerialAdapter implements ISerial {

    private Serial processingSerial;

    public Serial getProcessingSerial() {
        return processingSerial;
    }

    public void addListener(ISerialListener listener) {
        throw new RuntimeException("ProcessingSerial does not have listener property. Instead it invokes parent.serialEvent(Serial) directly;");
    }

    public void removeListener(ISerialListener listener) {
        throw new RuntimeException("ProcessingSerial does not have listener property. Instead it invokes parent.serialEvent(Serial) directly;");
    }

    public ProcessingSerialAdapter(Serial processingSerial) {
        this.processingSerial = processingSerial;
    }

    public void start() {
        // nothing (ProcessingSerial is opened in constructor)
        isStopping = false;
    }

    private boolean isStopping;

    public void stop() {
        isStopping = true;
        processingSerial.stop();
    }

    public boolean isStopping() {
        return isStopping;
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

    public void write(int outcomingByte) {
        processingSerial.write(outcomingByte);
    }

    public void write(byte[] outcomingBytes) {
        processingSerial.write(outcomingBytes);
    }
}
