package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.SamplingIntervalMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.writer.SysexMessageWriter;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for SamplingIntervalMessage
 */
public class SamplingIntervalMessageTest extends BaseFirmataTest {

    @Test
    public void testWrite() throws SerialException {
        serial.clear();

        SamplingIntervalMessage message = new SamplingIntervalMessage();
        message.setInterval(10);

        firmata.send(message);

        byte[] expected_output = new byte[] {
            (byte)SysexMessageWriter.COMMAND_START,
            (byte)SamplingIntervalMessage.COMMAND,

            // interval
            (byte)10,
            (byte)0,

            (byte)SysexMessageWriter.COMMAND_END
        };
        byte[] actual_output = serial.getOutputStream().toByteArray();

        assertTrue(Arrays.equals(expected_output, actual_output));
    }
}
