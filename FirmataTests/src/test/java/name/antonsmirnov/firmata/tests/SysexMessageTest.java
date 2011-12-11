package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.message.SysexMessage;
import org.junit.Test;

/**
 * Test for SysexMessage
 */
public class SysexMessageTest extends BaseFirmataTest {

    private final SysexMessage originalMessage = new SysexMessage(1, "abc");

    @Test
    public void testWriteRead() {
        serial.clear();
        firmata.send(originalMessage);
        byte[] output = serial.getOutputStream().toByteArray();

        serial.clear();
        feedToFirmata(output);

        Message actualMessage = firmata.getLastReceivedMessage();
        assertNotNull(actualMessage);
        assertEquals(originalMessage, actualMessage);
    }
}
