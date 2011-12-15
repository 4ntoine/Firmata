package name.antonsmirnov.firmata.serial;

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
        this.serial.setListener(this);

        this.buffer = buffer;
    }

    public int available() {
        return buffer.size();
    }

    private ISerialListener listener;

    public void setListener(ISerialListener listener) {
        this.listener = listener;
    }

    public void start() {
        startReadingThread();
        serial.start();
    }

    /**
     * Buffer reading thread
     */
    private class BufferReadingThread extends Thread {

        // flag for the thread to exit
        private AtomicBoolean shouldStop = new AtomicBoolean(false);

        public AtomicBoolean getShouldStop() {
            return shouldStop;
        }

        public void run() {
            while (true) {
                if (available() > 0) {
                    listener.onDataReceived(this);
                }

                // checking exit flag
                if (shouldStop.get())
                    break;
            }
        }
    }

    private BufferReadingThread readingThread;

    private void startReadingThread() {
        readingThread = new BufferReadingThread();
        readingThread.start();
    }

    public void stop() {
        stopReadingThread();
        serial.stop();

        clear();
    }

    private void stopReadingThread() {
        if (readingThread == null)
            return;

        // set exit flag
        readingThread.getShouldStop().set(true);
        readingThread = null;
    }

    public void clear() {
        buffer.clear();
    }

    public int read() {
        return buffer.get();
    }

    public void write(int outcomingByte) {
        serial.write(outcomingByte);
    }

    public void write(byte[] outcomingBytes) {
        serial.write(outcomingBytes);
    }

    public void onDataReceived(ConcreteSerialImpl serialImpl) {
        // add incoming byte into buffer
        buffer.add((byte)serial.read());
    }
}
