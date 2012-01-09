package name.antonsmirnov.firmata.serial.tests;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.serial.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test for BufferingSerialWrapper
 */
public class BufferingSerialWrapperTest extends TestCase {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final byte MIN_VALUE = Byte.MIN_VALUE;
    private static final byte MAX_VALUE = Byte.MAX_VALUE;

    private static final int SLEEP_DELAY = 100;
    private static final int SLEEP_MAX = 20 * 1000; // 20 sec

    /**
     * Thread that simulates incoming bytes in TestSerial
     */
    public class WriterThread extends Thread {

        private ISerial serial;
        private byte minValue;
        private byte maxValue;

        public WriterThread(ISerial serial, byte minValue, byte maxValue) {
            super();
            setPriority(Thread.MAX_PRIORITY);

            this.serial = serial;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Override
        public void run() {
            super.run();
            
            for (int i=minValue; i<=maxValue; i++) {
                byte outcomingByte = (byte)i;
                log.info("Put '{}' to buffer", outcomingByte);
                try {
                    serial.write(outcomingByte);
                } catch (SerialException e) {
                    log.error("serial exception", e);
                }
            }
        }
    }

    private WriterThread writingThread;

    private AtomicInteger maxRead = new AtomicInteger();
    final ISerial serial = new WritebackSerial();

    @Test
    // ConcurrentLinkedQueue as buffer
    public void testQueue() throws InterruptedException, SerialException {
        testWriteRead(new QueueByteBufferAdapter(new ConcurrentLinkedQueue<Byte>()));
    }

    @Test
    // byte[] as buffer
    public void testByteArray() throws InterruptedException, SerialException {
        testWriteRead(new ByteArrayByteBufferAdapter(new byte[256]));
    }

    private void testWriteRead(IByteBuffer buffer) throws InterruptedException, SerialException {
        final BufferingSerialWrapper bufferingWrapper = new BufferingSerialWrapper(serial, buffer);

        // reading thread is slower than writing to check buffer filling
        bufferingWrapper.setThreadPriority(Thread.MIN_PRIORITY);

        bufferingWrapper.start();

        writingThread = new WriterThread(serial, MIN_VALUE, MAX_VALUE);
        bufferingWrapper.addListener(new ISerialListener() {
            public void onDataReceived(Object serialImpl) {
                byte incomingByte = (byte) bufferingWrapper.read();
                maxRead.set(incomingByte);
                final int available = bufferingWrapper.available();
                log.info("Read '{}' from buffer (size={})", maxRead.intValue(), available);
            }

            public void onException(Throwable e) {
                log.error("serial exception", e);
            }
        });

        // start simulating incoming bytes
        writingThread.start();

        int slept = 0;
        while (maxRead.get() < (MAX_VALUE-1)) {
            Thread.sleep(SLEEP_DELAY);
            slept += SLEEP_DELAY;
            if (slept > SLEEP_MAX)
                throw new RuntimeException("did not receive the last value");
        }

        bufferingWrapper.stop();

        assertEquals(0, buffer.size());
    }
}
