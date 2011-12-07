package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.deserializer.FirmwareVersionMessageDeserializer;
import name.antonsmirnov.firmata.message.FirmwareVersionMessage;
import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.message.ReportFirmwareVersionMessage;
import name.antonsmirnov.firmata.serializer.SysexMessageSerializer;
import org.junit.Test;

import java.nio.ByteBuffer;

import static name.antonsmirnov.firmata.BytesHelper.ENCODE_STRING;
import static name.antonsmirnov.firmata.BytesHelper.LSB;
import static name.antonsmirnov.firmata.BytesHelper.MSB;

/**
 * Test for FirmwareVersionMessage
 */
public class FirmwareVersionMessageTest extends BaseFirmataTest {

    private final FirmwareVersionMessage expectedMessage = new FirmwareVersionMessage(1, 2, "abc");

    private byte[] getInput() {
        final byte[] NAME = expectedMessage.getName().getBytes();

        // prepare input
        byte[] input = new byte[5 + NAME.length * 2];
        // 5: start byte + sysex command byte + major byte + minor byte + end byte
        // 2: each name byte is encoded into 2 bytes

        int offset = 0;
        input[offset++] = (byte)SysexMessageSerializer.COMMAND_START;
        input[offset++] = (byte)ReportFirmwareVersionMessage.COMMAND;
        input[offset++] = (byte)expectedMessage.getMajor();
        input[offset++] = (byte)expectedMessage.getMinor();
        ENCODE_STRING(NAME, ByteBuffer.wrap(input), offset);
        offset += 2 * NAME.length;
        input[offset] = (byte)SysexMessageSerializer.COMMAND_END;

        return input;
    }

    @Test
    public void testDeserialize() {

        byte[] input = getInput();

        // feed input
        FirmwareVersionMessageDeserializer deserializer = new FirmwareVersionMessageDeserializer();
        deserializer.startHandling();
        // from 2 (not 0), because COMMAND_START and sysex command bytes were walked during canHandle()
        for (int i=2; i<input.length; i++)
            deserializer.handle(input, i+1);

        assertTrue(deserializer.finishedHandling());

        FirmwareVersionMessage actualMessage = deserializer.getMessage();
        assertNotNull(actualMessage);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRead() {
        byte[] input = getInput();
        feedToFirmata(input);

        Message actualMessage = firmata.getLastReceivedMessage();
        assertNotNull(actualMessage);
        assertEquals(FirmwareVersionMessage.class, actualMessage.getClass());
        assertEquals(expectedMessage, actualMessage);
    }

}
