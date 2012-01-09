package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.ReportDigitalPortMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import org.junit.Test;

/**
 * Test for ReportDigitalPortMessage
 */
public class ReportDigitalPortMessageTest extends BaseFirmataTest {

    @Test
    public void testWrite() throws SerialException {
        for (int port = 0; port < PORT_MAX; port++) {
            assertOk(port, true);
            assertOk(port, false);
        }
    }

    private void assertOk(int port, boolean enable) throws SerialException {
        serial.clear();
        ReportDigitalPortMessage message = new ReportDigitalPortMessage(port, enable);
        firmata.send(message);
        byte[] output = serial.getOutputStream().toByteArray();

        assertNotNull(output);
        assertEquals(2, output.length);
    }
}
