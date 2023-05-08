import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Ghost {
    //Starting x and y positions of all the ghosts
    private int[] xStart;
    private int[] yStart;
    //Current x and y directions of ghosts,
    //For example if the xDirection value is 1,
    //and yDirection value is 0, ghost moves to the right.
    private int[] xDirection;
    private int[] yDirection;
    //Those arrays store the current x and y coordinates of every ghost.
    private int[] possibleXDirections;
    private int[] possibleYDirections;
    //Ghost animation.
    private Image ghost;
    //An indicator to collusion between some ghost and pacman.
    private boolean collusion;
    //Indicates the current level.
    private int currentLevel;

    //Ghost constructor.
    public Ghost() {
        this.loadImage();
        this.initVariables();
    }

    //This method initializes arrays length according to the number of ghosts in the game.
    private void initVariables() {
        this.xStart = new int[Constants.GHOSTS_NUMBER];
        this.yStart = new int[Constants.GHOSTS_NUMBER];
        this.xDirection = new int[Constants.GHOSTS_NUMBER];
        this.yDirection = new int[Constants.GHOSTS_NUMBER];
        //up , down , left or right.
        this.possibleXDirections = new int[4];
        this.possibleYDirections = new int[4];
    }

    //Load ghost animation.
    private void loadImage() {
        try {
            this.ghost = new ImageIcon(Constants.GHOST).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get collusion status.
    protected boolean getCollisionStatus() {
        return this.collusion;
    }

    //Set collusion status.
    public void setCollisionStatus(boolean status) {
        this.collusion = status;
    }

    //This method sets the start position of the ghosts.
    protected void setStartPositions(int currentLevel) {
        this.currentLevel = currentLevel;
        for (int i = 0; i < Constants.GHOSTS_NUMBER; i++) {
            //start position of ghosts.
            //You can change it by multiplying this values by an integer
            // , while the x and y values are not out of panel bounds.
            this.yStart[i] = Constants.BLOCK_SIZE;
            this.xStart[i] = Constants.BLOCK_SIZE;
            this.yDirection[i] = 1;
            this.xDirection[i] = 1;
        }
    }

    //This method manages the ghosts movements.
    protected void moveGhosts(Graphics g, GamePanel frame, int pacmanX, int pacmanY) {
        Random random = new Random();

        // For every ghost in the game.
        for (int i = 0; i < Constants.GHOSTS_NUMBER; i++) {
            // If ghosts coordinates fits the map.
            if (this.xStart[i] % Constants.BLOCK_SIZE == 0 && this.yStart[i] % Constants.BLOCK_SIZE == 0) {
                //Ghost position calculated and updated
                int position = calculateGhostPosition(i);
                updateGhostDirection(i, position, random);
            }
            moveGhost(i);
            drawGhost(g, frame, i);
            checkCollisionWithPacman(i, pacmanX, pacmanY);
        }
    }

    //This method calculates ghost position.
    private int calculateGhostPosition(int ghostIndex) {
        int position = this.xStart[ghostIndex] / Constants.BLOCK_SIZE + Constants.BLOCKS_AMOUNT * (this.yStart[ghostIndex] / Constants.BLOCK_SIZE);
        return position;
    }

    //This method updates ghost position.
    private void updateGhostDirection(int ghostIndex, int position, Random random) {
        int count = 0;
        //Checks if the move direction is available (up, down , right or left).
        for (int direction = 0; direction < 4; direction++) {
            if (isDirectionAvailable(position, ghostIndex, direction)) {
                this.possibleXDirections[count] = Constants.DIRECTIONS[direction][0];
                this.possibleYDirections[count] = Constants.DIRECTIONS[direction][1];
                count++;
            }
        }
        //If there is at least one available direction or more, choose randomly a direction from the available.
        if (count != 0) {
            int chosenDirection = random.nextInt(count);
            this.xDirection[ghostIndex] = this.possibleXDirections[chosenDirection];
            this.yDirection[ghostIndex] = this.possibleYDirections[chosenDirection];
        }
    }

    //This method returns indicator to direction availability.
    private boolean isDirectionAvailable(int position, int ghostIndex, int directionIndex) {
        //The x value of direction(0,1 or-1).
        int dx = Constants.DIRECTIONS[directionIndex][0];
        //The y value of direction(0,1 or-1).
        int dy = Constants.DIRECTIONS[directionIndex][1];
        //wall represent the indicator to the case if the ghost collides with one of the borders.
        int wall = Constants.PLAYING_FIELD[this.currentLevel][position] % Constants.WALLS[directionIndex];
        //If the ghost position not collides with some border position,
        // and the ghosts are not choosing to go to the opposite direction,
        //then the method will return true. If I did not check the opposite direction ,
        //the ghosts will go from left to right or from up to down.
        if (wall != 0 && !(this.xDirection[ghostIndex] == -dx && this.yDirection[ghostIndex] == -dy)) {
            return true;
        }
        return false;
    }

    //This method makes the ghosts move, by updating their position.
    //(Adding -1,0 or 1 to both x and y values).
    private void moveGhost(int ghostIndex) {
        this.xStart[ghostIndex] += this.xDirection[ghostIndex];
        this.yStart[ghostIndex] += this.yDirection[ghostIndex];
    }

    //This method draws ghosts on the screen.
    private void drawGhost(Graphics g, GamePanel frame, int ghostIndex) {
        g.drawImage(this.ghost, this.xStart[ghostIndex] + 1, this.yStart[ghostIndex] + 1, frame);
    }

    //This method checks for pacman and one of the ghost collision.
    private void checkCollisionWithPacman(int ghostIndex, int pacmanX, int pacmanY) {
        //If pacman x position is bigger than ghost left corner x position and also pacman x position is smaller than ghost right corner,
        //(the same check to y position) ,then sets collusion value to true.
        if (pacmanX > (this.xStart[ghostIndex] - Constants.BLOCK_SIZE / 2) && pacmanX < (this.xStart[ghostIndex] + Constants.BLOCK_SIZE / 2)
                && pacmanY > (this.yStart[ghostIndex] - Constants.BLOCK_SIZE / 2) && pacmanY < (this.yStart[ghostIndex] + Constants.BLOCK_SIZE / 2)) {
            this.collusion = true;
        }
    }
}
