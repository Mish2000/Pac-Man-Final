import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music extends Thread {
    //An array that stores all sound game effects.
    private final Clip[] musicClip;
    private GamePanel gamePanel;
    //This constructor initializes musicClip array with the length of the sounds array (F.E in GamePanel class),
    //and makes  a clip object in each music clip index.
    public Music(String[] sounds) {
        this.musicClip = new Clip[sounds.length];
        for (int i = 0; i < sounds.length; i++) {
            this.musicClip[i] = makeClip(sounds[i]);
        }
    }
    //This method is a loop for the Music object which extends thread.
    //It runs parallel to the game and checks for calls for sound effects.
    public void run() {
        while (true) {
            int musicFlag = Utils.getMusicFlag();
            if (musicFlag == Constants.MUSIC_STOP) {
                continue;
            }
            try {
                this.musicClip[musicFlag].setFramePosition(0);
                this.musicClip[musicFlag].start();
                Utils.setMusicFlag(Constants.MUSIC_STOP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    //This method returns a clip object which is a sound effect.
    private Clip makeClip(String path) {
        Clip clip;
        try {
            //Creates an object from audio file.
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
