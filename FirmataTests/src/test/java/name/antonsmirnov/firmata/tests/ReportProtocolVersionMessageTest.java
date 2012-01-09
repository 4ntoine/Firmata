package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.ReportProtocolVersionMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.writer.ReportProtocolVersionMessageWriter;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for ReportProtocolVersionMessage
 */
public class ReportProtocolVersionMessageTest extends BaseFirmataTest {

    @Test
    public void testWrite() throws SerialException {
        serial.clear();
        firmata.send(new ReportProtocolVersionMessage());

        byte[] expected_output = new byte[] { (byte) ReportProtocolVersionMessageWriter.COMMAND };
        byte[] actual_output = serial.getOutputStream().toByteArray();

        assertTrue(Arrays.equals(expected_output, actual_output));
    }
}
