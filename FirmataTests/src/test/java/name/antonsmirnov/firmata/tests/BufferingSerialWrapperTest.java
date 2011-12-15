package name.antonsmirnov.firmata.tests;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.TestSerial;
import name.antonsmirnov.firmata.serial.BufferingSerialWrapper;
import name.antonsmirnov.firmata.serial.IByteBuffer;
import name.antonsmirnov.firmata.serial.ISerialListener;
import name.antonsmirnov.firmata.serial.QueueByteBufferAdapter;
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
    private class TestThread extends Thread {

        private TestSerial serial;
        private int minValue;
        private int maxValue;

        public TestThread(TestSerial serial, byte minValue, byte maxValue) {
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
                log.info("Put '{}' to buffer", i);
                serial.simulateIncomingByte(i);
            }
        }
    }

    private TestThread writingThread;

    private AtomicInteger maxRead = new AtomicInteger();
    final TestSerial serial = new TestSerial();


    @Test
    // ConcurrentLinkedQueue as buffer
    public void testQueue() throws InterruptedException {
        testWriteRead(new QueueByteBufferAdapter(new ConcurrentLinkedQueue<Byte>()));
    }

    private void testWriteRead(IByteBuffer buffer) throws InterruptedException {
        final BufferingSerialWrapper bufferingWrapper = new BufferingSerialWrapper(serial, buffer);

        // reading thread is slower than writing to check buffer filling
        bufferingWrapper.setThreadPriority(Thread.MIN_PRIORITY);

        bufferingWrapper.start();

        writingThread = new TestThread(serial, MIN_VALUE, MAX_VALUE);
        bufferingWrapper.setListener(new ISerialListener() {
            public void onDataReceived(Object serialImpl) {
                maxRead.set(bufferingWrapper.read());
                log.info("Read '{}' from buffer (size={})", maxRead.intValue(), bufferingWrapper.available());
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
