package name.antonsmirnov.firmata.serial;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Adapts byte[] to be used as IByteBuffer
 */
public class ByteArrayByteBufferAdapter implements IByteBuffer {

    private byte[] array;

    public ByteArrayByteBufferAdapter(byte[] array) {
        this.array = array;
        reset();
    }

    private void reset() {
        readIndex.set(0);
        writeIndex.set(0);
    }

    private AtomicInteger readIndex = new AtomicInteger();
    private AtomicInteger writeIndex = new AtomicInteger();
    
    public void add(byte value) {
        array[writeIndex.getAndIncrement()] = value;
    }

    public byte get() {
        if (size() <= 0)
            return -1;

        byte outcomingByte = array[readIndex.getAndIncrement()];

        // if reached written count
        if (readIndex == writeIndex) {
            reset();
        }

        return outcomingByte;
    }

    public void clear() {
        reset();
    }

    public int size() {
        return writeIndex.get() - readIndex.get();
    }
}
