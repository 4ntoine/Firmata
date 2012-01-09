package name.antonsmirnov.firmata.tests.hardware;

import name.antonsmirnov.firmata.WaitException;
import name.antonsmirnov.firmata.message.ReportDigitalPortMessage;
import name.antonsmirnov.firmata.message.SetPinModeMessage;
import name.antonsmirnov.firmata.message.factory.MessageFactory;
import name.antonsmirnov.firmata.message.factory.MessageValidationException;
import name.antonsmirnov.firmata.message.factory.arduino.Duemilanove;
import name.antonsmirnov.firmata.serial.SerialException;
import org.junit.Test;

/**
 * Test to write digital port value
 * (upload StandardFirmata firmware to the board)
 */
public class DigitalWriteHardwareTest extends BaseHardwareTest {

    public static final int LED_PIN = 13;
    private MessageFactory board;
    private static final int PORT = 1;
    private static final int PORT_BITS = 255;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        board = new Duemilanove();
    }

    @Test
    // test : LED (pin 13) is switched ON for 5 seconds
    public void testWriteRead() throws MessageValidationException, WaitException, InterruptedException, SerialException {
        // set pin mode : output
        firmata.send(new SetPinModeMessage(LED_PIN, SetPinModeMessage.PIN_MODE.OUTPUT.getMode()));
        // set port value : all to HIGH
        firmata.send(board.digitalWrite(PORT, PORT_BITS));
        Thread.sleep(5000);
    }

    @Override
    protected void tearDown() throws Exception {
        firmata.send(new ReportDigitalPortMessage(PORT, false));
        super.tearDown();
    }
}
