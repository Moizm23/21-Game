import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class BlackjackGame extends JFrame {
    
    // UI components
    private JPanel mainPanel, buttonPanel;
    private JTextArea playerArea, dealerArea, Information;
    private JLabel statusLabel;
    private JButton hitButton, standButton, restartButton;
    private JButton bet25Button, bet50Button, allInButton;
    
    
    // Logic Variables 
    private ArrayList<String> playerHand;
    private ArrayList<String> dealerHand;
    
    private int card;
    private String cardType;
    private boolean allowedToEnd = false;
    private int playerScore = 0;
    private int dealerScore = 0;
    
    // Tracking Variables
    private int playerWins = 0;
    private int dealerWins = 0;
    private double money = 1000.00;
    private double bet = 0.00;


    private Random random = new Random();

    public BlackjackGame() {
        
        //Setting up the Frame
        setTitle("Blackjack");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Text areas to display hands
        playerArea = new JTextArea(5, 20);
        dealerArea = new JTextArea(5, 20);
        Information = new JTextArea(5, 20);
        Information.setEditable(false);
        playerArea.setEditable(false);
        dealerArea.setEditable(false);

        // Labels for areas
        JPanel handPanel = new JPanel(new GridLayout(2, 3));
        handPanel.add(new JLabel("Player Hand:"));
        handPanel.add(new JLabel("Dealer Hand:"));
        handPanel.add(new JLabel("Information"));
        handPanel.add(new JScrollPane(playerArea));
        handPanel.add(new JScrollPane(dealerArea));
        
        // Info panel at bottom of handPanel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(new JScrollPane(Information), BorderLayout.CENTER);
        
        // Bet buttons
        JPanel betPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        bet25Button = new JButton("Bet 25%");
        bet50Button = new JButton("Bet 50%");
        allInButton = new JButton("All In");

        // Add buttons to betPanel
        betPanel.add(bet25Button);
        betPanel.add(bet50Button);
        betPanel.add(allInButton);

        // Add both Information area and bet buttons
        infoPanel.add(betPanel, BorderLayout.SOUTH);
        handPanel.add(infoPanel);

        // Add hand panel to center of frame
        add(handPanel, BorderLayout.CENTER);

        // Buttons
        buttonPanel = new JPanel();
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        restartButton = new JButton("Restart");
        
        // Setting Button States
        restartButton.setEnabled(false); // Disabled until game ends
        hitButton.setEnabled(false);
        standButton.setEnabled(false);

        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(restartButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Status label at the top
        statusLabel = new JLabel("Welcome to Blackjack!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);
        // Set font to bold, size 24 (you can change size as you like)
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        
        // Button listeners
        hitButton.addActionListener(e -> {
            hitAction();
            infoRefresh();
            endGameCheck();
        });
        
        standButton.addActionListener(e -> {
            standAction();
            allowedToEnd = true;
            infoRefresh();
            endGameCheck();
        });
        
        restartButton.addActionListener(e -> {
            restartGame();
            allowedToEnd = false;
            endGameCheck();
        });
        
        bet25Button.addActionListener(e -> {
            bet = Math.floor(money * 0.25 * 100.0) / 100.0;
            allowedToEnd = false;
            bet25();
            gameStarter();
            endGameCheck();
            
        });
        bet50Button.addActionListener(e -> {
            bet = Math.floor(money * 0.5 * 100.0) / 100.0;
            allowedToEnd = false;
            bet50();
            gameStarter();
            endGameCheck();
        });
        allInButton.addActionListener(e -> {
            bet = Math.floor(money * 100.0) / 100.0; // All-in
            allowedToEnd = false;
            allIn();
            gameStarter();
            endGameCheck();
        });
        
        
        // Set up initial state
        betPlace();

        setVisible(true);
    }
    
    
    // Setting up the betting phase
    private void betPlace(){
        
        //reseting scores and states
        bet25Button.setEnabled(true);
        bet50Button.setEnabled(true);
        allInButton.setEnabled(true);
        restartButton.setEnabled(false);
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        
        // Changing the Text in the Text Areas
        playerArea.append("\n" + "PLACE A BET");
        dealerArea.append("\n" + "PLACE A BET");
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        Information.append("\n" + "Money Left: " + String.format("%.2f", money));
        
    }
    
    
     // Begin the game round
    private void gameStarter(){
        
        // Reset scores and states
        playerScore = 0;
        dealerScore = 0;
        restartButton.setEnabled(false);
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        bet25Button.setEnabled(false);
        bet50Button.setEnabled(false);
        allInButton.setEnabled(false);
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        

        // Deal initial cards
        playerHand.add(newPlayerCard());
        playerHand.add(newPlayerCard());
        playerArea.setText("Your cards: " + playerHand.toString());
        playerArea.append("\n" + "Total Card Amount: " + playerScore);
        
        dealerHand.add(newDealerCard());
        dealerHand.add("???");
        dealerArea.setText("Dealers Cards: " + dealerHand);
        dealerArea.append("\n" + "Total Card Amount: " + dealerScore);
        
        // Adjust for Ace if busting, only in the intial two cards here.
        if (playerScore > 21){
            System.out.println("ReWork");
            playerHand.set(1, "A (1)");
            playerScore = playerScore - 10;
            playerArea.setText("Your cards: " + playerHand.toString());
            playerArea.append("\n" + "Total Card Amount: " + playerScore);
            
        }
    
    }
    
    
     // Deal card to player
    private String newPlayerCard(){
        Random rand = new Random();
        int card = rand.nextInt(13);
        card ++;
        
        cardType = specialNames(card);
        
        if (card > 10){
            card = 10;
        }
        
        if (card == 1){
            card = 11;
        }
        playerScore = playerScore + card;
        System.out.println("Player :" + playerScore);
        
        return cardType;
    }
    
     // Deal card to dealer
    private String newDealerCard(){
        Random rand = new Random();
        int card = rand.nextInt(13);
        card ++;
        
        cardType = specialNames(card);
        
        if (card > 10){
            card = 10;
        }
        
        if (card == 1){
            card = 11;
        }
        
        dealerScore = dealerScore + card;
        System.out.println("Dealer :" + dealerScore);
        
        return cardType;
    }
    
    // Convert card number to name
    private String specialNames(int card){
        
        if (card == 1){
            return "A (11)";
        }else if (card == 11){
            return "J (10)";
        }else if (card == 12){
            return "Q (10)";
        }else if (card == 13){
            return "K (10)";
        }else{
            return String.valueOf(card);
        }
    }
    
    // Player chooses to hit
    private void hitAction(){
        playerHand.add(newPlayerCard());
         // Handle Ace adjustment
        for (int i = 0; i < playerHand.size(); i++) {
            if (playerHand.get(i).equals("A (11)") && playerScore > 21) {
                playerHand.set(i, "A (1)");
                playerScore = playerScore - 10;
                
            }
            
        }
        
        playerArea.setText("Your cards: " + playerHand.toString());
        playerArea.append("\n" + "Total Card Amount: " + playerScore);
        if (playerScore > dealerScore){
            allowedToEnd = true;
        }

        
        System.out.println(playerHand + " " + dealerHand);
    }
    
    // Player chooses to stand
    private void standAction(){
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        
        // Reveal hidden dealer card
        for (int i = 0; i < dealerHand.size(); i++) {
            if (dealerHand.get(i).equals("???")) {
                dealerHand.remove(i);
            }
        }
        // Dealer hits until score is at least 16
        while (16 > dealerScore && dealerScore <= playerScore){
            dealerHand.add(newDealerCard());
            // Handle Ace adjustment
            for (int i = 0; i < dealerHand.size(); i++) {
                if (dealerHand.get(i).equals("A (11)") && dealerScore > 21) {
                    dealerHand.set(i, "A (1)");
                    dealerScore = dealerScore - 10;
                }
            }
            dealerArea.setText("Dealers Cards: " + dealerHand);
            dealerArea.append("\n" + "Total Card Amount: " + dealerScore);
        }
    }
    // Check for game over conditions
    private void endGameCheck(){
        
        // Player busts
        if (playerScore > 21 && allowedToEnd == true){
            restartButton.setEnabled(true);
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            
            playerArea.append("\n" + "\n" + "PLAYER LOSES, YOU BUSTED");
            dealerArea.append("\n" + "\n" + "DEALER WINS!" + "\n" + "TRY AGAIN?");
            dealerWins++;
            moneyLost();
        // Dealer wins
        }else if (dealerScore <= 21 && dealerScore > playerScore && allowedToEnd == true){
            restartButton.setEnabled(true);
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            
            playerArea.append("\n" + "\n" + "PLAYER LOSES");
            dealerArea.append("\n" + "\n" + "DEALER WINS!" + "\n" + "TRY AGAIN?");
            dealerWins++;
            moneyLost();
        }
        // Player wins: dealer bust
        if (dealerScore > 21 && allowedToEnd == true){
            restartButton.setEnabled(true);

            playerArea.append("\n" + "\n" + "PLAYER WINS  :) ");
            dealerArea.append("\n" + "\n" + "DEALER LOSES, HE BUSTED");
            playerWins++;
            moneyWon();
        // Player wins by higher score    
        }else if (dealerScore >=16 && playerScore > dealerScore && allowedToEnd == true){
            restartButton.setEnabled(true);
            playerArea.append("\n" + "\n" + "PLAYER WINS  :) ");
            dealerArea.append("\n" + "\n" + "DEALER LOSES");
            playerWins++;
            moneyWon();
            
        }
        // Blackjack condition
        if (playerHand.size() == 2 && ((playerHand.get(0).equals("A (11)") && isTenValue(playerHand.get(1))) || (playerHand.get(1).equals("A (11)") && isTenValue(playerHand.get(0))))){
            restartButton.setEnabled(true);
            playerArea.append("\n" + "\n" + "PLAYER WINS" + "\n" + "YOU GOT BLACKJACK  :) ");
            dealerArea.append("\n" + "\n" + "DEALER LOSES");
            playerWins++;
            moneyWon();
            
            restartButton.setEnabled(true);
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        // Tie game
        }else if (dealerScore == playerScore && allowedToEnd == true){
            playerArea.append("\n" + "\n" + "GAME TIED");
            dealerArea.append("\n" + "\n" + "GAME TIED");
            
            restartButton.setEnabled(true);
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        }
        
        
    }
    // Restart the game round
    private void restartGame(){
        restartButton.setEnabled(false);
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        allowedToEnd = false;
        dealerScore = 0;
        playerScore = 0;
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        playerArea.setText(" ");
        dealerArea.setText(" ");
        System.out.println("Restart");
        betPlace();
    }
    // Update info box after each action
    public void infoRefresh(){
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        Information.append("\n" + "Money Left: " + String.format("%.2f", money));
    }
    
    
    // Display betting info for each option
    public void bet25(){
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        Information.append("\n" + "Money Left: " + String.format("%.2f", money));
        Information.append("\n" + "Money Bet: " + String.format("%.2f", bet));
    }
    public void bet50(){
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        Information.append("\n" + "Money Left: " + String.format("%.2f", money));
        Information.append("\n" + "Money Bet: " + String.format("%.2f", bet));
    }
    public void allIn(){
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        Information.append("\n" + "Money Left: " + String.format("%.2f", money));
        Information.append("\n" + "Money Bet: " + String.format("%.2f", bet));
    }
    
    
    // Handle win logic and payout
    public void moneyWon(){
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        if (playerHand.size() == 2 && ((playerHand.get(0).equals("A (11)") && isTenValue(playerHand.get(1))) || (playerHand.get(1).equals("A (11)") && isTenValue(playerHand.get(0))))){
                bet = 1.5 * bet;
            }
        Information.append("\n" + "You Won: " + String.format("%.2f", bet)); 
        money = money + bet;
        Information.append("\n" + "Money Left: " + String.format("%.2f", money)); 
    }
    // Handle loss logic and subtract money
    public void moneyLost(){
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        Information.append("\n" + "You LOST: " + bet); 
        Information.append("\n" + "You LOST: " + String.format("%.2f", bet)); 
        money = money - bet;
        Information.append("\n" + "Money Left: " + String.format("%.2f", money));
    }
    // Check if a card is worth 10
    private boolean isTenValue(String card) {
    return card.equals("10") || card.equals("J (10)") || card.equals("Q (10)") || card.equals("K (10)");
        
    }
    
    // Main method
    public static void main(String[] args) {
        new BlackjackGame();
    }
    
}
