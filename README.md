# ☕ Java Development Internship — Oasis Infobyte (OIBSIP)

[![Java Version](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Internship](https://img.shields.io/badge/Oasis%20Infobyte-Java%20Development-purple.svg)](https://oasisinfobyte.com/)
[![Repository](https://img.shields.io/badge/GitHub-OIBSIP-181717.svg?logo=github)](https://github.com/yagnesh747/OIBSIP)

Welcome to my **Oasis Infobyte Java Development Internship (OIBSIP)** repository! This repository showcases production-quality Java applications built following Object-Oriented Programming (OOP) principles, clean architecture, exception handling, thread-safe persistence, and handcrafted desktop GUI designs.

---

## 📌 Internship Details

- **Organization:** Oasis Infobyte
- **Domain:** Java Development
- **Intern:** Yagnesh
- **Repository:** [yagnesh747/OIBSIP](https://github.com/yagnesh747/OIBSIP)

---

## 📂 Projects Overview

| Task # | Project Name | Description | Status |
| :---: | :--- | :--- | :---: |
| **Task 2** | [Number Guessing Game](./Java-Task2-NumberGuessingGame) | A feature-rich guessing game with difficulty levels, proximity hints, live score multipliers, and session statistics. | ✅ Completed |
| **Task 3** | [ATM Interface](./Java-Task3-ATMInterface) | A multi-account ATM simulation with SHA-256 PIN security, cash withdrawals, deposits, fund transfers, transaction history, and text receipt generation. | ✅ Completed |

---

## 🛠 Technologies Used

- **Core Language:** Java 17+
- **GUI Framework:** Java Swing (`JFrame`, `CardLayout`, `GridBagLayout`, `JTable`)
- **Programming Concepts:** Object-Oriented Programming (OOP), Enums, Records, Encapsulation, Polymorphism
- **Data Structures:** Java Collections Framework (`ArrayList`, `ConcurrentHashMap`)
- **Exception Handling:** Custom Exception Hierarchy (`InsufficientFundsException`, `InvalidGuessException`, etc.)
- **Persistence & Security:** Java NIO (`java.nio.file`), CSV Storage, SHA-256 Digest Hashing
- **Version Control:** Git & GitHub

---

## 📁 Repository Structure

```text
OIBSIP/
├── .gitignore
├── README.md
│
├── Java-Task2-NumberGuessingGame/
│   ├── src/
│   │   ├── Main.java                        # Dual Launcher (Console / GUI)
│   │   ├── model/                           # DifficultyLevel, GameRound, GameStats...
│   │   ├── service/                         # GameEngine, ScoreCalculator...
│   │   ├── ui/                              # ConsoleUI, SwingGameFrame, UIConstants...
│   │   ├── util/                            # InputValidator, MotivationalQuotes
│   │   └── exception/                       # Custom Exceptions
│   ├── screenshots/                         # Screenshots folder
│   ├── web_app.html                         # Web preview interface
│   ├── README.md                            # Task 2 documentation
│   ├── LICENSE                              # MIT License
│   └── .gitignore
│
└── Java-Task3-ATMInterface/
    ├── src/
    │   ├── Main.java                        # App Entry Point
    │   ├── model/                           # Account, Transaction, Enums...
    │   ├── service/                         # Auth, Account, Transaction, Receipt...
    │   ├── ui/                              # ATMFrame, Dashboard, Withdraw, Deposit...
    │   ├── util/                            # SecurityUtil, CurrencyFormatter...
    │   ├── exception/                       # Custom Exceptions
    │   └── data/                            # DataStore (CSV Handler)
    ├── data/                                # CSV storage files
    ├── screenshots/                         # Screenshots folder
    ├── web_app.html                         # Web preview interface
    ├── README.md                            # Task 3 documentation
    ├── LICENSE                              # MIT License
    └── .gitignore
```

---

## ✨ Project Features Summary

### 🎯 Task 2 – Number Guessing Game
- **Dual UI Support:** ANSI-colored terminal console and dark-theme Swing GUI (`--gui`).
- **Difficulty Modes:** Easy (1–50, 10 attempts), Medium (1–100, 7 attempts), Hard (1–200, 5 attempts).
- **Proximity Hints:** Directional feedback ("Too High/Low") paired with distance indicators ("Burning Hot / Warm / Cold").
- **Statistics Dashboard:** Tracks Games Played, Wins, Losses, Win Rate %, High Score, and Average Attempts.

### 🏧 Task 3 – Enterprise ATM Interface
- **Authentication & Security:** SHA-256 PIN encryption & 3-strike account lockout protection.
- **Banking Operations:** Cash Withdrawal (preset amounts & ₹100 denomination check), Deposit, Inter-account Transfer, PIN Change, Balance Inquiry.
- **Persistence & Receipts:** Thread-safe CSV file storage, printable text receipt generation (`receipts/`), and rotating file logs (`logs/`).

---

## 📸 Screenshots

*(Screenshots are hosted within their respective project directories)*

- **Task 2 Screenshots:** [Java-Task2-NumberGuessingGame/screenshots](./Java-Task2-NumberGuessingGame/screenshots)
- **Task 3 Screenshots:** [Java-Task3-ATMInterface/screenshots](./Java-Task3-ATMInterface/screenshots)

---

## 🎓 Learning Outcomes

- Applied **SOLID principles** and **Layered Architecture** (Model, Service, Persistence, UI).
- Gained hands-on experience designing responsive **Java Swing GUIs** and custom component styling.
- Practiced defensive programming with custom **Exception Hierarchies** and robust input validation.
- Implemented file persistence using **Java NIO** and CSV storage.
- Standardized git workflows using structured commit messages and documentation.

---

## 👤 Author

- **Name:** Yagnesh
- **Internship:** Oasis Infobyte Java Development Intern
- **GitHub:** [@yagnesh747](https://github.com/yagnesh747)

---

## 📜 License

This repository is open-source and available under the [MIT License](LICENSE).
