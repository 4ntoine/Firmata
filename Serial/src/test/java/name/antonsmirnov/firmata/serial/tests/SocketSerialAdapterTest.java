package name.antonsmirnov.firmata.serial.tests;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.ISerialListener;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.serial.SocketSerialAdapter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for SocketSerialAdapter
 */
public class SocketSerialAdapterTest extends TestCase implements ISerialListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ISerial serial;

    private List<Byte> readBytes = new ArrayList<Byte>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        serial = new SocketSerialAdapter(IP, PORT);
        serial.addListener(this);

        serial.start();
    }

    public void onDataReceived(Object serialImpl) {
        try {
            byte incomingByte = (byte) serial.read();
            readBytes.add(incomingByte);

            log.info("Received byte: " + incomingByte + " " + new Character((char)incomingByte));
        } catch (SerialException e) {
            onException(e);
        }
    }

    public void onException(Throwable e) {
        log.error("Serial error", e);
    }

    private static final String IP = "192.168.168.169";
    private static final int PORT = 1000;

    private final byte[] MESSAGE = "ABC1234567890".getBytes();

    @Test
    // incoming bytes array should be exactly the same as sent bytes array
    public void testEcho() throws SerialException, InterruptedException {
        serial.write(MESSAGE);

        Thread.sleep(5 * 1000); // waiting 5 seconds

        assertEquals(MESSAGE.length, readBytes.size());
        for (int i=0; i<MESSAGE.length; i++) {
            assertEquals(MESSAGE[i], readBytes.get(i).byteValue());
        }
    }

    /*@Test
    public void testHelloWorld() throws SerialException, InterruptedException {
        serial.write("Anton\n".getBytes());
        serial.write("28\n".getBytes());

        Thread.sleep(30 * 1000);
    }*/

    @Override
    protected void tearDown() throws Exception {
        if (serial != null)
            serial.stop();

        super.tearDown();
    }
}
