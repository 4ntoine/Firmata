package name.antonsmirnov.firmata.serial;

import processing.serial.IndepProcessingSerial;

import java.util.ArrayList;
import java.util.List;

/**
 * "ISerial" adapter for RXTX "Serial" class
 */
public class IndepProcessingSerialAdapter implements ISerial, IndepProcessingSerial.Listener {

    private IndepProcessingSerial indepProcessingSerial;

    public IndepProcessingSerial getIndepProcessingSerial() {
        return indepProcessingSerial;
    }

    public void onDataReceived() {
        for (ISerialListener eachListener : listeners)
            eachListener.onDataReceived(this);
    }

    public IndepProcessingSerialAdapter(IndepProcessingSerial indepProcessingSerial) {
        this.indepProcessingSerial = indepProcessingSerial;
        indepProcessingSerial.setListener(this);
    }

    private List<ISerialListener> listeners = new ArrayList<ISerialListener>();
    
    public void addListener(ISerialListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ISerialListener listener) {
        listeners.remove(listener);
    }

    private boolean isStopping;

    public void start() {
        isStopping = false;
        indepProcessingSerial.start();
    }

    public void stop() {
        isStopping = true;
        indepProcessingSerial.stop();
    }

    public boolean isStopping() {
        return isStopping;
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
