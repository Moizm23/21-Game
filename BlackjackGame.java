import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class BlackjackGame extends JFrame {
    private JPanel mainPanel, buttonPanel;
    private JTextArea playerArea, dealerArea, Information;
    private JLabel statusLabel;
    private JButton hitButton, standButton, restartButton;
    private JButton bet25Button, bet50Button, allInButton;
     
    private ArrayList<String> playerHand;
    private ArrayList<String> dealerHand;
    
    private int card;
    private String cardType;
    private boolean allowedToEnd = false;
    private int playerScore = 0;
    private int dealerScore = 0;
    
    private int playerWins = 0;
    private int dealerWins = 0;
    private double money = 1000.00;
    private double bet = 0.00;


    private Random random = new Random();

    public BlackjackGame() {
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
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(new JScrollPane(Information), BorderLayout.CENTER);

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

        
        add(handPanel, BorderLayout.CENTER);

        // Buttons
        buttonPanel = new JPanel();
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        restartButton = new JButton("Restart");
        restartButton.setEnabled(false); // Disabled until game ends
        hitButton.setEnabled(false);
        standButton.setEnabled(false);

        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(restartButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Status
        statusLabel = new JLabel("Welcome to Blackjack!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);
        
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
         
        betPlace();

        setVisible(true);
    }
    
    private void betPlace(){
        bet25Button.setEnabled(true);
        bet50Button.setEnabled(true);
        allInButton.setEnabled(true);
        restartButton.setEnabled(false);
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        playerArea.append("\n" + "PLACE A BET");
        dealerArea.append("\n" + "PLACE A BET");
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        Information.append("\n" + "Money Left: " + String.format("%.2f", money));
        
    }
    
    private void gameStarter(){
        restartButton.setEnabled(false);
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        bet25Button.setEnabled(false);
        bet50Button.setEnabled(false);
        allInButton.setEnabled(false);
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        

        
        playerHand.add(newPlayerCard());
        playerHand.add(newPlayerCard());
        playerArea.setText("Your cards: " + playerHand.toString());
        playerArea.append("\n" + "Total Card Amount: " + playerScore);
        
        dealerHand.add(newDealerCard());
        dealerHand.add("???");
        dealerArea.setText("Dealers Cards: " + dealerHand);
        dealerArea.append("\n" + "Total Card Amount: " + dealerScore);
        
        if (playerScore > 21){
            System.out.println("ReWork");
            playerHand.set(1, "A (1)");
            playerScore = playerScore - 10;
            playerArea.setText("Your cards: " + playerHand.toString());
            playerArea.append("\n" + "Total Card Amount: " + playerScore);
            
        }
    
    }
    
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
    
    private void hitAction(){
        playerHand.add(newPlayerCard());
        
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
    
    private void standAction(){
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        
        for (int i = 0; i < dealerHand.size(); i++) {
            if (dealerHand.get(i).equals("???")) {
                dealerHand.remove(i);
            }
        }
        
        while (16 > dealerScore && dealerScore <= playerScore){
            dealerHand.add(newDealerCard());
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
    
    private void endGameCheck(){
        if (playerScore > 21 && allowedToEnd == true){
            restartButton.setEnabled(true);
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            
            playerArea.append("\n" + "\n" + "PLAYER LOSES, YOU BUSTED");
            dealerArea.append("\n" + "\n" + "DEALER WINS!" + "\n" + "TRY AGAIN?");
            dealerWins++;
            moneyLost();
        }else if (dealerScore <= 21 && dealerScore > playerScore && allowedToEnd == true){
            restartButton.setEnabled(true);
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            
            playerArea.append("\n" + "\n" + "PLAYER LOSES");
            dealerArea.append("\n" + "\n" + "DEALER WINS!" + "\n" + "TRY AGAIN?");
            dealerWins++;
            moneyLost();
        }
        
        if (dealerScore > 21 && allowedToEnd == true){
            restartButton.setEnabled(true);

            playerArea.append("\n" + "\n" + "PLAYER WINS  :) ");
            dealerArea.append("\n" + "\n" + "DEALER LOSES, HE BUSTED");
            playerWins++;
            moneyWon();
            
        }else if (dealerScore >=16 && playerScore > dealerScore && allowedToEnd == true){
            restartButton.setEnabled(true);
            playerArea.append("\n" + "\n" + "PLAYER WINS  :) ");
            dealerArea.append("\n" + "\n" + "DEALER LOSES");
            playerWins++;
            moneyWon();
            
        }
        
        if (dealerScore == playerScore && allowedToEnd == true){
            playerArea.append("\n" + "\n" + "GAME TIED");
            dealerArea.append("\n" + "\n" + "GAME TIED");
            
            restartButton.setEnabled(true);
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        }
        
        
    }
    
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
    
    public void infoRefresh(){
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        Information.append("\n" + "Money Left: " + String.format("%.2f", money));
    }
    
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
    
    public void moneyWon(){
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        if (playerHand.get(0).equals("A (11)") || playerHand.get(0).equals("J (10)") || playerHand.get(0).equals("Q (10)") || playerHand.get(0).equals("K (10)")){
            if (playerHand.get(1).equals("A (11)") || playerHand.get(1).equals("J (10)") || playerHand.get(1).equals("Q (10)") || playerHand.get(1).equals("K (10)")){
                bet = 1.5 * bet;
            }
        }
        Information.append("\n" + "You Won: " + String.format("%.2f", bet)); 
        money = money + bet;
        Information.append("\n" + "Money Left: " + String.format("%.2f", money)); 
    }
    
    public void moneyLost(){
        Information.setText("Games the Player won: " + playerWins);
        Information.append("\n" + "Games Dealer won: " + dealerWins);
        Information.append("\n" + "You LOST: " + bet); 
        Information.append("\n" + "You LOST: " + String.format("%.2f", bet)); 
        money = money - bet;
        Information.append("\n" + "Money Left: " + String.format("%.2f", money));
    }

    public static void main(String[] args) {
        new BlackjackGame();
    }
    
}
