package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.StringSysexMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.wrapper.MessageWithProperties;
import org.junit.Test;

/**
 * Test for StringSysexMessage
 */
public class StringSysexMessageTest extends BaseFirmataTest {

    private final StringSysexMessage originalMessage = new StringSysexMessage("abc");

    @Test
    public void testWriteRead() throws SerialException {
        serial.clear();

        firmata.send(originalMessage);
        byte[] output = serial.getOutputStream().toByteArray();

        serial.clear();
        feedToFirmata(output);

        MessageWithProperties actualMessage = historyFirmataWrapper.getLastReceivedMessageWithProperties();
        assertNotNull(actualMessage);
        assertEquals(originalMessage, actualMessage.getMessage());
    }
}
