
# 🃏 Blackjack Game (Java Swing GUI)

## 📌 Overview

This project is a simple Blackjack game implemented in Java using Swing for the graphical user interface. The game lets a player compete against a computer dealer, place bets, and track wins/losses and money remaining.

---

## 🎮 Features

- Full GUI using Java Swing
- Hit, Stand, and Restart options
- Betting system with:
  - Bet 25% of your current money
  - Bet 50%
  - All-in option
- Displays:
  - Player and dealer hands
  - Current game status
  - Player winnings, dealer wins, and remaining money
- Handles:
  - Aces counting as 11 or 1
  - Blackjack bonus (1.5x payout)
  - Busted conditions
  - Ties and automatic dealer logic

---

## 🧠 Game Rules (Simplified)

- The goal is to get as close to 21 without going over.
- Face cards (J, Q, K) count as 10.
- Aces can count as 11 or 1, depending on the situation.
- The dealer will continue drawing cards if under 16 and not already beating the player.
- Blackjack (Ace + 10-valued card) pays 1.5x the bet and is an Automatic win.

---

## 🛠 How to Run

1. Make sure you have Java installed (`JDK 8+`).
2. Compile the file:
   ```bash
   javac BlackjackGame.java
   ```
3. Run the program:
   ```bash
   java BlackjackGame
   ```
4. Make sure to use Java Swing
---

## 📂 File Structure

```
BlackjackGame.java    // Main class containing all game logic and GUI
README.md             // This file
```

---

## 💰 Money and Betting

- You start with **$1000**.
- Select a bet amount before the game begins.
- If you win, the money is added. If you lose, it’s subtracted.
- Blackjack earns **1.5x** your bet if you start with an Ace + 10.

---

## 🧩 Future Improvements (Ideas)

- Add card graphics instead of text
- Multiplayer support
- Save/load game state
- Background music or sound effects
- More realistic dealer behavior (e.g., insurance, splitting)

---

## 📸 Screenshots

![image](https://github.com/user-attachments/assets/ff5b3187-ad86-4db7-9bdb-aa071b7c8ca9)


---

## 👨‍💻 Author

- **Developed by:** Muhammad Moiz
- **Language:** Java
- **Libraries used:** Java Swing, AWT
