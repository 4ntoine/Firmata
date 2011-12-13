package name.antonsmirnov.firmata;

import java.nio.ByteBuffer;

/**
 * Helps to prepare bytes data
 */
public class BytesHelper {

    /**
     * @param channel command channel
     * @return command channel mask
     */
    public static int ENCODE_CHANNEL(int channel) {
        return channel & 0x0F;
    }

    /**
     * Decode command from byte
     *
     * @param incomingByte
     * @return
     */
    public static int DECODE_COMMAND(int incomingByte) {
        return incomingByte < 0xF0
                ? incomingByte & 0xF0
                : incomingByte;
    }

    /**
     * Decode channel from byte
     *
     * @param incomingByte
     * @return
     */
    public static int DECODE_CHANNEL(int incomingByte) {
        return incomingByte & 0x0F;
    }

    /**
     * Return less significant byte
     *
     * @param value value
     * @return less significant byte
     */
    public static int LSB(int value) {
        return value & 0x7F;
    }

    /**
     * Return most significant byte
     *
     * @param value value
     * @return most significant byte
     */
    public static int MSB(int value) {
        return (value >> 7) & 0x7F;
    }

    /**
     * Return byte from LSB and MSB
     *
     * @param lsb less significant byte
     * @param msb most significant byte
     * @return byte
     */
    public static int DECODE_BYTE(int lsb, int msb) {
        return (msb << 7) + lsb;
    }

    /**
     * Decode string that was encoded using LSB(byte), MSB(byte)
     *
     * @param buffer  buffer
     * @param startIndex start index
     * @param endIndex end index
     * @return decoded string
     */
    public static String DECODE_STRING(byte[] buffer, int startIndex, int endIndex) {
        StringBuilder sb = new StringBuilder();
        int offset = startIndex;
        int length = (endIndex - startIndex + 1) / 2;
        for (int i=0; i<length; i++) {
            sb.append((char)DECODE_BYTE(buffer[offset++], buffer[offset++]));
        }
        return sb.toString();
    }

    /**
     * Encode string - every byte goes to LSB(byte), MSB(byte)
     *
     * @param data string data
     * @return encoded bytes arrray
     */
    public static byte[] ENCODE_STRING(String data) {
        byte[] original_data = data.getBytes();
        byte[] encoded_data = new byte[original_data.length * 2];
        ENCODE_STRING(original_data, ByteBuffer.wrap(encoded_data), 0);
        return encoded_data;
    }

    /**
     * Encode string to existing buffer - every byte goes to LSB(byte), MSB(byte)
     *
     * @param original_data string data
     * @param buffer existing buffer
     * @param offset offset in buffer
     */
    public static void ENCODE_STRING(byte[] original_data, ByteBuffer buffer, int offset) {
        for (int i=0; i<original_data.length; i++) {
            buffer.put(offset++, (byte)LSB(original_data[i]));
            buffer.put(offset++, (byte)MSB(original_data[i]));
        }
    }
}
