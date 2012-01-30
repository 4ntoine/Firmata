package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.ServoConfigMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.writer.ServoConfigMessageWriter;
import name.antonsmirnov.firmata.writer.SysexMessageWriter;
import static name.antonsmirnov.firmata.BytesHelper.*;
import org.junit.Test;

import java.util.Arrays;

/**
 *  Test for ServoConfigMessage
 */
public class ServoConfigMessageTest extends BaseFirmataTest {

    @Test
    public void testWrite() throws SerialException {
        serial.clear();

        ServoConfigMessage message = new ServoConfigMessage();
        message.setPin(1);
        message.setMinPulse(10);
        message.setMaxPulse(300);
        message.setAngle(45);

        firmata.send(message);

        byte[] expected_output = new byte[] {
            (byte)SysexMessageWriter.COMMAND_START,
            (byte)ServoConfigMessage.COMMAND,

            // pin
            (byte)1,

            // min pulse
            (byte)10,
            (byte)0,

            // max pulse
            (byte)44,
            (byte)2,

            // angle
            (byte)45,
            (byte)0,

            (byte)SysexMessageWriter.COMMAND_END
        };
        byte[] actual_output = serial.getOutputStream().toByteArray();

        assertTrue(Arrays.equals(expected_output, actual_output));
    }
}
