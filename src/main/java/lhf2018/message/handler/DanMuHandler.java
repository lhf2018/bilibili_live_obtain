package lhf2018.message.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lhf2018.message.MessageRepo;
import lhf2018.model.DanMu;

public class DanMuHandler implements Handler {
    private static final String MIDDLE_MSG = " 发送了弹幕: ";
    private static final String PREFIX_MSG = "用户: ";

    @Override
    public void handler(String msg) {
        DanMu danMu = JSON.parseObject(msg, new TypeReference<DanMu>() {});
        String content = danMu.getInfo().get(1);
        String userNick = danMu.getInfo().get(2).split("\"")[1];
        MessageRepo.MESSAGE_QUEUE.offer(PREFIX_MSG + userNick + MIDDLE_MSG + content);
        System.out.println(PREFIX_MSG + userNick + MIDDLE_MSG + content);
    }
}
