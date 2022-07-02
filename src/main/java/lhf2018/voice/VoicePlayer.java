package lhf2018.voice;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 语音播报
 *
 * @author Administrator
 */
public class VoicePlayer {
    public static void play(String content) {

        ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
        Dispatch sapo = sap.getObject();
        try {
            //音量
            sap.setProperty("Volume", new Variant(100));
            //语速
            sap.setProperty("Rate", new Variant(2));
            Dispatch.call(sapo, "Speak", new Variant(content));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sapo.safeRelease();
            sap.safeRelease();
        }
    }

}
