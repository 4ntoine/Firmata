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

    public void setListener(ISerialListener listener) {
        throw new RuntimeException("ProcessingSerial does not have listener property. Instead it invokes parent.serialEvent(Serial) directly;");
    }

    public ProcessingSerialAdapter(Serial processingSerial) {
        this.processingSerial = processingSerial;
    }

    public void start() {
        // nothing (ProcessingSerial is opened in constructor)
    }

    public void stop() {
        processingSerial.stop();
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
