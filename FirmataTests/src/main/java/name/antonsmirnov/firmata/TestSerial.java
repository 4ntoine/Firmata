package name.antonsmirnov.firmata;

import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.ISerialListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Test ISerial implementation
 * (for usage in tests)
 */
public class TestSerial implements ISerial {

    // buffer
    private static final int BUFFER_SIZE = 1024;
    private byte[] buffer = new byte[BUFFER_SIZE];

    public TestSerial() {
        initInputStream();
        initOutputStream();
    }

    public void setListener(ISerialListener listener) {
        // nothing (feed firmata directly instead)
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
    }

    public void stop() {
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
}
