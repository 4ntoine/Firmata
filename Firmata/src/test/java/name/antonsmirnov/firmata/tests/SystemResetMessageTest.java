package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.SystemResetMessage;
import name.antonsmirnov.firmata.serializer.SystemResetMessageSerializer;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for SystemResetMessage
 */
public class SystemResetMessageTest extends BaseFirmataTest {

    private final SystemResetMessage message = new SystemResetMessage();

    @Test
    public void testWrite() {
        serial.clear();
        firmata.send(message);

        byte[] expectedOutput = new byte[] { (byte)SystemResetMessageSerializer.COMMAND };
        byte[] actualOutput = serial.getOutputStream().toByteArray();

        assertTrue(Arrays.equals(expectedOutput, actualOutput));

    }
}
