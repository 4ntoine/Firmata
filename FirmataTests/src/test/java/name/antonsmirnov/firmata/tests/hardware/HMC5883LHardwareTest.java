package name.antonsmirnov.firmata.tests.hardware;

import name.antonsmirnov.firmata.FirmataWaiter;
import name.antonsmirnov.firmata.WaitException;
import name.antonsmirnov.firmata.message.*;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.wrapper.MessageWithProperties;
import org.junit.Test;

import java.util.List;

/**
 * Test communication I2c with HMC5883L mag
 */
public class HMC5883LHardwareTest extends BaseHardwareTest {

    public static final int HMC5883L_Address = 0x1E;
    public static final int ConfigurationRegisterA = 0x00;
    public static final int ConfigurationRegisterB = 0x01;
    public static final int ModeRegister = 0x02;
    public static final int DataRegisterBegin = 0x03;

    public static final int Measurement_Continuous = 0x00;
    public static final int Measurement_SingleShot = 0x01;
    public static final int Measurement_Idle = 0x03;

    protected I2cRequestMessage newMessage() {
        I2cRequestMessage message = new I2cRequestMessage();
        message.setTenBitsMode(false);
        message.setSlaveAddress(HMC5883L_Address);
        message.setMode(I2cRequestMessage.MODE.WRITE);
        return message;
    }
    
    protected void setMeasurementMode(int mode) throws SerialException {
        // select register and mode
        I2cRequestMessage selectRegisterMessage = newMessage();
        selectRegisterMessage.setBinaryData(new int[]{ ModeRegister, mode });
        firmata.send(selectRegisterMessage);
    }

    protected void startMeasurement() throws SerialException {
        I2cRequestMessage startMessage = newMessage();
        startMessage.setBinaryData(new int[] { DataRegisterBegin });
        firmata.send(startMessage);
    }

    private void setModes() throws SerialException {
        /*SetPinModeMessage mode = new SetPinModeMessage(13, SetPinModeMessage.PIN_MODE.OUTPUT.getMode());
        firmata.send(mode);*/

        I2cConfigMessage message = new I2cConfigMessage();
        message.setOn(true);
        message.setDelay(100);
        firmata.send(message);
    }

    private void startReadingMeasurement(I2cRequestMessage.MODE mode) throws SerialException {
        I2cReadRequestMessage readMessage = new I2cReadRequestMessage();
        readMessage.setSlaveAddress(HMC5883L_Address);
        readMessage.setMode(mode);
        readMessage.setSlaveRegister(DataRegisterBegin);
        readMessage.setBytesToRead(3); // x + y + z bytes
        firmata.send(readMessage);
    }

    private boolean needStopMsr = false;

    private void stopReadingMsr() throws SerialException {
        // stop firmata
        I2cRequestMessage stopFirmataMessage = newMessage();
        stopFirmataMessage.setMode(I2cRequestMessage.MODE.STOP_READING);
        firmata.send(stopFirmataMessage);

        // stop the board
        I2cRequestMessage stopBoardMessage = newMessage();
        stopBoardMessage.setBinaryData(new int[] { Measurement_Idle });
        firmata.send(stopBoardMessage);
    }

    /*@Test
    public void testSingleMeasurement() throws WaitException, SerialException, InterruptedException {
        setModes();

        setMeasurementMode(Measurement_SingleShot);
        startMeasurement();
        startReadingMeasurement(I2cRequestMessage.MODE.READ_ONCE);

        // wait incoming I2cReplyMessage for 5 seconds max
        new FirmataWaiter(firmata).waitSeconds(5, I2cReplyMessage.class);

        // reply
        I2cReplyMessage replyMessage = (I2cReplyMessage) historyFirmataWrapper.getLastReceivedMessageWithProperties().getMessage();
        assertI2ReplyMessageOk(replyMessage);
    }*/

    private void assertI2ReplyMessageOk(I2cReplyMessage replyMessage) {
        assertNotNull(replyMessage);
        assertEquals(HMC5883L_Address, replyMessage.getSlaveAddress());
        assertEquals(3, replyMessage.getBinaryData().length);
    }

    @Test
    public void testContinuousMeasurement() throws WaitException, SerialException, InterruptedException {
        setModes();

        setMeasurementMode(Measurement_Continuous);
        startMeasurement();
        historyFirmataWrapper.clear();
        startReadingMeasurement(I2cRequestMessage.MODE.READ_CONTINUOUSLY);
        needStopMsr = true;

        // reading messages
        Thread.sleep(5 * 1000);

        // stop
        stopReadingMsr();
        serial.stop();
        needStopMsr = false;

        // checking replies
        List<MessageWithProperties> receivedMessages = historyFirmataWrapper.getReceivedMessages();
        assertTrue(receivedMessages.size() > 0);

        for (int i=0; i<receivedMessages.size(); i++) {
            Message message = receivedMessages.get(i).getMessage();

            assertEquals(I2cReplyMessage.class, message.getClass());
            assertI2ReplyMessageOk((I2cReplyMessage) message);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if (needStopMsr)
            stopReadingMsr();

        super.tearDown();
    }
}
