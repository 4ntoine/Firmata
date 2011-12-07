package name.antonsmirnov.firmata.tests;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.Firmata;
import name.antonsmirnov.firmata.OriginalFirmata;
import name.antonsmirnov.firmata.TestSerial;

/**
 * Base Firmata Test
 */
public abstract class BaseFirmataTest extends TestCase {

    protected static final int PIN_MAX = 16;
    protected static final int PORT_MAX = 3;
    protected static final int  BYTE_MAX = 256;

    protected TestSerial serial;

    // original impl
    protected OriginalFirmata originalFirmata;

    // new impl
    protected Firmata firmata;

    @Override
    protected void setUp() throws Exception {
        serial = new TestSerial();
        originalFirmata = new OriginalFirmata(serial);
        firmata = new Firmata(serial);
        firmata.setListener(new Firmata.StubListener());
    }

    protected void feedToFirmata(byte[] input) {
        for (byte eachByte : input)
            firmata.onDataReceived(eachByte);
    }

}
