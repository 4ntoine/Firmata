package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.ReportAnalogPinMessage;
import org.junit.Test;

/**
 * Test for ReportAnalogPinMessage
 */
public class ReportAnalogPinMessageTest extends BaseFirmataTest {

    @Test
    public void testWrite() {
        for (int pin = 0; pin < PIN_MAX; pin++) {
            assertOk(pin, true);
            assertOk(pin, false);
        }
    }

    private void assertOk(int pin, boolean enable) {
        serial.clear();
        ReportAnalogPinMessage message = new ReportAnalogPinMessage(pin, enable);
        firmata.write(message);
        byte[] output = serial.getOutputStream().toByteArray();

        assertNotNull(output);
        assertEquals(2, output.length);
    }
}
