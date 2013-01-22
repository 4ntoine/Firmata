package name.antonsmirnov.firmata;

import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.wrapper.MessagesHistoryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wait for incoming message from firmata
 */
public class FirmataWaiter {

    private Logger log = LoggerFactory.getLogger(getClass());
    
    private final int WAIT_INCREMENT = 10;  // ms
    
    private MessagesHistoryWrapper firmata;

    public FirmataWaiter(IFirmata firmata) {
        this.firmata = new MessagesHistoryWrapper(firmata);
    }

    private int waited;

    public void waitSeconds(int seconds, Class<? extends Message> messageClass) throws WaitException {
        waited = 0;
        int wait = seconds * 1000; // sec -> ms

        log.info("Started waiting {} ...", messageClass != null
                ? "for " + messageClass.getSimpleName()
                : "");
        
        while (true) {
            try {
                Thread.sleep(WAIT_INCREMENT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (firmata.getLastReceivedMessageWithProperties() == null
                ||
                (
                    firmata.getLastReceivedMessageWithProperties() != null
                    &&
                    messageClass != null
                    &&
                    !firmata.getLastReceivedMessageWithProperties().getMessage().getClass().equals(messageClass)
                )) {
                waited += WAIT_INCREMENT;
                if (waited > wait)
                    throw new WaitException(messageClass);
            } else {
                break;
            }
        }
    }

    public void waitSeconds(int seconds) throws WaitException {
        waitSeconds(seconds, null);
    }
}
