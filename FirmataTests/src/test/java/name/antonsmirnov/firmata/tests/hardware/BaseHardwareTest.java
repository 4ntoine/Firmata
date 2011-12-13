package name.antonsmirnov.firmata.tests.hardware;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.Firmata;
import name.antonsmirnov.firmata.FirmataWaiter;
import name.antonsmirnov.firmata.message.FirmwareVersionMessage;
import name.antonsmirnov.firmata.message.ProtocolVersionMessage;
import name.antonsmirnov.firmata.serial.ISerial;
import name.antonsmirnov.firmata.serial.IndepProcessingSerialAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.serial.IndepProcessingSerial;

/**
 * Hardware (board) interaction test
 */
public abstract class BaseHardwareTest extends TestCase {

    protected Logger log = LoggerFactory.getLogger(getClass());
    
    public static final String PORT = "COM8";
    public static final int BAUD_RATE = 57600;

    protected ISerial serial;
    protected Firmata firmata;

    @Override
    protected void setUp() throws Exception {
        IndepProcessingSerial serialImpl = new IndepProcessingSerial(PORT, BAUD_RATE);
        serial = new IndepProcessingSerialAdapter(serialImpl);
        firmata = new Firmata(serial);
        firmata.setListener(new Firmata.StubListener() {
            @Override
            public void onUnknownByteReceived(int byteValue) {
                log.info("Received byte: {}('{}')", byteValue, new Character((char)byteValue));
            }
        });

        serial.start();
        log.info("Connected to {} at {}", PORT, BAUD_RATE);

        // waiting for default messages (protocol version and firmware)
        new FirmataWaiter(firmata).waitSeconds(5, ProtocolVersionMessage.class);
        new FirmataWaiter(firmata).waitSeconds(5, FirmwareVersionMessage.class);

        Thread.sleep(10 * 1000);
        log.info("Listening to default messaged finished");
    }

    @Override
    protected void tearDown() throws Exception {
        serial.stop();
    }
}
