package name.antonsmirnov.firmata;

import name.antonsmirnov.firmata.serial.ISerial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class TestSerial implements ISerial {

    // buffer
    private static final int BUFFER_SIZE = 1024;
    private byte[] buffer = new byte[BUFFER_SIZE];

    public TestSerial() {
        initInputStream();
        initOutputStream();
    }

    private void initOutputStream() {
        outputStream = new ByteArrayOutputStream();
    }

    private void initInputStream() {
        inputStream = new ByteArrayInputStream(buffer);
    }

    private ByteArrayInputStream inputStream;

    public ByteArrayInputStream getInputStream() {
        return inputStream;
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }

    private ByteArrayOutputStream outputStream;

    public void setProperties(Properties props) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void start() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void stop() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setDTR(boolean state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int available() {
        return inputStream.available();
    }

    public void clear() {
        initInputStream();
        initOutputStream();
    }

    public int read() {
        return inputStream.read();
    }

    public int last() {
        return 0;
    }

    public char readChar() {
        return 0;
    }

    public char lastChar() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] readBytes() {
        return null;
    }

    public int readBytes(byte[] outgoing) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] readBytesUntil(int interesting) {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int readBytesUntil(int interesting, byte[] outgoing) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String readString() {
        return null;
    }

    public String readStringUntil(int interesting) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

    public void write(String what) {
        try {
            outputStream.write(what.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
