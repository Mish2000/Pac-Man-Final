import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {
    //Represents the width and height of this panel.
    private final Dimension dimension;
    //Game running status.
    private boolean inGame;
    //pacman alive status.
    private boolean dead;
    //pacman lives and score.
    private int lives;
    private int score;
    //Heart image.
    private Image heart;
    //Controls for key events.
    private int controlX;
    private int controlY;
    //An array that stores all dot positions on the map. At start of each level,it fills the map with true values,
    //that represent a position with eatable dot, and when pacman passes over this position, the value of the index
    //becomes false.
    public boolean[] dotInitializer;
    //Graphics object that draws all components on the panel.
    private Graphics graphics;
    //An object that make the graphic elements in the game to work more smoothly without lagging.
    private BufferedImage image;
    //A thread that maintains the game loop.
    private Thread thread;
    //A Music object that extends another tread and maintains the sounds of the game.
    private Music musicThread;
    //A Pacman class object.
    private final Pacman pacman;
    //A Ghost class object.
    private final Ghost ghost;
    //Indicator for a level number.
    private int currentLevel;
    //Level 1 completion status.
    private boolean wonLevel1;
    //Game finishing status.
    private boolean wonGame;

    //This is The constructor of the game panel.
    public GamePanel() {
        this.dotInitializer = new boolean[Constants.BLOCKS_AMOUNT * Constants.BLOCKS_AMOUNT];
        this.dimension = new Dimension(Constants.SCREEN_SIZE, Constants.SCREEN_SIZE);
        this.pacman = new Pacman();
        this.loadHeart();
        this.addKeyListener(this.controls);
        this.setFocusable(true);
        this.ghost = new Ghost();
        this.initFirstLevel();
        this.loadSounds();
    }
    public boolean isInGame()
    {
        return this.inGame;
    }

    //This method loads all the necessary sounds for the game.
    private void loadSounds() {
        String[] sounds = {Constants.PATH_TO_START, Constants.PATH_TO_EAT, Constants.PATH_TO_DEATH, Constants.PATH_TO_STEP};
        try {
            Utils.setMusicFlag(Constants.MUSIC_STOP);
            //Initializes all game sounds.
            this.musicThread = new Music(sounds);
            this.musicThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method loads the heart image.
    private void loadHeart() {
        try {
            this.heart = new ImageIcon(Constants.HEART).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method copies all the level map values to other array.
    private void initFirstLevel() {
        this.currentLevel = Constants.LEVEL_1;
        this.initLevel();
        this.score = 0;
    }

    //This method resets the positions of pacman and ghosts, and sets pacman alive and collision status to false.
    private void resetPositions() {
        this.ghost.setStartPositions(this.currentLevel);
        this.pacman.setStartPosition(this.currentLevel);
        this.dead = false;
        this.ghost.setCollisionStatus(false);
    }

    //This method initializes level, while filling the screen data array with boolean true values,
    //to make the whole map to be initialized with dots.
    private void initLevel() {
        this.lives = 4;
        for (int i = 0; i < Constants.BLOCKS_AMOUNT * Constants.BLOCKS_AMOUNT; i++) {
            this.dotInitializer[i] = Constants.EATABLE_DOT;
        }
        resetPositions();
        this.wonLevel1 = false;
        this.wonGame = false;
    }

    //This method initializes the game.
    private void startGame() {
        //Width and height of the panel.
        int width = this.getWidth();
        int height = this.getHeight();
        //Creates a buffered image object, which allows smoother graphics updates (it mainly used for the gif animations).
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //The graphics object will draw all the game elements on the buffered image.
        this.graphics = this.image.createGraphics();
        //Initializes the game thread.
        this.thread = new Thread(this);
        this.thread.start();
        //Game start sound effect.
        Utils.setMusicFlag(Constants.MUSIC_START);
        //Indicator that turns true when all the dots cleared.
        this.wonGame = false;
        if (currentLevel == Constants.LEVEL_1) {
            this.score = 0;
        }
    }

    //This method manages the game events.
    private void playGame(Graphics g) {
        //If game is active.
        if (this.inGame) {
            //If pacman is killed.
            if (this.dead) {
                death();
            } else {
                //If pacman is alive.
                managePacman();
                this.pacman.drawPacman(g, this.controlX, this.controlY, this);
                moveGhosts(g);
                winner();
            }
            //If the game is not running.
        } else {
            showStartScreen(g);
        }
    }

    //This method mainly responsible for handling interactions between pacman and the dots.
    private void managePacman() {
        int pacmanPosition = this.pacman.getPacmanPosition();
        boolean isADot = false;
        //If pacman position is valid.
        if (pacmanPosition >= 0) {
            //If pacman did not eat there already, it equals true.
            boolean dotValue = this.dotInitializer[pacmanPosition];
            //If the value in pacman position equals true, the value turns to false, and pacman eats it.
            if (dotValue == Constants.EATABLE_DOT) {
                this.dotInitializer[pacmanPosition] = Constants.EMPTY_SPACE;
                this.score++;
                Utils.setMusicFlag(Constants.MUSIC_EAT);
                //An indicator that indicates that pacman eats a dot.
                isADot = true;
            }
        }
        //Making pacman keep moving.
        this.pacman.movePacman(isADot, controlX, controlY);
    }

    //This method shows you the start game option.
    private void showStartScreen(Graphics g) {
        String start = "Press SPACE to start";
        g.setColor(Color.BLACK);
        g.drawString(start, (Constants.SCREEN_SIZE) / 4, Constants.SCREEN_SIZE / 2);
    }

    //This method manages all drawing methods and handles the cases of some components to be drawn.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Background color setting.
        g.setColor(Constants.COLOR_RECTANGLE[this.currentLevel]);
        g.fillRect(0, 0, this.dimension.width, this.dimension.height);
        drawMap(g);
        drawScoreAndLives(g);
        if (this.inGame) {
            playGame(g);
        } else {
            showStartScreen(g);
        }
    }

    // This method draws the maze on the panel.
    private void drawMap(Graphics g) {
        int position = 0;
        // This loop draws all map components.
        // This loop run on all y values of the blocks.
        for (int y = 0; y < Constants.SCREEN_SIZE; y += Constants.BLOCK_SIZE) {
            //This loop run on all x values of the blocks.
            for (int x = 1; x < Constants.SCREEN_SIZE; x += Constants.BLOCK_SIZE, position++) {
                // Sets color for obstacles.
                g.setColor(Constants.COLOR_1[this.currentLevel]);

                //If the map value is 0, there will be drawn an obstacle.
                if ((Constants.PLAYING_FIELD[this.currentLevel][position] == 0)) {
                    g.fillRect(x, y, Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
                } else {
                    //Blue color is to mark the territory where pacman and ghosts cannot access.
                    g.setColor(Color.BLUE);
                    //If the map position value dividable by LB value, draws line that represents the left border.
                    if ((Constants.PLAYING_FIELD[this.currentLevel][position] % Constants.LB) == 0) {
                        g.drawLine(x, y, x, y + Constants.BLOCK_SIZE);
                    }
                    //If the map position value dividable by TB value, draws line that represents the top border.
                    if ((Constants.PLAYING_FIELD[this.currentLevel][position] % Constants.TB) == 0) {
                        g.drawLine(x, y, x + Constants.BLOCK_SIZE, y);
                    }
                    //The 'magic numbers' here used to make the borders lines visible.
                    //If the map position value dividable by RB value, draws line that represents the right border.
                    if ((Constants.PLAYING_FIELD[this.currentLevel][position] % Constants.RB) == 0) {
                        g.drawLine(x + Constants.BLOCK_SIZE - 1, y, x + Constants.BLOCK_SIZE - 1, y + Constants.BLOCK_SIZE);
                    }
                    //If the map position value dividable by BB value, draws line that represents the bottom border.
                    if ((Constants.PLAYING_FIELD[this.currentLevel][position] % Constants.BB) == 0) {
                        g.drawLine(x, y + Constants.BLOCK_SIZE - 1, x + Constants.BLOCK_SIZE, y + Constants.BLOCK_SIZE - 1);
                    }
                    // Draws a dot if the dotInitializer array position value equals to constant value.
                    if (this.dotInitializer[position] == Constants.EATABLE_DOT) {
                        g.setColor(Constants.COLOR_2[this.currentLevel]);
                        g.fillOval(x + Constants.SPACING, y + Constants.SPACING, Constants.SPACING, Constants.SPACING);
                    }
                }
            }
        }
    }

    //This method manages the ghosts move, and sets dying to true if there is a collision.
    private void moveGhosts(Graphics g) {
        int pacmanX = this.pacman.getPacmanX();
        int pacmanY = this.pacman.getPacmanY();
        this.ghost.moveGhosts(g, this, pacmanX, pacmanY);
        this.dead = this.ghost.getCollisionStatus();
    }

    //This method checks if you collected all dots, if yes, you have an option to proceed to the next level.
    //If you completed the game, you can exit or restart the game.
    private void winner() {
        String string;
        int dialogOut;
        new JOptionPane();
        //If you collected all dots in the first level.
        if (!this.wonLevel1 && this.score == Constants.MAX_LEVEL_SCORE[Constants.LEVEL_1] && this.currentLevel == Constants.LEVEL_1) {
            this.inGame = false;
            this.wonLevel1 = true;
            string = "Level completed!";
            dialogOut = JOptionPane.showConfirmDialog(this, "Want to proceed to the final level?", string, JOptionPane.YES_NO_OPTION);
            //If yes clicked, going to the next level.
            if (dialogOut == JOptionPane.YES_OPTION) {
                this.currentLevel = Constants.LEVEL_1 + 1;
                this.score = Constants.MAX_LEVEL_SCORE[Constants.LEVEL_1];
                this.initLevel();
            } else
                //If no clicked, application stops.
                System.exit(0);
        } else {
            //Total score is the sum of the dots of both levels.
            int totalScore = Constants.MAX_LEVEL_SCORE[0] + Constants.MAX_LEVEL_SCORE[1];
            //If you collected all dots in the last level.
            if (!this.wonGame && this.score == totalScore) {
                this.inGame = false;
                this.wonGame = true;
                string = "Game completed ! Hope you enjoyed :) ";
                dialogOut = JOptionPane.showConfirmDialog(this, "Want to play again?", string, JOptionPane.YES_NO_OPTION);
                if (dialogOut == JOptionPane.YES_OPTION) {
                    this.initFirstLevel();
                } else
                    System.exit(0);
            }
        }
    }

    //This method performed when pacman dies, it decreases lives and makes a death sound.
    //In addition, if the lives over, you have the option to restart the game or exit.
    private void death() {
        this.lives--;
        Utils.setMusicFlag(Constants.MUSIC_DEATH);
        if (this.lives == 0) {
            this.inGame = false;
            new JOptionPane();
            int dialogOut = JOptionPane.showConfirmDialog(this, "Want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
            if (dialogOut == JOptionPane.YES_OPTION)
                this.initFirstLevel();
            else
                System.exit(0);
        } else
            this.resetPositions();
    }

    //This is the controller of pacman move directions.
    private final KeyAdapter controls = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT -> {
                    if (inGame) {
                        //Left
                        controlX = -1;
                        controlY = 0;
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (inGame) {
                        //Right
                        controlX = 1;
                        controlY = 0;
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (inGame) {
                        //Up
                        controlX = 0;
                        controlY = -1;
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (inGame) {
                        //Down
                        controlX = 0;
                        controlY = 1;
                    }
                }
                case KeyEvent.VK_ESCAPE -> {
                    if (inGame) {
                        //Restart game.
                        inGame = false;
                        initFirstLevel();
                    }
                }
                case KeyEvent.VK_SPACE -> {
                    if (!inGame) {
                        //Start game.
                        inGame = true;
                        startGame();
                        initLevel();
                    }
                }
            }
        }
    };

    //This method draws the score and the lives of pacman.
    private void drawScoreAndLives(Graphics g) {
        g.setFont(Constants.FONT);
        g.setColor(Color.RED);
        String s = "Score: " + this.score;
        //-120 and 16 ints used to draw the score in optimal x and y position.
        g.drawString(s, Constants.SCREEN_SIZE - 120, Constants.SCREEN_SIZE + 16);
        for (int i = 0; i < this.lives; i++) {
            g.drawImage(this.heart, i * Constants.HEART_SIZE + Constants.SPACING, Constants.SCREEN_SIZE, this);
        }
    }

    //This is the game loop that refreshes the game every 15 milliseconds (around 67 fps).
    @Override
    public void run() {
        while (this.inGame) {
            if (this.dead) {
                death();
            } else {
                managePacman();
                moveGhosts(this.graphics);
            }
            winner();
            repaint();
            try {
                //67 FPS
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



