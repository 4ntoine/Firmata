package name.antonsmirnov.firmata.serial;

/**
 * Serial interface
 * (methods required for Firmata only)
 */
public interface ISerial {

    /**
     * Set serial events listener
     * @param listener serial events listener
     */
    void setListener(ISerialListener listener);

    /**
     * Start talking to serial
     */
    void start();

    /**
     * Stop talking to serial
     */
    void stop();

    /**
     * Returns the number of bytes that have been read from serial
     * and are waiting to be dealt with by the user.
     */
    int available();

    /**
     * Clear buffers
     */
    void clear();

    /**
     * Read byte from serial
     * (check available() before)
     */
    int read();

    /**
     * Write byte to serial
     * @param outcomingByte outcoming byte
     */
    void write(int outcomingByte);

    /**
     * Write outcoming bytes to serial
     * @param outcomingBytes bytes to write
     */
    void write(byte[] outcomingBytes);
}
