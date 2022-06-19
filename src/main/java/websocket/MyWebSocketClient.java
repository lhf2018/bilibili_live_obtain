package websocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class MyWebSocketClient extends WebSocketClient {


    private static final URI uri = URI.create("ws://broadcastlv.chat.bilibili.com:2244/sub");
    //需要向该api请求
    private static final String ROOM_API_URL = "https://api.live.bilibili.com/room/v1/Room/room_init?id=22834435";
    private static final String json = """
            {
                "uid": 0,
                "roomid": 22834435,
                "protover": 1,
                "platform": "web",
                "clientver": "1.5.10.1",
                "type":2
            }""";

    private static final String head = "000000{replce}001000010000000700000001";//每两个十六进制数为一个字节，格式见https://blog.csdn.net/xfgryujk/article/details/80306776

    public MyWebSocketClient() {
        super(uri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("[MyWebSocketClient#onOpen]The WebSocket connection is open.  连接上了");
        //发送数据 数据包处理 https://blog.csdn.net/yyznm/article/details/116543107
        send(getCertification());
        log.info("success send");
        //todo 心跳包
    }

    private byte[] getCertification() {
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        String headStr = head.replace("{replce}", Integer.toHexString(body.length + 16));
        byte[] headBytes = headStr.getBytes(StandardCharsets.UTF_8);

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
    public void onClose(int i, String s, boolean b) {
        log.info("[MyWebSocketClient#onClose]The WebSocket connection is close. 断开了");
    }

    @Override
    public void onError(Exception e) {
        log.info("[MyWebSocketClient#onError]The WebSocket connection is error.");
    }
}
