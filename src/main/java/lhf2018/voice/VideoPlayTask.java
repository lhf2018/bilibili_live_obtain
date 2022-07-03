package lhf2018.voice;

public class VideoPlayTask implements Runnable {
    private final String message;

    public VideoPlayTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        VoicePlayer.play(message);
    }
}
