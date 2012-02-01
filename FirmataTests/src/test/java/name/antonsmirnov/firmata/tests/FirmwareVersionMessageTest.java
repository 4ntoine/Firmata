package name.antonsmirnov.firmata.tests;

import name.antonsmirnov.firmata.message.FirmwareVersionMessage;
import name.antonsmirnov.firmata.message.ReportFirmwareVersionMessage;
import name.antonsmirnov.firmata.reader.FirmwareVersionMessageReader;
import name.antonsmirnov.firmata.wrapper.MessageWithProperties;
import name.antonsmirnov.firmata.writer.SysexMessageWriter;
import org.junit.Test;

import java.nio.ByteBuffer;

import static name.antonsmirnov.firmata.BytesHelper.ENCODE_STRING;

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
        input[offset++] = (byte) SysexMessageWriter.COMMAND_START;
        input[offset++] = (byte)ReportFirmwareVersionMessage.COMMAND;
        input[offset++] = (byte)expectedMessage.getMajor();
        input[offset++] = (byte)expectedMessage.getMinor();
        ENCODE_STRING(NAME, ByteBuffer.wrap(input), offset);
        offset += 2 * NAME.length;
        input[offset] = (byte) SysexMessageWriter.COMMAND_END;

        return input;
    }

    @Test
    public void testDeserialize() {
        byte[] input = getInput();

        // feed input
        FirmwareVersionMessageReader reader = new FirmwareVersionMessageReader();
        reader.startReading();
        // from 2 (not 0), because COMMAND_START and sysex command bytes were walked during canRead()
        for (int i=2; i<input.length; i++)
            reader.read(input, i + 1);

        assertTrue(reader.finishedReading());

        FirmwareVersionMessage actualMessage = reader.getMessage();
        assertNotNull(actualMessage);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRead() {
        byte[] input = getInput();
        feedToFirmata(input);

        MessageWithProperties actualMessage = historyFirmataWrapper.getLastReceivedMessageWithProperties();
        assertNotNull(actualMessage);
        assertEquals(FirmwareVersionMessage.class, actualMessage.getMessage().getClass());
        assertEquals(expectedMessage, actualMessage.getMessage());
    }

}
