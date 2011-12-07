package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.ReportProtocolVersionMessage;
import name.antonsmirnov.firmata.serializer.ReportProtocolVersionMessageSerializer;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for ReportProtocolVersionMessage
 */
public class ReportProtocolVersionMessageTest extends BaseFirmataTest {

    @Test
    public void testWrite() {
        serial.clear();
        firmata.write(new ReportProtocolVersionMessage());

        byte[] expected_output = new byte[] { (byte)ReportProtocolVersionMessageSerializer.COMMAND };
        byte[] actual_output = serial.getOutputStream().toByteArray();

        assertTrue(Arrays.equals(expected_output, actual_output));

    }
}
