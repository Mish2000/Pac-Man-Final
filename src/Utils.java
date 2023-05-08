//This class manages the musicFlag to set the game sound for each action.
//The music flag variable and his getters and setters are static because I use it in different classes without creating
//an object.
public class Utils {
    private static int musicFlag ;
    //This boolean sets to true only when setMusicFlag method is called to change the sound effect,
    //otherwise it turns to false in the getMusicFlag method.
    private static boolean musicFlagAvailable ;
    //This field is used mainly for the wait() and notify() methods, which used to manage
    //thread state.
    private static final Object lock = new Object();
    //This method returns the music flag, but only when new music flag becomes available.
    //Otherwise, lock object will make the thread wait until status changes.This prevents the thread,
    // from returning the same value in an infinity loop, and significantly decreases CPU usage.
    public static int getMusicFlag() {
        synchronized (lock) {
            while (!musicFlagAvailable) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            musicFlagAvailable = false;
            return musicFlag;
        }
    }

    //This method sets the value of the music flag field according to the constant that this method
    //takes as a parameter.Also, it sets music flag status to true to make a new sound effect.
    //Also, the lock.notify() call is to "wake up" the waiting thread that called the getMusicFlag method.
    //The thread that was waiting will then complete its life cycle.
    public static void setMusicFlag(int inMusicFlag) {
        synchronized (lock) {
            musicFlag = inMusicFlag;
            musicFlagAvailable = true;
            lock.notify();
        }
    }
}

