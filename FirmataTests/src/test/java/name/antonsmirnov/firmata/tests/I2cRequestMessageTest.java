package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.I2cRequestMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.writer.SysexMessageWriter;
import org.junit.Test;

import static name.antonsmirnov.firmata.BytesHelper.*;

import java.util.Arrays;

/**
 * Test for I2cRequestMessage
 */
public class I2cRequestMessageTest extends BaseFirmataTest {

    protected void testWrite(I2cRequestMessage.MODE mode, boolean tenBitsMode, byte modeByte) throws SerialException {
        serial.clear();

        I2cRequestMessage message = new I2cRequestMessage();
        message.setSlaveAddress(7);
        message.setMode(mode);
        message.setTenBitsMode(tenBitsMode);
        message.setBinaryData(new int[] { 10, 20, 30, 300 });

        firmata.send(message);

        byte[] expected_output = new byte[] {
            (byte)SysexMessageWriter.COMMAND_START,
            (byte)I2cRequestMessage.COMMAND,

            // slave address
            (byte)LSB(7),
            modeByte,

            // data byte 1
            (byte)10,
            (byte)0,

            // data byte 2
            (byte)20,
            (byte)0,

            // data byte 3
            (byte)30,
            (byte)0,

            // data byte 4
            (byte)44,
            (byte)2,

            (byte)SysexMessageWriter.COMMAND_END
        };
        byte[] actual_output = serial.getOutputStream().toByteArray();

        assertTrue(Arrays.equals(expected_output, actual_output));
    }
    
    @Test
    public void testWrite_1() throws SerialException {
        testWrite(I2cRequestMessage.MODE.READ_CONTINUOUSLY, true, (byte)48);
    }

    @Test
    public void testWrite_2() throws SerialException {
        testWrite(I2cRequestMessage.MODE.READ_CONTINUOUSLY, false, (byte)16);
    }

    @Test
    public void testWrite_3() throws SerialException {
        testWrite(I2cRequestMessage.MODE.WRITE, false, (byte)0);
    }

    @Test
    public void testWrite_4() throws SerialException {
        testWrite(I2cRequestMessage.MODE.READ_ONCE, false, (byte)8);
    }

    @Test
    public void testWrite_5() throws SerialException {
        testWrite(I2cRequestMessage.MODE.READ_CONTINUOUSLY, false, (byte)16);
    }

    @Test
    public void testWrite_6() throws SerialException {
        testWrite(I2cRequestMessage.MODE.STOP_READING, false, (byte)24);
    }
}
