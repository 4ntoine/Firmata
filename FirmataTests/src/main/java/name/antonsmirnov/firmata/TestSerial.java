package name.antonsmirnov.firmata;

import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.ISerialListener;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Test ISerial implementation
 * (for usage in tests)
 */
public class TestSerial implements ISerial {

    public TestSerial() {
        initInputStream();
        initOutputStream();
    }

    private ISerialListener listener;

    public void setListener(ISerialListener listener) {
        this.listener = listener;
    }

    private void initOutputStream() {
        outputStream = new ByteArrayOutputStream();
    }

    private void initInputStream() {
        inputStream = new ConcurrentLinkedQueue();
    }

    private ConcurrentLinkedQueue<Integer> inputStream = new ConcurrentLinkedQueue<Integer>();

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }

    private ByteArrayOutputStream outputStream;

    public void start() {
    }

    public void stop() {
    }

    public int available() {
        return inputStream.size();
    }

    public void clear() {
        initInputStream();
        initOutputStream();
    }

    public int read() {
        return inputStream.poll();
    }

    public void write(int what) {
        outputStream.write(what);
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void simulateIncomingByte(int incomingByte) {
        inputStream.add(incomingByte);
        listener.onDataReceived(this);
    }
}
