import java.awt.*;

public class Constants {
    //Paths to Pacman animations.
    public static final String DOWN = "images/down.gif";
    public static final String UP = "images/up.gif";
    public static final String LEFT = "images/left.gif";
    public static final String RIGHT = "images/right.gif";
    //Paths to other game components.
    public static final String GHOST = "images/ghost.gif";
    public static final String HEART = "images/heart.png";
    public static final String ICON = "images/icon.png";
    //Paths to game sounds.
    public static final String PATH_TO_EAT = "sounds/pacman_eatFruit.wav";
    public static final String PATH_TO_START = "sounds/pacman_beginning.wav";
    public static final String PATH_TO_DEATH = "sounds/pacman_death.wav";
    public static final String PATH_TO_STEP = "sounds/pacman_chomp.wav";
    //Font of in game text.
    public static final Font FONT = new Font("Georgia", Font.BOLD, 20);
    //Pixels size of one game block.
    public static final int BLOCK_SIZE = 24;
    //Amount of blocks in each row/column.
    public static final int BLOCKS_AMOUNT = 15;
    //Sizes of the window.
    public static final int WINDOW_WIDTH = 380;
    public static final int WINDOW_HEIGHT = 420;
    public static final int SPACING = 10;
    public static final int HEART_SIZE = 22;

    //Size of the game board.
    public static final int SCREEN_SIZE = BLOCKS_AMOUNT * BLOCK_SIZE;
    //Speed of pacman.
    public static final int PACMAN_SPEED = 1;
    //Number of ghosts in the game.
    public static final int GHOSTS_NUMBER = 3;
    //3 represents the left border.
    public static final int LB = 3;
    //5 represents the top border.
    public static final int TB = 5;
    //7 represents the bottom border.
    public static final int BB = 7;
    //11 represents the right border.
    public static final int RB = 11;
    //This is a constant value that represents each value of screen data array, when the level starts.
    //I initialize every value of the map to be with a dot because the borders already prevents pacman to access
    //to the restricted zones.
    public static final boolean EATABLE_DOT = true;
    //false represents an empty space (a space after pacman eats the dot from it).
    public static final boolean EMPTY_SPACE = false;
    //This array represent the whole game map, while every index of the array represent a combination of two borders or
    //single border or an obstacle or a potential dot position.
    //I used border values also used for the obstacles.
    // Two is used to represent a potential dot position.
    // Zero is used for obstacle representation.
    public static final int[][] PLAYING_FIELD = {
            {
                    TB * LB, TB, TB, TB * BB, TB, TB, TB, TB, TB, TB, TB, TB * BB, TB, TB, TB * RB,
                    LB, 2, RB, 0, LB, 2, 2, 2, 2, 2, RB, 0, LB, 2, RB,
                    LB, BB, BB * RB, 0, LB, 2, 2, 2, 2, 2, RB, 0, LB * BB, BB, RB,
                    LB * RB, 0, 0, 0, LB, BB, BB, BB, BB, BB, RB, 0, 0, 0, RB * LB,
                    LB, TB, TB, TB, RB, 0, 0, 0, 0, 0, LB, TB, TB, TB, RB,
                    LB, BB, BB, BB, 2, TB, TB, TB, TB, TB, 2, BB, BB, BB, RB,
                    LB * RB, 0, 0, 0, LB, 2, 2, 2, 2, 2, RB, 0, 0, 0, RB * LB,
                    LB * RB, 0, 0, 0, LB, 2, 2, 2, 2, 2, RB, 0, 0, 0, RB * LB,
                    LB, TB, TB, TB, 2, BB, BB, BB, BB, BB, 2, TB, TB, TB, RB,
                    LB, 2, 2, 2, RB, 0, 0, 0, 0, 0, LB, 2, 2, 2, RB,
                    LB, BB, BB, BB, 2, TB, TB, TB, TB, TB, 2, BB, BB, BB, RB,
                    LB * RB, 0, 0, 0, LB, 2, 2, 2, 2, 2, RB, 0, 0, 0, RB * LB,
                    LB, TB, TB * RB, 0, LB, 2, 2, 2, 2, 2, RB, 0, TB * LB, TB, RB,
                    LB, 2, RB, 0, LB, 2, 2, 2, 2, 2, RB, 0, LB, 2, RB,
                    LB * BB, BB, BB, BB * TB, BB, BB, BB, BB, BB, BB, BB, BB * TB, BB, BB, RB * BB},
            {
                    TB * LB, TB, TB, TB, TB, TB, TB, TB, TB, TB, TB, TB, TB, TB, TB * RB,
                    LB, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, RB,
                    LB, 2, 2, BB, BB, 2, 2, 2, 2, 2, BB, BB, 2, 2, RB,
                    LB, 2, RB, 0, 0, LB, 2, 2, 2, RB, 0, 0, LB, 2, RB,
                    LB, 2, RB, 0, 0, LB, 2, 2, 2, RB, 0, 0, LB, 2, RB,
                    LB, 2, 2, TB, TB, 2, 2, 2, 2, 2, TB, TB, 2, 2, RB,
                    LB, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, RB,
                    LB, 2, BB, 2, 2, 2, 2, 2, 2, 2, 2, 2, BB, 2, RB,
                    LB, RB, 0, LB, 2, 2, 2, 2, 2, 2, 2, RB, 0, LB, RB,
                    LB, RB, 0, LB * BB, BB, BB, BB, BB, BB, BB, BB, BB * RB, 0, LB, RB,
                    LB, RB, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, LB, RB,
                    LB, 2, TB, TB, TB, TB, TB, TB, TB, TB, TB, TB, TB, 2, RB,
                    LB, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, RB,
                    LB, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, RB,
                    LB * BB, BB, BB, BB, BB, BB, BB, BB, BB, BB, BB, BB, BB, BB, RB * BB}
    };
    //Color of the obstacles.
    public static final Color[] COLOR_1 = {Color.PINK, Color.ORANGE};
    //Color of the dots in each level.
    public static final Color[] COLOR_2 = {Color.GREEN, Color.WHITE};
    //Color of the panel.
    public static final Color[] COLOR_RECTANGLE = {Color.GRAY, Color.RED};
    //An indicator for a level.
    public static final int LEVEL_1 = 0;
    //An array that represent the amount of dots in each level.
    public static final int[] MAX_LEVEL_SCORE ={183,202};
    //Possible directions for the ghosts(left,up,right,down).
    public static final int[][] DIRECTIONS = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
    //This array stores all the border values.
    public static final int[] WALLS = {LB, TB, RB, BB};
    //Those integers are used to manage the game sounds.
    public static final int MUSIC_START = 0;
    public static final int MUSIC_EAT = 1;
    public static final int MUSIC_DEATH = 2;
    public static final int MUSIC_STEP = 3;
    public static final int MUSIC_STOP = 4;
}
