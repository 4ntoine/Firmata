package name.antonsmirnov.firmata.serial;

import processing.serial.IndepProcessingSerial;

/**
 * "ISerial" adapter for RXTX "Serial" class
 */
public class IndepProcessingSerialAdapter implements ISerial, IndepProcessingSerial.Listener {

    private IndepProcessingSerial indepProcessingSerial;

    public IndepProcessingSerial getIndepProcessingSerial() {
        return indepProcessingSerial;
    }

    public void onDataReceived() {
        if (listener != null)
            listener.onDataReceived(this);
    }

    public IndepProcessingSerialAdapter(IndepProcessingSerial indepProcessingSerial) {
        this.indepProcessingSerial = indepProcessingSerial;
        indepProcessingSerial.setListener(this);
    }

    private ISerialListener listener;
    
    public void setListener(ISerialListener listener) {
        this.listener = listener;
    }

    public void start() {
        indepProcessingSerial.start();
    }

    public void stop() {
        indepProcessingSerial.stop();
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

    public void write(int outcomingByte) {
        indepProcessingSerial.write((byte)outcomingByte);
    }

    public void write(byte[] outcomingBytes) {
        indepProcessingSerial.write(outcomingBytes);
    }
}
