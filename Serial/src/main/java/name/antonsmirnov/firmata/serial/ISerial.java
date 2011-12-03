package name.antonsmirnov.firmata.serial;

import java.util.Properties;

/**
 * Serial Port interface
 * (extracted from Processing Serial impl)
 */
public interface ISerial {

    void setProperties(Properties props);

    /**
     * Open port
     */
    void start();

    /**
     * Stop talking to serial and shut things down.
     * <P>
     * Basically just a user-accessible version of dispose().
     * For now, it just calls dispose(), but dispose shouldn't
     * be called from applets, because in some libraries,
     * dispose() blows shit up if it's called by a user who
     * doesn't know what they're doing.
     */
    void stop();

    /**
    * Set the DTR line.
    */
    void setDTR(boolean state);

    /**
     * Returns the number of bytes that have been read from serial
     * and are waiting to be dealt with by the user.
     */
    int available();

    /**
     * Ignore all the bytes read so far and empty the buffer.
     */
    void clear();

    /**
     * Returns a number between 0 and 255 for the next byte that's
     * waiting in the buffer.
     * Returns -1 if there was no byte (although the user should
     * first check available() to see if things are ready to avoid this)
     */
    int read();

    /**
     * Same as read() but returns the very last value received
     * and clears the buffer. Useful when you just want the most
     * recent value sent over the port.
     */
    int last();

    /**
     * Returns the next byte in the buffer as a char.
     * Returns -1, or 0xffff, if nothing is there.
     */
    char readChar();

    /**
     * Just like last() and readChar().
     */
    char lastChar();

    /**
     * Return a byte array of anything that's in the serial buffer.
     * Not particularly memory/speed efficient, because it creates
     * a byte array on each read, but it's easier to use than
     * readBytes(byte b[]) (see below).
     */
    byte[] readBytes();

    /**
     * Grab whatever is in the serial buffer, and stuff it into a
     * byte buffer passed in by the user. This is more memory/time
     * efficient than readBytes() returning a byte[] array.
     *
     * Returns an int for how many bytes were read. If more bytes
     * are available than can fit into the byte array, only those
     * that will fit are read.
     */
    int readBytes(byte outgoing[]);

    /**
     * Reads from the serial port into a buffer of bytes up to and
     * including a particular character. If the character isn't in
     * the serial buffer, then 'null' is returned.
     */
    byte[] readBytesUntil(int interesting);

    /**
     * Reads from the serial port into a buffer of bytes until a
     * particular character. If the character isn't in the serial
     * buffer, then 'null' is returned.
     *
     * If outgoing[] is not big enough, then -1 is returned,
     *   and an error message is printed on the console.
     * If nothing is in the buffer, zero is returned.
     * If 'interesting' byte is not in the buffer, then 0 is returned.
     */
    int readBytesUntil(int interesting, byte outgoing[]);

    /**
     * Return whatever has been read from the serial port so far
     * as a String. It assumes that the incoming characters are ASCII.
     *
     * If you want to move Unicode data, you can first convert the
     * String to a byte stream in the representation of your choice
     * (i.e. UTF8 or two-byte Unicode data), and send it as a byte array.
     */
    String readString();

    /**
     * Combination of readBytesUntil and readString. See caveats in
     * each function. Returns null if it still hasn't found what
     * you're looking for.
     *
     * If you want to move Unicode data, you can first convert the
     * String to a byte stream in the representation of your choice
     * (i.e. UTF8 or two-byte Unicode data), and send it as a byte array.
     */
    String readStringUntil(int interesting);

    /**
     * This will handle both ints, bytes and chars transparently.
     */
    void write(int what);

    void write(byte bytes[]);

    /**
     * Write a String to the output. Note that this doesn't account
     * for Unicode (two bytes per char), nor will it send UTF8
     * characters.. It assumes that you mean to send a byte buffer
     * (most often the case for networking and serial i/o) and
     * will only use the bottom 8 bits of each char in the string.
     * (Meaning that internally it uses String.getBytes)
     *
     * If you want to move Unicode data, you can first convert the
     * String to a byte stream in the representation of your choice
     * (i.e. UTF8 or two-byte Unicode data), and send it as a byte array.
     */
    void write(String what);
}
