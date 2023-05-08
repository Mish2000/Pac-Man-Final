import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Instructions extends JPanel implements ActionListener {
    //Click this button to proceed to the game panel.
    private final JButton startGameButton;
    //Click this button to exit the application.
    private final JButton exitButton;
    //Instructions components.
    private final JLabel label;
    private final JTextArea instructionsText;
    private final GameFrame gameFrame;

    //This constructor creates the instructions panel.
    public Instructions(GameFrame frame) {
        this.gameFrame = frame;
        this.setLayout(new BorderLayout());
        this.label = new JLabel("Welcome to Pacman game!");
        this.label.setFont(new Font("Arial", Font.BOLD, 16));
        this.startGameButton = new JButton("Start Game");
        this.exitButton = new JButton("Exit");
        this.startGameButton.addActionListener(this);
        this.exitButton.addActionListener(this);
        this.instructionsText = new JTextArea("Game instructions:" +
                "\n\n" + "1. Move pacman character using the arrow keys." + "\n\n" +
                "2. Avoid making contact with ghosts. " +
                "\n\n3. Your main goal is to eat all the dots on the map without losing all lives. " +
                "\n\n4. Press escape to restart the game while playing at any level. " +
                "\n\n5. Have fun!");
        this.instructionsText.setFont(new Font("Arial", Font.BOLD, 15));
        this.instructionsText.setEditable(false);
        //Prevents the sentence from getting out of textField bounds.
        this.instructionsText.setLineWrap(true);
        //Prevents the words to diverse from line to line.
        this.instructionsText.setWrapStyleWord(true);
        //Creates decorative border to surround the textField.
        this.instructionsText.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.BLACK)));
        JPanel buttonPanel = new JPanel();
        //Adding all components to the panel.
        buttonPanel.add(this.startGameButton);
        buttonPanel.add(this.exitButton);
        this.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        this.add(this.label, BorderLayout.NORTH);
        this.add(this.instructionsText, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    //Manages buttons actions.
    public void actionPerformed(ActionEvent e) {
        // Switch to game panel if startGameButton  is clicked
        if (e.getSource() == this.startGameButton) {
            GamePanel gamePanel = new GamePanel();
            //Removes Instructions panel.
            this.gameFrame.getContentPane().removeAll();
            //Adds GamePanel to the frame and sets it up.
            this.gameFrame.add(gamePanel, BorderLayout.CENTER);
            this.gameFrame.revalidate();
            this.gameFrame.repaint();
            // Making the game panel available to user.
            gamePanel.requestFocusInWindow();
        } else if (e.getSource() == this.exitButton) {
            // Exit the application if exitButton is clicked
            System.exit(0);
        }
    }
}
