import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlackJack extends JFrame {

    private JPanel mainPanel;
    private JButton _startButton;
    private JButton _exitButton;
    private JLabel _mainTitle;

    public BlackJack(){
        setTitle("BlackJack Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#006442"));
        
        // 1. Switch layout to GridBagLayout (this handles custom rows/columns)
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Set spacing between the elements (top, left, bottom, right)
        gbc.insets = new Insets(20, 0, 20, 0); 
        gbc.fill = GridBagConstraints.NONE;

        // --- ROW 0: Title ---
        _mainTitle = new JLabel("BlackJack ");
        _mainTitle.setFont(new Font("Arial", Font.BOLD, 70)); // Made it bigger for full screen
        _mainTitle.setForeground(Color.WHITE); // Make it pop against the green
        
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        mainPanel.add(_mainTitle, gbc);

        
        // --- ROW 1: Start Button ---
        _startButton = new JButton("Start New Game");
        _startButton.setFont(new Font("Arial", Font.PLAIN, 30));
        _startButton.setPreferredSize(new Dimension(300, 60));
        _startButton.setBackground(Color.decode("#d99e30")); // Give it a nice menu size
        
        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Row 1
        mainPanel.add(_startButton, gbc);
        
        
        // --- ROW 2: Exit Button ---
        _exitButton = new JButton("Exit Game");
        _exitButton.setFont(new Font("Arial", Font.PLAIN, 30));
        _exitButton.setPreferredSize(new Dimension(300, 60));
        _exitButton.setBackground(Color.decode("#d99e30")); // Give it a nice menu size
        
        gbc.gridx = 0; // Column 0
        gbc.gridy = 2; // Row 2
        mainPanel.add(_exitButton, gbc);

        // Functional Exit button action
        _exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(mainPanel);
        setVisible(true);
    }   

    public static void main(String[] args){
        // Good practice to run Swing on the event dispatch thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BlackJack();
            }
        });
    }
}