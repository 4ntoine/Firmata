package name.antonsmirnov.firmata.tests;

import junit.framework.Assert;
import name.antonsmirnov.firmata.message.AnalogMessage;
import name.antonsmirnov.firmata.message.SetPinModeMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for AnalogMessage
 */
public class AnalogMessageTest extends BaseFirmataTest {

    @Test
    // compare original impl output and new impl output
    public void testWrite() throws SerialException {
        serial.clear();

        for (int pin = 0; pin < PIN_MAX; pin++)
            for (int value = 0; value < BYTE_MAX; value++) {

                // old Firmata impl
                serial.clear();
                originalFirmata.analogWrite(pin, value);
                final byte[] oldOutput = serial.getOutputStream().toByteArray();

                // new Firmata impl
                serial.clear();
                firmata.send(new SetPinModeMessage(pin, SetPinModeMessage.PIN_MODE.PWM.getMode()));
                firmata.send(new AnalogMessage(pin, value));
                final byte[] newOutput = serial.getOutputStream().toByteArray();

                assertTrue(Arrays.equals(oldOutput, newOutput));
            }
    }

    @Test
    public void testRead() throws SerialException {
        for (int pin = 0; pin < PIN_MAX; pin++)
            for (int value = 0; value < BYTE_MAX; value++) {
                // create output
                serial.clear();
                AnalogMessage outcomingMessage = new AnalogMessage(pin, value);
                firmata.send(outcomingMessage);
                final byte[] newOutput = serial.getOutputStream().toByteArray();

                // feed output to input
                for (byte eachByte : newOutput)
                    firmata.onDataReceived(eachByte);

                // compare original command and received command
                assertNotNull(historyFirmataWrapper.getLastReceivedMessage());
                Assert.assertEquals(outcomingMessage, historyFirmataWrapper.getLastReceivedMessage());
            }
    }
}
