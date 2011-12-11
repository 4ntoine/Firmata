package name.antonsmirnov.firmata.tests.hardware;

import name.antonsmirnov.firmata.FirmataWaiter;
import name.antonsmirnov.firmata.WaitException;
import name.antonsmirnov.firmata.message.ProtocolVersionMessage;
import name.antonsmirnov.firmata.message.ReportProtocolVersionMessage;
import org.junit.Test;

/**
 * Firmata protocol hardware test
 * (is NOT started by maven, should be started manually when the board is ready)
 */
public class ReportProtocolHardwareTest extends BaseHardwareTest {

    @Test
    public void testReportProtocol() throws WaitException {
        firmata.send(new ReportProtocolVersionMessage());

        // wait ProtocolVersionMessage for 5 seconds max
        new FirmataWaiter(firmata).waitSeconds(5, ProtocolVersionMessage.class);
    }

}
