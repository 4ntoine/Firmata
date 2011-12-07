package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.ReportFirmwareVersionMessage;
import name.antonsmirnov.firmata.serializer.SysexMessageSerializer;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for ReportFirmwareVersionMessage
 */
public class ReportFirmwareVersionMessageTest extends BaseFirmataTest {

    @Test
    public void testSerialize() {
        serial.clear();
        ReportFirmwareVersionMessage message = new ReportFirmwareVersionMessage();
        firmata.write(message);

        byte[] expected_output = new byte[] {
                (byte)SysexMessageSerializer.COMMAND_START,
                (byte)ReportFirmwareVersionMessage.COMMAND,
                (byte)SysexMessageSerializer.COMMAND_END
        };

        byte[] actual_output = serial.getOutputStream().toByteArray();
        assertTrue(Arrays.equals(expected_output, actual_output));
    }

}
