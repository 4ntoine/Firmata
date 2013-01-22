package name.antonsmirnov.firmata.serial;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * Serial adapter for regular TCP socket
 */
public class SocketSerialAdapter extends StreamingSerialAdapter implements Serializable {

    private transient Socket socket;

    private String address;
    private int port;

    public SocketSerialAdapter(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void start() throws SerialException {
        try {
            socket = new Socket(address, port);
            setInStream(socket.getInputStream());
            setOutStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new SerialException(e);
        }
        super.start();
    }

    @Override
    public void stop() throws SerialException {
        setStopReading();

        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            throw new SerialException(e);
        }
        super.stop();
    }
}
