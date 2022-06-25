package lhf2018.websocket;

import lhf2018.utils.ByteUtil;
import lhf2018.utils.MessageHandler;
import lhf2018.websocket.cons.WebSocketRequestCons;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Slf4j
public class MyWebSocketClient extends WebSocketClient {


    public MyWebSocketClient() {
        super(URI.create(WebSocketRequestCons.WEBSOCKET_REQUEST_URL));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("[MyWebSocketClient#onOpen]The WebSocket connection is open.  连接上了");
        //发送数据 数据包处理 https://blog.csdn.net/yyznm/article/details/116543107
        send(getCertification());
        log.info("success send");

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                send(ByteUtil.hexToBytes(WebSocketRequestCons.HEART_BEAT_BYTE));
                log.info("success heartBeat");
            }
        }, 0L, 30000L);

    }

    private byte[] getCertification() {
        byte[] body = WebSocketRequestCons.CERTIFICATION_REQUEST_JSON.getBytes(StandardCharsets.UTF_8);
        String headStr = WebSocketRequestCons.CERTIFICATION_REQUEST_HEAD_HEX.replace("{replce}", Integer.toHexString(body.length + 16));
        byte[] headBytes = ByteUtil.hexToBytes(headStr);

        byte[] result = new byte[body.length + headBytes.length];
        System.arraycopy(headBytes, 0, result, 0, headBytes.length);
        System.arraycopy(body, 0, result, headBytes.length, body.length);
        return result;
    }


    @Override
    public void send(byte[] data) {
        super.send(data);
    }

    @Override
    public void onMessage(String s) {
        log.info("[MyWebSocketClient#onMessage]The client has received the message from server." +
                "The Content is [" + s + "]");
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            //todo 接收消息的处理
            new MessageHandler().messageHandle(bytes);
//            new MessageHandler().getMessage(bytes);
//            log.info("onMessage, content={}", result);
        } catch (Exception ignored) {
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("[MyWebSocketClient#onClose]The WebSocket connection is close. 断开了");
    }

    @Override
    public void onError(Exception e) {
        log.info("[MyWebSocketClient#onError]The WebSocket connection is error.");
    }
}
