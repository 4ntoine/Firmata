package name.antonsmirnov.firmata.tests.hardware;

import name.antonsmirnov.firmata.FirmataWaiter;
import name.antonsmirnov.firmata.WaitException;
import name.antonsmirnov.firmata.message.StringSysexMessage;
import name.antonsmirnov.firmata.serial.SerialException;
import name.antonsmirnov.firmata.wrapper.MessageWithProperties;
import org.junit.Test;

/**
 * Test to receive the string sent
 * (upload EchoString firmware to the board)
 */
public class EchoStringHardwareTest extends BaseHardwareTest {

    private static final String STRING = "ping";
    
    @Test
    public void testEcho() throws WaitException, SerialException {
        firmata.send(new StringSysexMessage(STRING));
        new FirmataWaiter(firmata).waitSeconds(10, StringSysexMessage.class);

        MessageWithProperties message = historyFirmataWrapper.getLastReceivedMessageWithProperties();
        assertEquals(STRING, ((StringSysexMessage) message.getMessage()).getData());
    }
}
