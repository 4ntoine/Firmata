package name.antonsmirnov.firmata.serial;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Perform buffered reading from the port
 * (thread-safe, not bounded)
 */
public class BufferingSerialWrapper<ConcreteSerialImpl> implements ISerial, ISerialListener<ConcreteSerialImpl> {

    private IByteBuffer buffer;

    private int threadPriority = Thread.NORM_PRIORITY;

    public int getThreadPriority() {
        return threadPriority;
    }

    /**
     * Buffer reading thread priority
     *
     * @param threadPriority buffer reading thread priority
     */
    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    private ISerial serial;

    public BufferingSerialWrapper(ISerial serial, IByteBuffer buffer) {
        this.serial = serial;
        this.serial.addListener(this);

        this.buffer = buffer;
    }

    public int available() {
        return buffer.size();
    }

    private List<ISerialListener> listeners = new ArrayList<ISerialListener>();

    public void addListener(ISerialListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ISerialListener listener) {
        listeners.remove(listener);
    }

    public void start() throws SerialException {
        startReadingThread();
        serial.start();
    }

    // flag for the thread to exit
    private AtomicBoolean shouldStop = new AtomicBoolean();

    public AtomicBoolean getShouldStop() {
        return shouldStop;
    }

    /**
     * Buffer reading thread
     */
    private class BufferReadingThread extends Thread {

        public void run() {
            while (!shouldStop.get()) {
                if (available() > 0) {
                    for (ISerialListener eachListener : listeners)
                        eachListener.onDataReceived(this);
                }
            }
        }
    }

    private BufferReadingThread readingThread;

    private void startReadingThread() {
        readingThread = new BufferReadingThread();
        shouldStop.set(false);
        readingThread.start();
    }

    public void stop() throws SerialException {
        stopReadingThread();
        serial.stop();

        clear();
    }

    public boolean isStopping() {
        return shouldStop.get();
    }

    private void stopReadingThread() {
        if (readingThread == null)
            return;

        // set exit flag
        shouldStop.set(true);
        readingThread = null;
    }

    public void clear() {
        buffer.clear();
    }

    public int read() {
        return buffer.get();
    }

    public void write(int outcomingByte) throws SerialException {
        serial.write(outcomingByte);
    }

    public void write(byte[] outcomingBytes) throws SerialException {
        serial.write(outcomingBytes);
    }

    public void onDataReceived(ConcreteSerialImpl serialImpl) {
        // add incoming byte into buffer
        try {
            buffer.add((byte)serial.read());
        } catch (SerialException e) {
            onException(e);
        }
    }

    public void onException(Throwable e) {
        for (ISerialListener eachListener : listeners)
            eachListener.onException(e);
    }
}
