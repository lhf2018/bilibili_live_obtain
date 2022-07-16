package lhf2018.message.receiver;

import lhf2018.cons.WebSocketRequestCons;
import lhf2018.message.handler.MessageHandler;
import lhf2018.utils.ByteUtil;
import lhf2018.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 接受websocket消息
 */
@Component
@Slf4j
public class ReceiverClient extends WebSocketClient {


    public ReceiverClient() {
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
        byte[] body = getCertificationRequestJson().getBytes(StandardCharsets.UTF_8);
        String headStr = WebSocketRequestCons.CERTIFICATION_REQUEST_HEAD_HEX.replace("{replce}", Integer.toHexString(body.length + 16));
        byte[] headBytes = ByteUtil.hexToBytes(headStr);

        byte[] result = new byte[body.length + headBytes.length];
        System.arraycopy(headBytes, 0, result, 0, headBytes.length);
        System.arraycopy(body, 0, result, headBytes.length, body.length);
        return result;
    }

    /**
     * 向api请求实际的roomid，有些直播间的url是shortid
     */
    private String getCertificationRequestJson() {
        String resp = HttpUtils.sendRequest(WebSocketRequestCons.ROOM_API_URL);
        String roomId = resp.substring(resp.indexOf("room_id\":") + 9, resp.indexOf(",", resp.indexOf("room_id\":")));
        return String.format(WebSocketRequestCons.CERTIFICATION_REQUEST_JSON, roomId);
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
