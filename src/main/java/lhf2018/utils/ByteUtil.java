package lhf2018.utils;

public class ByteUtil {
    /**
     * Hex转byte[]，两种情况，Hex长度为奇数最后一个字符会被舍去
     */
    public static byte[] hexToBytes(String hex) {
        byte[] result = new byte[hex.length() / 2];
        int j = 0;
        for(int i = 0; i < hex.length(); i+=2) {
            result[j++] = (byte)Integer.parseInt(hex.substring(i,i+2), 16);
        }
        return result;
    }
}
