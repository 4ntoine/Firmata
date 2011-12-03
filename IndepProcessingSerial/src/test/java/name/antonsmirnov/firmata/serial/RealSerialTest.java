package name.antonsmirnov.firmata.serial;

import junit.framework.TestCase;
import org.junit.Test;
import processing.serial.IndepProcessingSerial;

import javax.sound.sampled.Port;

/**
 * To test with blinking LED to see board reaction
 * (should not be used in CI (configured to be skipped in pom.xml), just to see with eye };)
 */
public class RealSerialTest extends TestCase {

    private ISerial serial;

    // default port
    private static final String PORT_NAME = "COM8";
    private static final int PORT_BAUD_RATE = 9600;

    private static final int SLEEP_TIME = 1000; // 1 sec

    @Test
    public void testWrite() {
        serial = new IndepProcessingSerialAdapter(new IndepProcessingSerial(PORT_NAME, PORT_BAUD_RATE));
        try {
            serial.start();
            sleep();
            serial.write("-"); // LED will be switched ON
            sleep();
            serial.write("-"); // LED will be switched OFF
            sleep();
            System.out.println(serial.readString());
        } finally{
            serial.stop();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
