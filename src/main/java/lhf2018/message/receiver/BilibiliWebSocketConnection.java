package lhf2018.message.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BilibiliWebSocketConnection {
    @Autowired
    private ReceiverClient receiverClient;

    @PostConstruct
    private void connect() throws InterruptedException {
        receiverClient.connectBlocking();
    }
}
