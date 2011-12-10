package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.ReportDigitalPortMessage;
import org.junit.Test;

/**
 * Test for ReportDigitalPortMessage
 */
public class ReportDigitalPortMessageTest extends BaseFirmataTest {

    @Test
    public void testWrite() {
        for (int port = 0; port < PORT_MAX; port++) {
            assertOk(port, true);
            assertOk(port, false);
        }
    }

    private void assertOk(int port, boolean enable) {
        serial.clear();
        ReportDigitalPortMessage message = new ReportDigitalPortMessage(port, enable);
        firmata.send(message);
        byte[] output = serial.getOutputStream().toByteArray();

        assertNotNull(output);
        assertEquals(2, output.length);
    }
}
