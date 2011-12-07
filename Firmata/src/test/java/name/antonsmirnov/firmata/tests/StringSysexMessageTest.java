package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.message.StringSysexMessage;
import org.junit.Test;

/**
 * Test for StringSysexMessage
 */
public class StringSysexMessageTest extends BaseFirmataTest {

    private final StringSysexMessage originalMessage = new StringSysexMessage("abc");

    @Test
    public void testWriteRead() {
        serial.clear();

        firmata.write(originalMessage);
        byte[] output = serial.getOutputStream().toByteArray();

        serial.clear();
        feedToFirmata(output);

        Message actualMessage = firmata.getLastReceivedMessage();
        assertNotNull(actualMessage);

        assertEquals(originalMessage, actualMessage);
    }
}
