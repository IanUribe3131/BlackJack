import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.CardLayout; // 1. Need this import
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class BlackJack extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private Deck deck;
    private Hand dealerHand;
    private Hand playerHand;
    private boolean isGameOver;
    private JButton hitButton, standButton, playAgainButton, homeButton;
    private JPanel dealerPanel, playerPanel;
    private JLabel statusLabel, dealerScoreLabel, playerScoreLabel;


    public BlackJack(){
        setTitle("BlackJack Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // 2. Initialize the CardLayout and the main container panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createHomeScreen(),"home");
        mainPanel.add(createGameScreen(),"game");
        
        add(mainPanel);

    }   

    private JPanel createHomeScreen(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(34, 139, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel titLabel = new JLabel("BLACKJACK");
        titLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titLabel, gbc);

        JButton starButton = new JButton("Start Game");
        starButton.setFont(new Font("Arial", Font.BOLD, 24));
        starButton.addActionListener(e -> {
            StartNewGame();
            cardLayout.show(mainPanel, "game");
        });
        gbc.gridy = 1;
        panel.add(starButton, gbc);

        return panel;
            
    }

    private JPanel createGameScreen(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(34, 139, 34));

        //dealer area
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        dealerScoreLabel = new JLabel("Dealer: ?", SwingConstants.CENTER);
        dealerScoreLabel.setForeground(Color.WHITE);
        dealerScoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        dealerPanel = new JPanel();
        dealerPanel.setOpaque(false);
        topPanel.add(dealerScoreLabel, BorderLayout.NORTH);
        topPanel.add(dealerPanel, BorderLayout.CENTER);

        //status area
        statusLabel = new JLabel("Good Luck!", SwingConstants.CENTER);
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 30));

        //player area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        playerScoreLabel = new JLabel("Player: 0", SwingConstants.CENTER);
        playerScoreLabel.setForeground(Color.WHITE);
        playerScoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        playerPanel = new JPanel();
        playerPanel.setOpaque(false);

        //control buttons
        JPanel controlsPanel = new JPanel();
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        playAgainButton = new JButton("Play Again");
        homeButton = new JButton("Main Menu");

        controlsPanel.add(hitButton);
        controlsPanel.add(standButton);
        controlsPanel.add(playAgainButton);
        controlsPanel.add(homeButton);

        bottomPanel.add(playerPanel, BorderLayout.NORTH);
        bottomPanel.add(playerScoreLabel, BorderLayout.CENTER);
        bottomPanel.add(controlsPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        //action listeners
        hitButton.addActionListener(e-> handleHit());
        standButton.addActionListener(e-> handleStand());
        playAgainButton.addActionListener(e-> StartNewGame());
        homeButton.addActionListener(e-> cardLayout.show(mainPanel, "home"));

        return panel;
    }

    //Game Logic
    private void StartNewGame(){
        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();
        isGameOver = false;

        playerHand.addCards(deck.drawCard());
        dealerHand.addCards(deck.drawCard());
        playerHand.addCards(deck.drawCard());
        dealerHand.addCards(deck.drawCard());

        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        playAgainButton.setEnabled(true);
        statusLabel.setText("Hit or Stand?");

        updateUIstate();
    }

    private void handleHit(){
        playerHand.addCards(deck.drawCard());
        if(playerHand.calculateValue() > 21){
            isGameOver = true;
            statusLabel.setText("Yikes! You Lose.");
            endGameUI();
        }
        updateUIstate();
    }

    private void handleStand(){
        isGameOver = true;
        while(dealerHand.calculateValue() < 17){
            dealerHand.addCards(deck.drawCard());
        }

        int playerTotal = playerHand.calculateValue();
        int dealerTotal = dealerHand.calculateValue();

        if(dealerTotal > 21){
            statusLabel.setText("Dealer Busts! You Win!");
        }else if(playerTotal > dealerTotal){
            statusLabel.setText("You Win!");
        }else if(playerTotal < dealerTotal){
            statusLabel.setText("Dealer Wins!");
        }else{
            statusLabel.setText("Push (tie)!");
        }

        endGameUI();
        updateUIstate();
    }
    private void endGameUI(){
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        playAgainButton.setEnabled(true);
    }           
    private void updateUIstate(){
        playerPanel.removeAll();
        dealerPanel.removeAll();
    
        for (Card card : playerHand.getCards()){
            playerPanel.add(createCardLabel(card.getRank().name(), card.getSuit()));
        }
        playerScoreLabel.setText("Player: " + playerHand.calculateValue());

        ArrayList<Card> dCards = dealerHand.getCards();
        for(int i = 0; i < dCards.size(); i++){
            if(i==0 && !isGameOver){
                dealerPanel.add(createCardLabel("??",null));
            }else{
                dealerPanel.add(createCardLabel(dCards.get(i).getRank().name(), dCards.get(i).getSuit()));
            }
        }
        
        if(isGameOver){
            dealerScoreLabel.setText("Dealer: "+ dealerHand.calculateValue());
        }else{
            int visibleScore = dCards.get(1).getRank().value;
            if(dCards.get(1).rank == Card.Rank.ACE) visibleScore = 11;
            dealerScoreLabel.setText("Dealer: "+ visibleScore + " + ?");
        }
        playerPanel.revalidate();
        playerPanel.repaint();
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    private JLabel createCardLabel(String text, Card.Suit suit){
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(100,150));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2,true));
        label.setFont(new Font("Arial", Font.BOLD, 18));
        if(suit == Card.Suit.HEARTS || suit == Card.Suit.DIAMONDS){
            label.setForeground(Color.RED);
        }else{
            label.setForeground(Color.BLACK);
        }
    return label;
    }
        
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            new BlackJack().setVisible(true);
        });
    }
}
        