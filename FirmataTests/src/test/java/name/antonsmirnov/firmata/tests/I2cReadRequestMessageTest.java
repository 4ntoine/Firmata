package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.I2cReadRequestMessage;
import name.antonsmirnov.firmata.message.I2cRequestMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.writer.SysexMessageWriter;
import org.junit.Test;

import java.util.Arrays;

import static name.antonsmirnov.firmata.BytesHelper.LSB;
import static name.antonsmirnov.firmata.BytesHelper.MSB;

/**
 * Test for I2cReadRequestMessage
 */
public class I2cReadRequestMessageTest extends BaseFirmataTest {

    private static final int SLAVE_ADDRESS = 7;
    private static final int SLAVE_REGISTER = 3;
    private static final int BYTES_TO_READ = 6;

    @Test
     // read-once mode with specified register and bytes count parameters
     public void testWrite_ReadOnce_RegisterAndBytesToRead() throws SerialException {
        serial.clear();

        I2cReadRequestMessage message = new I2cReadRequestMessage();
        message.setMode(I2cRequestMessage.MODE.READ_ONCE);
        message.setSlaveAddress(SLAVE_ADDRESS);
        message.setTenBitsMode(false);
        message.setSlaveRegister(SLAVE_REGISTER);
        message.setBytesToRead(BYTES_TO_READ);

        firmata.send(message);

        byte[] expected_output = new byte[] {
            (byte)SysexMessageWriter.COMMAND_START,
            (byte)I2cRequestMessage.COMMAND,

            // slave address
            (byte)LSB(SLAVE_ADDRESS),
            8, // mask for READ_ONCE mode

            // slave register
            (byte)LSB(SLAVE_REGISTER),
            (byte)MSB(SLAVE_REGISTER),

            // bytes to read
            (byte)LSB(BYTES_TO_READ),
            (byte)MSB(BYTES_TO_READ),

            (byte)SysexMessageWriter.COMMAND_END
        };
        byte[] actual_output = serial.getOutputStream().toByteArray();
        assertTrue(Arrays.equals(expected_output, actual_output));
    }

    @Test
    // read-once mode with specified bytes count parameter only
    public void testWrite_ReadOnce_BytesToRead() throws SerialException {
        serial.clear();

        I2cReadRequestMessage message = new I2cReadRequestMessage();
        message.setMode(I2cRequestMessage.MODE.READ_ONCE);
        message.setSlaveAddress(SLAVE_ADDRESS);
        message.setTenBitsMode(false);
        message.setSlaveRegister(null);  // !!! no slave register specified
        message.setBytesToRead(BYTES_TO_READ);

        firmata.send(message);

        byte[] expected_output = new byte[] {
            (byte)SysexMessageWriter.COMMAND_START,
            (byte)I2cRequestMessage.COMMAND,

            // slave address
            (byte)LSB(SLAVE_ADDRESS),
            8, // mask for READ_ONCE mode

            // bytes to read
            (byte)LSB(BYTES_TO_READ),
            (byte)MSB(BYTES_TO_READ),

            (byte)SysexMessageWriter.COMMAND_END
        };
        byte[] actual_output = serial.getOutputStream().toByteArray();
        assertTrue(Arrays.equals(expected_output, actual_output));
    }

    @Test
    // read-continuously mode
    public void testWrite_ReadContinuously() throws SerialException {
        serial.clear();

        I2cReadRequestMessage message = new I2cReadRequestMessage();
        message.setMode(I2cRequestMessage.MODE.READ_CONTINUOUSLY);
        message.setSlaveAddress(SLAVE_ADDRESS);
        message.setTenBitsMode(false);
        message.setSlaveRegister(SLAVE_REGISTER);
        message.setBytesToRead(BYTES_TO_READ);

        firmata.send(message);

        byte[] expected_output = new byte[] {
            (byte)SysexMessageWriter.COMMAND_START,
            (byte)I2cRequestMessage.COMMAND,

            // slave address
            (byte)LSB(SLAVE_ADDRESS),
            16, // mask for READ_CONTINUOUS mode

            // slave register
            (byte)LSB(SLAVE_REGISTER),
            (byte)MSB(SLAVE_REGISTER),

            // bytes to read
            (byte)LSB(BYTES_TO_READ),
            (byte)MSB(BYTES_TO_READ),

            (byte)SysexMessageWriter.COMMAND_END
        };
        byte[] actual_output = serial.getOutputStream().toByteArray();
        assertTrue(Arrays.equals(expected_output, actual_output));
    }

}
