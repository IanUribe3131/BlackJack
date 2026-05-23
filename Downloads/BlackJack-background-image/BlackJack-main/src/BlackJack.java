import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class BlackJack extends JFrame {

    private static final int CARD_MAX_WIDTH = 150;
    private static final int CARD_MAX_HEIGHT = 150;
    private static final String PIXEL_FONT_NAME = choosePixelFontName();

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
        JPanel panel = new BackgroundPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel titLabel = new JLabel("BLACKJACK");
        titLabel.setFont(pixelFont(64));
        titLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titLabel, gbc);

        JButton starButton = new JButton("Start Game");
        styleButton(starButton);
        starButton.addActionListener(e -> {
            StartNewGame();
            cardLayout.show(mainPanel, "game");
        });
        gbc.gridy = 1;
        panel.add(starButton, gbc);

        return panel;
            
    }

    private JPanel createGameScreen(){
        JPanel panel = new BackgroundPanel(new BorderLayout());

        //dealer area
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        dealerScoreLabel = new JLabel("Dealer: ?", SwingConstants.CENTER);
        dealerScoreLabel.setForeground(Color.WHITE);
        dealerScoreLabel.setFont(pixelFont(30));
        dealerPanel = new JPanel();
        dealerPanel.setOpaque(false);
        topPanel.add(dealerScoreLabel, BorderLayout.NORTH);
        topPanel.add(dealerPanel, BorderLayout.CENTER);

        //status area
        statusLabel = new JLabel("Good Luck!", SwingConstants.CENTER);
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(pixelFont(32));

        //player area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        playerScoreLabel = new JLabel("Player: 0", SwingConstants.CENTER);
        playerScoreLabel.setForeground(Color.WHITE);
        playerScoreLabel.setFont(pixelFont(30));
        playerPanel = new JPanel();
        playerPanel.setOpaque(false);

        //control buttons
        JPanel controlsPanel = new JPanel();
        controlsPanel.setOpaque(false);
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        playAgainButton = new JButton("Play Again");
        homeButton = new JButton("Main Menu");

        styleButton(hitButton);
        styleButton(standButton);
        styleButton(playAgainButton);
        styleButton(homeButton);

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

    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(java.awt.LayoutManager layout) {
            super(layout);
            setOpaque(false);
            loadBackgroundImage();
        }

        private void loadBackgroundImage() {
            try {
                java.net.URL imageUrl = BlackJack.class.getResource("/background.jpg");
                if (imageUrl != null) {
                    backgroundImage = ImageIO.read(imageUrl);
                    return;
                }

                File imageFile = new File("src/background.jpg");
                if (!imageFile.exists()) {
                    imageFile = new File("background.jpg");
                }

                if (imageFile.exists()) {
                    backgroundImage = ImageIO.read(imageFile);
                }
            } catch (IOException e) {
                backgroundImage = null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(34, 139, 34));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
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
            playerPanel.add(createCardLabel(card, false));
        }
        playerScoreLabel.setText("Player: " + playerHand.calculateValue());

        ArrayList<Card> dCards = dealerHand.getCards();
        for(int i = 0; i < dCards.size(); i++){
            if(i==0 && !isGameOver){
                dealerPanel.add(createCardLabel(null, true));
            }else{
                dealerPanel.add(createCardLabel(dCards.get(i), false));
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


    private JLabel createCardLabel(Card card, boolean hidden) {
    JLabel label = new JLabel();
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setVerticalAlignment(SwingConstants.CENTER);

    String imagePath;

    if (hidden) {
        imagePath = "/cards/card_back.png";
    } else {
        imagePath = getCardImagePath(card);
    }

    ImageIcon originalIcon = loadCardImage(imagePath);

    if (originalIcon != null) {
        Dimension cardSize = getScaledCardSize(originalIcon);
        label.setPreferredSize(cardSize);

        Image scaledImage = originalIcon.getImage().getScaledInstance(
            cardSize.width,
            cardSize.height,
            Image.SCALE_SMOOTH
        );

        label.setIcon(new ImageIcon(scaledImage));
    } else {
        label.setPreferredSize(new Dimension(CARD_MAX_WIDTH, CARD_MAX_HEIGHT));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        label.setFont(pixelFont(14));

        if (hidden) {
            label.setText("??");
        } else {
            label.setText(card.getRank().name());
        }
    }

    return label;
}

private static String choosePixelFontName() {
    String[] preferredFonts = {"Press Start 2P", "OCR A Extended", "Consolas", "Courier New", "Monospaced"};
    String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    for (String preferredFont : preferredFonts) {
        for (String availableFont : availableFonts) {
            if (availableFont.equalsIgnoreCase(preferredFont)) {
                return availableFont;
            }
        }
    }

    return Font.MONOSPACED;
}

private static Font pixelFont(int size) {
    return new Font(PIXEL_FONT_NAME, Font.BOLD, size);
}

private static void styleButton(JButton button) {
    button.setFont(pixelFont(20));
    button.setForeground(Color.WHITE);
    button.setBackground(new Color(22, 22, 22));
    button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
    button.setFocusPainted(false);
}

private Dimension getScaledCardSize(ImageIcon icon) {
    int originalWidth = icon.getIconWidth();
    int originalHeight = icon.getIconHeight();

    if (originalWidth <= 0 || originalHeight <= 0) {
        return new Dimension(CARD_MAX_WIDTH, CARD_MAX_HEIGHT);
    }

    double scale = Math.min(
        (double) CARD_MAX_WIDTH / originalWidth,
        (double) CARD_MAX_HEIGHT / originalHeight
    );

    return new Dimension(
        (int) Math.round(originalWidth * scale),
        (int) Math.round(originalHeight * scale)
    );
}

private ImageIcon loadCardImage(String imagePath) {
    java.net.URL imageUrl = BlackJack.class.getResource(imagePath);
    if (imageUrl != null) {
        return new ImageIcon(imageUrl);
    }

    String filePath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;
    File imageFile = new File(filePath);
    if (!imageFile.exists()) {
        imageFile = new File("src", filePath);
    }
    if (!imageFile.exists()) {
        imageFile = new File("..", filePath);
    }

    if (imageFile.exists()) {
        return new ImageIcon(imageFile.getPath());
    }

    return null;
}
        
private String getCardImagePath(Card card) {
    String suit = card.getSuit().name().toLowerCase();
    String rank = getRankFileName(card.getRank());

    return "/cards/card_" + suit + "_" + rank + ".png";
}

private String getRankFileName(Card.Rank rank) {
    switch (rank) {
        case ACE:
            return "A";
        case TWO:
            return "02";
        case THREE:
            return "03";
        case FOUR:
            return "04";
        case FIVE:
            return "05";
        case SIX:
            return "06";
        case SEVEN:
            return "07";
        case EIGHT:
            return "08";
        case NINE:
            return "09";
        case TEN:
            return "10";
        case JACK:
            return "J";
        case QUEEN:
            return "Q";
        case KING:
            return "K";
        default:
            return "";
    }
}
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            new BlackJack().setVisible(true);
        });
    }
}
        
