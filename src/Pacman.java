import javax.swing.*;
import java.awt.*;
public class Pacman {
    //pacman current coordinates.
    private int pacmanX;
    private int pacmanY;
    //pacman previous coordinates.
    private int oldPacmanX;
    private int oldPacmanY;
    //pacman direction to maintain movement direction.
    private int pacmanXDirection;
    private int pacmanYDirection;
    //pacman animations.
    private Image up;
    private Image down;
    private Image left;
    private Image right;
    //pacman position on the map.
    private int position;
    //An indicator that pacman is on a block with dot.
    private boolean isDotPosition;
    private int currentLevel;

    //Pacman constructor.
    public Pacman() {
        loadImages();
    }

    //This method loads pacman animations.
    private void loadImages() {
        try {
            this.down = new ImageIcon(Constants.DOWN).getImage();
            this.up = new ImageIcon(Constants.UP).getImage();
            this.left = new ImageIcon(Constants.LEFT).getImage();
            this.right = new ImageIcon(Constants.RIGHT).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Getters of pacman x and y location.
    protected int getPacmanX() {
        return this.pacmanX;
    }

    protected int getPacmanY() {
        return this.pacmanY;
    }

    //Sets pacman start position, you can edit it by multiplying block size by other integer, while not getting out of
    //panel bounds. When the game stars the old and new position is the same.
    protected void setStartPosition(int currentLevel) {
        this.currentLevel = currentLevel;
        this.oldPacmanX = this.pacmanX = 7 * Constants.BLOCK_SIZE;
        this.oldPacmanY = this.pacmanY = 8 * Constants.BLOCK_SIZE;
    }

    //This method draws pacman animations on the panel, based on pacman direction.
    protected void drawPacman(Graphics g, int controlX, int controlY, GamePanel panel) {
        if (controlX == -1) {
            g.drawImage(this.left, this.pacmanX, this.pacmanY, panel);
        } else if (controlX == 1) {
            g.drawImage(this.right, this.pacmanX, this.pacmanY, panel);
        } else if (controlY == -1) {
            g.drawImage(this.up, this.pacmanX, this.pacmanY, panel);
        } else {
            g.drawImage(this.down, this.pacmanX, this.pacmanY, panel);
        }
    }

    //This method returns the value of pacman current position, as it defined in the level data array.
    protected synchronized int getPacmanPosition() {
        // If pacman position fits to the map layout.
        if (this.pacmanX % Constants.BLOCK_SIZE == 0 && this.pacmanY % Constants.BLOCK_SIZE == 0) {
            // Calculate Pacman current position on the map and returns it.
            this.position = this.pacmanX / Constants.BLOCK_SIZE + Constants.BLOCKS_AMOUNT * (this.pacmanY / Constants.BLOCK_SIZE);
            this.isDotPosition = true;
            return this.position;
        } else {
            //If pacman position not fits to the map layout, it will not be able to consume the dot because his position
            // will not be defined.
            this.isDotPosition = false;
            return -1;
        }
    }

    protected synchronized void movePacman(boolean isADot, int controlX, int controlY) {
        // If this a position with a dot.
        if (this.isDotPosition) {
            // Update the old position if Pac-Man is on a  dot.
            if (isADot) {
                this.oldPacmanX = this.pacmanX;
                this.oldPacmanY = this.pacmanY;
            }
            // If pacman is moving, then update his direction by users input.
            if (controlX != 0 || controlY != 0) {
                this.pacmanXDirection = controlX;
                this.pacmanYDirection = controlY;
            }
            // Check for walls in pacman current direction.
            boolean leftWall = isLeftWall();
            boolean rightWall = isRightWall();
            boolean upWall = isUpWall();
            boolean downWall = isDownWall();

            // If there is a wall in front of pacman, then it will stop.
            if (leftWall || rightWall || upWall || downWall) {
                this.pacmanXDirection = 0;
                this.pacmanYDirection = 0;
            } else if (this.oldPacmanX != this.pacmanX || this.oldPacmanY != this.pacmanY) {
                // Play step sound if pacman is still moving.
                Utils.setMusicFlag(Constants.MUSIC_STEP);
            }
        }
        // Update pacman position based on the current direction and speed.
        this.pacmanX += Constants.PACMAN_SPEED * this.pacmanXDirection;
        this.pacmanY += Constants.PACMAN_SPEED * this.pacmanYDirection;
    }
    //Return true if pacman is moving left and his position divisible by left border value.
    private boolean isLeftWall() {
        if (this.pacmanXDirection == -1 && this.pacmanYDirection == 0 && (Constants.PLAYING_FIELD[this.currentLevel][this.position] % Constants.LB) == 0) {
            return true;
        }
        return false;
    }

    //Return true if pacman is moving right and his position divisible by right border value.
    private boolean isRightWall() {
        if (this.pacmanXDirection == 1 && this.pacmanYDirection == 0 && (Constants.PLAYING_FIELD[this.currentLevel][this.position] % Constants.RB) == 0) {
            return true;
        }
        return false;
    }
    //Return true if pacman is moving up and his position divisible by top border value.
    private boolean isUpWall() {
        if (this.pacmanXDirection == 0 && this.pacmanYDirection == -1 && (Constants.PLAYING_FIELD[this.currentLevel][this.position] % Constants.TB) == 0) {
            return true;
        }
        return false;
    }

    //Return true if pacman is moving down and his position divisible by bottom border value.
    private boolean isDownWall() {
        if (this.pacmanXDirection == 0 && this.pacmanYDirection == 1 && (Constants.PLAYING_FIELD[this.currentLevel][this.position] % Constants.BB) == 0) {
            return true;
        }
        return false;
    }
}
