package name.antonsmirnov.firmata.serial;

/**
 * Byte buffer interface
 * (can not use java.util.Queue interface because it works with Objects but not with Primitives)
 */
public interface IByteBuffer {

    /**
     * Add data
     * @param value byte data
     */
    void add(byte value);

    /**
     * Get data
     * @return byte data
     */
    byte get();

    /**
     * Clear data
     */
    void clear();

    /**
     * Buffered data size
     * (not buffer size)
     * @return buffered data size
     */
    int size();
}
