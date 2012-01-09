package name.antonsmirnov.firmata.serial;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * WriteBack Serial
 * (written bytes are returned back as input bytes)
 */
public class WritebackSerial extends StreamingSerialAdapter {

    private PipedInputStream pipedInStream;
    private PipedOutputStream pipedOutStream;

    public WritebackSerial() {
        super();

        pipedOutStream = new PipedOutputStream();
        try {
            pipedInStream = new PipedInputStream(pipedOutStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setInStream(pipedInStream);
        setOutStream(pipedOutStream);
    }

}
