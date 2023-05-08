import javax.swing.*;
public class GameFrame extends JFrame {
    //This constructor creates the window of the game, and makes the game process visible.
    public GameFrame() {
        ImageIcon imageIcon = new ImageIcon(Constants.ICON);
        this.setTitle("PAC-MAN");
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setResizable(false);
        this.setIconImage(imageIcon.getImage());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        Instructions instructions =new Instructions(this);
        this.add(instructions);
        this.setVisible(true);
    }
}
