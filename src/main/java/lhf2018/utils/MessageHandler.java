package lhf2018.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@Slf4j
public class MessageHandler {
    private static final String DANMU = "DANMU_MSG";
    private static final String GIFT = "SEND_GIFT";


    public void messageHandle(ByteBuffer byteBuffer) throws DataFormatException, UnsupportedEncodingException {
        List<String> s = messageToJson(byteBuffer);
        for (String message : s) {
            if (message.contains(DANMU)) {
                log.info("danmu_message={}", message);
            } else if (message.contains(GIFT)) {
                log.info("send_gift_message={}", message);
            }
        }
    }

    /**
     * @param message 如果是 message 是弹幕类型，则需要解压拆分
     * @return List<message>
     * @throws DataFormatException DataFormatException
     */
    private List<String> messageToJson(ByteBuffer message) throws DataFormatException {
        byte[] messageBytes = message.array();
        byte[] mainMessageBytes = Arrays.copyOfRange(messageBytes, 16, messageBytes.length);

        // 解压缩弹幕信息 todo 需要明白为什么是这几个代表弹幕
        byte[] newByte = new byte[1024 * 5];
        Inflater inflater = new Inflater(false);
        //http://itmyhome.com/java-api/java/util/zip/Inflater.html,解压后会有字段头
        inflater.setInput(mainMessageBytes);
        newByte = Arrays.copyOfRange(newByte, 16, inflater.inflate(newByte));
        return splitStringToJson(new String(newByte, StandardCharsets.UTF_8));
    }


    /**
     * @param str 包含多条 message 的字符串
     * @return List<message>
     */
    private static List<String> splitStringToJson(String str) {
        List<String> result = new ArrayList<>();
        for (int i = 1, count = 1; i < str.length(); i++) {

            if (str.charAt(i) == '{') {
                count++;
            } else if (str.charAt(i) == '}') {
                count--;
            }

            if (count == 0) {
                result.add(str.substring(0, i + 1));
                int nextIndex = str.indexOf("{", i + 1);
                if (nextIndex != -1) {
                    result.addAll(splitStringToJson(str.substring(nextIndex)));
                }
                return result;
            }
        }
        return result;
    }
}
