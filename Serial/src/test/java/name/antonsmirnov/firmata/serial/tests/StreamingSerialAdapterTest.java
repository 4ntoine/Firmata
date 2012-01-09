package name.antonsmirnov.firmata.serial.tests;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.serial.ISerialListener;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.serial.StreamingSerialAdapter;
import name.antonsmirnov.firmata.serial.WritebackSerial;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test for StreamingSerialAdapter
 */
public class StreamingSerialAdapterTest extends TestCase {

    private Logger log = LoggerFactory.getLogger(getClass());
    
    private AtomicInteger lastReadValue = new AtomicInteger();

    @Test
    // read written bytes test
    public void testWriteRead() throws IOException, InterruptedException, SerialException {
        // written bytes are returned back as input bytes
        WritebackSerial serial = new WritebackSerial();

        PipedOutputStream outStream = new PipedOutputStream();
        PipedInputStream inStream = new PipedInputStream(outStream);

        final StreamingSerialAdapter streamingSerial = new StreamingSerialAdapter(inStream, outStream);
        streamingSerial.addListener(new ISerialListener() {
            public void onDataReceived(Object serialImpl) {
                byte incomingByte = 0;
                try {
                    incomingByte = (byte)streamingSerial.read();
                } catch (SerialException e) {
                    onException(e);
                }
                lastReadValue.set(incomingByte);
                log.info("Read '{}' from serial", incomingByte);
            }

            public void onException(Throwable e) {
                throw new RuntimeException(e);
            }
        });

        streamingSerial.start();

        for (int i=Byte.MIN_VALUE; i<=Byte.MAX_VALUE; i++) {
            byte outcomingByte = (byte)i;
            streamingSerial.write(outcomingByte);
            log.info("Write '{}' to serial ", outcomingByte);
        }

        try {
            // wait for 5 seconds because read thread can be slower than write thread
            Thread.sleep(5 * 1000);
            assertEquals(Byte.MAX_VALUE, lastReadValue.byteValue());
        } finally {
            streamingSerial.stop();
        }

    }
}
