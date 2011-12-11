package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.DigitalMessage;
import org.junit.Test;

/**
 * Test for DigitalMessage
 */
public class DigitalMessageTest extends BaseFirmataTest {

    @Test
    public void testWrite() {
        serial.clear();

        for (int port = 0; port < PORT_MAX; port++)
            for (int value = 0; value < 2; value++) { // 2 (digital values only)

                // new Firmata impl
                serial.clear();
                DigitalMessage message = new DigitalMessage(port, value);
                firmata.send(message);
                final byte[] newOutput = serial.getOutputStream().toByteArray();

                assertNotNull(newOutput);
                assertEquals(3, newOutput.length);
            }
    }

    @Test
    public void testRead() {
        for (int port = 0; port < PORT_MAX; port++)
            for (int value = 0; value < 2; value++) { // 2 (digital values only)
                // create output
                serial.clear();
                DigitalMessage outcomingMessage = new DigitalMessage(port, value);
                firmata.send(outcomingMessage);
                final byte[] newOutput = serial.getOutputStream().toByteArray();

                // feed output to input
                for (byte eachByte : newOutput)
                    firmata.onDataReceived(eachByte);

                // compare original command and received command
                assertNotNull(firmata.getLastReceivedMessage());
                assertEquals(outcomingMessage, firmata.getLastReceivedMessage());
            }
    }
}
