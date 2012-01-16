package name.antonsmirnov.firmata.tests;

import junit.framework.TestCase;
import name.antonsmirnov.firmata.BytesHelper;
import org.junit.Test;

/**
 * Tests for BytesHelper
 */
public class BytesHelperTest extends TestCase {
                              //0                      //1                   //2
    private int[] portByPin = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2};
    private int[] pinInPort = {0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7};

    @Test
    public void testPortByPin() {
        for (int pin = 0; pin < portByPin.length; pin++)
            assertEquals(portByPin[pin], BytesHelper.portByPin(pin));
    }

    @Test
    public void testPinInPort() {
        for (int pin = 0; pin < pinInPort.length; pin++)
            assertEquals(pinInPort[pin], BytesHelper.pinInPort(pin));
    }

    @Test
    public void testSetPin_High_One() {
        for (int pin = 0; pin < BytesHelper.BITS_IN_BYTE; pin++) {
            assertEquals((int)Math.pow(2, pin), BytesHelper.setPin(0, pin, true));
        }
    }

    @Test
    public void testSetPin_High_All() {
        int portValues = 0;
        for (int pin = 0; pin < BytesHelper.BITS_IN_BYTE; pin++) {
            // accumulating all bits values
            int currentBitValue = (int)Math.pow(2, pin);
            portValues += currentBitValue;

            assertEquals(portValues, BytesHelper.setPin(portValues, pin, true));
        }
    }

    @Test
    public void testSetPin_Low_One() {
        for (int pin = 0; pin < BytesHelper.BITS_IN_BYTE; pin++) {
            assertEquals(
                BytesHelper.BYTE_MAX_VALUE - (int)Math.pow(2, pin),
                BytesHelper.setPin(BytesHelper.BYTE_MAX_VALUE, pin, false));
        }
    }

    @Test
    public void testSetPin_Low_All() {
        int portValues = BytesHelper.BYTE_MAX_VALUE;
        for (int pin = 0; pin < BytesHelper.BITS_IN_BYTE; pin++) {
            // accumulating all bits values
            int currentBitValue = (int)Math.pow(2, pin);
            portValues -= currentBitValue;

            assertEquals(portValues, BytesHelper.setPin(portValues, pin, false));
        }
    }

    @Test
    public void testPinGet_High_One() {
        for (int setPin=0; setPin<BytesHelper.BITS_IN_BYTE; setPin++) {
            for (int checkPin=0; checkPin<BytesHelper.BITS_IN_BYTE; checkPin++)
                assertEquals(setPin == checkPin, BytesHelper.getPin((int)Math.pow(2, setPin), checkPin));
        }
    }

    @Test
    public void testPinGet_High_All() {
        for (int pin=0; pin<BytesHelper.BITS_IN_BYTE; pin++) {
            assertTrue(BytesHelper.getPin(BytesHelper.BYTE_MAX_VALUE, pin));
        }
    }
}
