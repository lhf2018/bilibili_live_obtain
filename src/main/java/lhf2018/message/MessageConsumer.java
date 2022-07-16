package lhf2018.message;

import lhf2018.utils.ThreadPoolUtil;
import lhf2018.voice.VoicePlayer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 消费消息
 */
@Component
public class MessageConsumer {
    @PostConstruct
    public void init() {
        ThreadPoolUtil.run(new Task());
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            while (true) {
                while (!MessageRepo.MESSAGE_QUEUE.isEmpty()) {
                    VoicePlayer.play(MessageRepo.MESSAGE_QUEUE.poll());
                }
            }
        }
    }
}
