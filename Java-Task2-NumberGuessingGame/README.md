# 🎯 Number Guessing Game

**Oasis Infobyte Java Development Internship — Task 2**

[![Java Version](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Internship](https://img.shields.io/badge/Oasis%20Infobyte-Java%20Development-purple.svg)](https://oasisinfobyte.com/)

> An enterprise-grade, portfolio-ready Number Guessing Game built in **Java 17+** featuring dual execution modes (ANSI Console & Swing GUI), difficulty multipliers, statistical dashboards, and high score tracking.

---

## 📌 Project Overview & Internship Info

- **Internship Program:** Oasis Infobyte Java Development Internship
- **Task Assigned:** Task 2 – Number Guessing Game
- **Developer:** Yagnesh Patel
- **Repository:** [yagnesh747/OIBSIP](https://github.com/yagnesh747/OIBSIP)

---

## ✨ Features & Highlights

- **Dual UI Support:** ANSI-colored rich terminal console and modern dark-theme Swing GUI (`--gui`).
- **Difficulty Modes:** Easy (1–50, 10 attempts, 1.0x), Medium (1–100, 7 attempts, 1.5x), Hard (1–200, 5 attempts, 2.5x).
- **Proximity & Directional Hints:** "Too High / Too Low" paired with "Burning Hot / Warm / Cold" indicators.
- **Statistical Tracker:** Tracks total games played, wins, losses, win rate percentage, high score, and average attempts per win.
- **Dynamic Feedback:** Motivational quotes on every guess and end-of-round outcome.

---

## 🛠 Technologies Used

- **Language:** Java 17+
- **GUI Framework:** Java Swing
- **Console Interface:** ANSI Escape Codes (`AnsiColor.java`)
- **Web Interface:** HTML5, CSS3, JavaScript (Glassmorphism)

---

## 📂 Project Structure

```
OIBSIP/
└── Java-Task2-NumberGuessingGame/
    ├── src/
    │   ├── Main.java                        # Dual Launcher Entry Point
    │   ├── model/                           # Domain Data Models
    │   │   ├── DifficultyLevel.java
    │   │   ├── GameRound.java
    │   │   ├── GameStats.java
    │   │   └── GuessResult.java
    │   ├── service/                         # Core Logic & Services
    │   │   ├── GameEngine.java
    │   │   ├── ScoreCalculator.java
    │   │   └── StatisticsManager.java
    │   ├── ui/                              # Presentation Layer
    │   │   ├── AnsiColor.java
    │   │   ├── ConsoleUI.java
    │   │   ├── SwingGameFrame.java
    │   │   └── UIConstants.java
    │   ├── util/                            # Helpers & Validation
    │   │   ├── InputValidator.java
    │   │   └── MotivationalQuotes.java
    │   └── exception/                       # Custom Exception Domain
    │       ├── GameOverException.java
    │       └── InvalidGuessException.java
    ├── screenshots/                         # Screenshots folder
    ├── web_app.html                         # Interactive Web Preview
    ├── README.md                            # Documentation
    ├── LICENSE                              # MIT License
    └── .gitignore                           # Git ignore rules
```

---

## 💻 How to Compile & Run

### Prerequisites
- Install **JDK 17 or higher**.

### Steps

1. **Clone Repository:**
   ```bash
   git clone https://github.com/yagnesh747/OIBSIP.git
   cd OIBSIP/Java-Task2-NumberGuessingGame
   ```

2. **Compile Project:**
   ```bash
   mkdir bin
   javac -d bin src/model/*.java src/exception/*.java src/util/*.java src/service/*.java src/ui/*.java src/Main.java
   ```

3. **Run Console Interface:**
   ```bash
   java -cp bin Main
   ```

4. **Run Swing GUI Interface:**
   ```bash
   java -cp bin Main --gui
   ```

---

## 📜 License

Distributed under the [MIT License](LICENSE).
