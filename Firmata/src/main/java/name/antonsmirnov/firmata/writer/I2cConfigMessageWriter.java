package name.antonsmirnov.firmata.writer;

import name.antonsmirnov.firmata.message.I2cConfigMessage;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.SerialException;

import static name.antonsmirnov.firmata.BytesHelper.LSB;
import static name.antonsmirnov.firmata.BytesHelper.MSB;

/**
 * MessageWriter for I2cConfigMessage
 */
public class I2cConfigMessageWriter extends SysexMessageWriter<I2cConfigMessage> {

    @Override
    protected void writeData(I2cConfigMessage message, ISerial serial) throws SerialException {
        // can not use super.writeData() because it works with String
        writeI2cConfigData(message, serial);
    }

    private void writeI2cConfigData(I2cConfigMessage message, ISerial serial) throws SerialException {
        byte[] buffer = new byte[3];

        buffer[0] = (byte)(message.isOn() ? 1 : 0);
        buffer[1] = (byte)LSB(message.getDelay());
        buffer[2] = (byte)MSB(message.getDelay());

        serial.write(buffer);
    }
}
