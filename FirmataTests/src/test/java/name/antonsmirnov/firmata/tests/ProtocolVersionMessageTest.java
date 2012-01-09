package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.message.ProtocolVersionMessage;
import name.antonsmirnov.firmata.writer.ReportProtocolVersionMessageWriter;
import org.junit.Test;

/**
 * Test for ProtocolVersionMessage
 */
public class ProtocolVersionMessageTest extends BaseFirmataTest {

    private final ProtocolVersionMessage expectedMessage = new ProtocolVersionMessage(1, 2);

    @Test
    public void testRead() {
        byte[] input = new byte[] {
            (byte)ReportProtocolVersionMessageWriter.COMMAND,
            (byte)expectedMessage.getMajor(),
            (byte)expectedMessage.getMinor()
        };

        feedToFirmata(input);

        Message actualMessage = historyFirmataWrapper.getLastReceivedMessage();
        assertNotNull(actualMessage);
        assertEquals(expectedMessage, actualMessage);
    }
}
