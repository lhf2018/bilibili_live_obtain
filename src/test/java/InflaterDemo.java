import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class InflaterDemo {
    public static void main(String[] args)
            throws DataFormatException, UnsupportedEncodingException {
        String message = "Welcome to Yiibai.com;"
                +"Welcome to Yiibai.com;"
                +"Welcome to Yiibai.com;"
                +"Welcome to Yiibai.com;"
                +"Welcome to Yiibai.com;"
                +"Welcome to Yiibai.com;"
                +"Welcome to Yiibai.com;"
                +"Welcome to Yiibai.com;"
                +"Welcome to Yiibai.com;"
                +"Welcome to Yiibai.com;";
        System.out.println("Original Message length : " + message.length());
        byte[] input = message.getBytes(StandardCharsets.UTF_8);
        System.out.println(input.length);

        // Compress the bytes
        byte[] output = new byte[1024];
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();
        int compressedDataLength = deflater.deflate(output);
        deflater.end();

        System.out.println("Compressed Message length : " + compressedDataLength);

        // Decompress the bytes
        Inflater inflater = new Inflater();
        inflater.setInput(output, 0, compressedDataLength);
        byte[] result = new byte[1024];
        int resultLength = inflater.inflate(result);
        inflater.end();

        // Decode the bytes into a String
        message = new String(result, 0, resultLength, "UTF-8");

        System.out.println("UnCompressed Message length : " + message.length());
    }
}


