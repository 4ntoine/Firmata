package name.antonsmirnov.firmata.tests;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.TestSerial;
import name.antonsmirnov.firmata.serial.BufferingSerialWrapper;
import name.antonsmirnov.firmata.serial.ISerialListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test for BufferingSerialWrapper
 */
public class BufferingSerialWrapperTest extends TestCase {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int MAX_VALUE = 10 * 1000;

    private static final int SLEEP_DELAY = 100;
    private static final int SLEEP_MAX = 20 * 1000; // 20 sec

    /**
     * Thread that simulates incoming bytes in TestSerial
     */
    private class TestThread extends Thread {

        private TestSerial serial;
        private int maxValue;

        public TestThread(TestSerial serial, int maxValue) {
            super();
            setPriority(Thread.MAX_PRIORITY);

            this.serial = serial;
            this.maxValue = maxValue;
        }

        @Override
        public void run() {
            super.run();
            
            for (int i=0; i<maxValue; i++) {
                log.info("Put '{}' as incoming byte", i);
                serial.simulateIncomingByte(i);
            }
        }
    }

    private TestThread writingThread;

    private AtomicInteger maxRead = new AtomicInteger();
    
    @Test
    public void testWriteRead() throws InterruptedException {
        final TestSerial serial = new TestSerial();
        final BufferingSerialWrapper bufferingWrapper = new BufferingSerialWrapper(serial);
        // reading thread is slower than writing to check buffer filling
        bufferingWrapper.setThreadPriority(Thread.MIN_PRIORITY);

        bufferingWrapper.start();

        writingThread = new TestThread(serial, MAX_VALUE);
        bufferingWrapper.setListener(new ISerialListener() {
            public void onDataReceived(Object serialImpl) {
                maxRead.set(bufferingWrapper.read());
                log.info("Read '{}' as incoming byte (size={})", maxRead.intValue(), bufferingWrapper.available());
            }
        });

        // start simulating incoming bytes
        writingThread.start();
        
        int slept = 0;
        while (maxRead.get() < (MAX_VALUE-1)) {
            Thread.sleep(SLEEP_DELAY);
            slept += SLEEP_DELAY;
            if (slept > SLEEP_MAX)
                throw new RuntimeException("did not received the last value");
        }

        bufferingWrapper.stop();
    }
}
