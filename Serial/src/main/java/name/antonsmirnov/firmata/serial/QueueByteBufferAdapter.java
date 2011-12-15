package name.antonsmirnov.firmata.serial;

import java.util.Queue;

/**
 * Adapts java.util.Queue to be used as IByteBuffer
 */
public class QueueByteBufferAdapter implements IByteBuffer {

    private Queue<Byte> queue;
    
    public QueueByteBufferAdapter(Queue<Byte> queue) {
        this.queue = queue;
    }

    public void add(byte value) {
        queue.add(value);
    }

    public byte get() {
        return queue.poll();
    }

    public void clear() {
        queue.clear();
    }

    public int size() {
        return queue.size();
    }
}
