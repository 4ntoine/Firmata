package name.antonsmirnov.firmata.tests;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.*;
import name.antonsmirnov.firmata.message.AnalogMessage;
import name.antonsmirnov.firmata.message.SetPinModeMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import org.junit.Test;

/**
 * Tests for firmata wrappers
 */
public class FirmataWrappersTest extends TestCase {

    private TestSerial serial = new TestSerial();

    @Test
    // test for PinModeFirmataWrapper
    public void testPinModeWrapper() throws SerialException {
        IFirmata impl = new Firmata(serial);
        PinModeFirmataWrapper wrapper = new PinModeFirmataWrapper(impl, null);
        
        final int PIN1 = 1;
        final int MODE1 = SetPinModeMessage.PIN_MODE.OUTPUT.getMode();

        final int PIN2 = 2;
        final int MODE2 = SetPinModeMessage.PIN_MODE.PWM.getMode();

        // pin messages
        SetPinModeMessage message1 = new SetPinModeMessage(PIN1, MODE1);
        wrapper.send(message1);


        SetPinModeMessage message2 = new SetPinModeMessage(PIN2, MODE2);
        wrapper.send(message2);

        // other messages
        wrapper.send(new AnalogMessage(1, 128));

        assertEquals(2, wrapper.getPinsConfig().size());
        assertTrue(wrapper.getPinsConfig().containsKey(message1.getPin()));
        assertTrue(wrapper.getPinsConfig().containsKey(message2.getPin()));
    }

    @Test
    // test for MessagesHistoryFirmataWrapper
    public void testMessageHistoryWrapper() throws SerialException {
        IFirmata impl = new Firmata(serial);
        MessagesHistoryFirmataWrapper wrapper = new MessagesHistoryFirmataWrapper(impl);

        final int PIN1 = 1;
        final int MODE1 = SetPinModeMessage.PIN_MODE.OUTPUT.getMode();

        final int PIN2 = 2;
        final int MODE2 = SetPinModeMessage.PIN_MODE.PWM.getMode();

        SetPinModeMessage message1 = new SetPinModeMessage(PIN1, MODE1);
        wrapper.send(message1);

        SetPinModeMessage message2 = new SetPinModeMessage(PIN2, MODE2);
        wrapper.send(message2);

        AnalogMessage message3 = new AnalogMessage(1, 128);
        wrapper.send(message3);

        assertEquals(3, wrapper.getSentMessages().size());
        assertTrue(wrapper.getSentMessages().contains(message1));
        assertTrue(wrapper.getSentMessages().contains(message2));
        assertTrue(wrapper.getSentMessages().contains(message3));
    }
}
