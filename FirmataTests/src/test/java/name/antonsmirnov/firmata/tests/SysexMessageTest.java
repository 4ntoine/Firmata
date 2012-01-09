package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.message.SysexMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import org.junit.Test;

/**
 * Test for SysexMessage
 */
public class SysexMessageTest extends BaseFirmataTest {

    private final SysexMessage originalMessage = new SysexMessage(1, "abc");

    @Test
    public void testWriteRead() throws SerialException {
        serial.clear();
        firmata.send(originalMessage);
        byte[] output = serial.getOutputStream().toByteArray();

        serial.clear();
        feedToFirmata(output);

        Message actualMessage = historyFirmataWrapper.getLastReceivedMessage();
        assertNotNull(actualMessage);
        assertEquals(originalMessage, actualMessage);
    }
}
