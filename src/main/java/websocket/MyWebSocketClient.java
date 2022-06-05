package websocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Component
@Slf4j
public class MyWebSocketClient extends WebSocketClient {


    private static final URI uri = URI.create("ws://broadcastlv.chat.bilibili.com:2244/sub");
    private static final String ROOM_API_URL="https://api.live.bilibili.com/room/v1/Room/room_init?id=23197314";
    String json = """
            {
                "uid": 0,
                "roomid": 23197314
                "protover": 1,
                "platform": "web",
                "clientver": "1.4.0"
            }""";

    public MyWebSocketClient() {
        super(uri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("[MyWebSocketClient#onOpen]The WebSocket connection is open.  连接上了");
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(ROOM_API_URL);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();// 关闭远程连接
        }
        System.out.println(result);
        //发送数据
        send(getCertification(json));

    }

    private byte[] getCertification(String json) {
        return null;
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
    public void send(byte[] data) {
        super.send(data);
    }

    @Override
    public void onError(Exception e) {
        log.info("[MyWebSocketClient#onError]The WebSocket connection is error.");
    }
}
