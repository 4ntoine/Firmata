package name.antonsmirnov.firmata.tests;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.*;
import name.antonsmirnov.firmata.IFirmata;
import name.antonsmirnov.firmata.wrapper.MessagesHistoryWrapper;

/**
 * Base Firmata Test
 */
public abstract class BaseFirmataTest extends TestCase {

    protected static final int PIN_MAX = 16;
    protected static final int PORT_MAX = 3;
    protected static final int BYTE_MAX = 255;

    protected TestSerial serial;

    // original impl
    protected OriginalFirmata originalFirmata;

    // new impl
    protected Firmata impl;
    protected IFirmata firmata;
    protected MessagesHistoryWrapper historyFirmataWrapper;

    @Override
    protected void setUp() throws Exception {
        serial = new TestSerial();
        originalFirmata = new OriginalFirmata(serial);

        impl = new Firmata(serial);
        historyFirmataWrapper = new MessagesHistoryWrapper(impl);

        firmata = historyFirmataWrapper;
    }

    protected void feedToFirmata(byte[] input) {
        for (byte eachByte : input)
            firmata.onDataReceived(eachByte);
    }

}
