# 🏧 Enterprise ATM Interface Simulation

**Oasis Infobyte Java Development Internship — Task 3**

[![Java Version](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Internship](https://img.shields.io/badge/Oasis%20Infobyte-Java%20Development-purple.svg)](https://oasisinfobyte.com/)
[![Architecture](https://img.shields.io/badge/Architecture-Layered%20OOP-orange.svg)]()

> A production-grade, secure, multi-account Automated Teller Machine (ATM) application built using **Java 17+** with a **Java Swing GUI** and **Web Interface**. Designed and engineered to exceed all Oasis Infobyte internship evaluation standards.

---

## 📌 Project Overview & Internship Details

- **Internship Program:** Oasis Infobyte Java Development Internship
- **Task Assigned:** Task 3 – ATM Interface
- **Developer:** Yagnesh Patel
- **Repository:** [yagnesh747/OIBSIP](https://github.com/yagnesh747/OIBSIP)

---

## 🎯 Objective

The primary objective of this project is to simulate real-world Automated Teller Machine (ATM) banking operations in a secure, robust, and user-friendly software environment. The application handles user authentication, balance inquiries, cash withdrawals, deposits, intra-bank fund transfers, transaction history tracking, and security management while enforcing clean code principles, exception handling, and persistent data storage.

---

## ✨ Features & Functional Scope

### 🔒 Authentication & Security
- **SHA-256 Hashing:** Raw PINs are never stored in cleartext (`SecurityUtil.java`).
- **3-Strike Account Lockout:** Accounts lock automatically after 3 consecutive failed login attempts.
- **Session Management:** Secure login/logout workflows with context cleanup.

### 💰 Core Banking Operations
- **Cash Withdrawal:** Fast-withdraw preset amounts (₹500, ₹1000, ₹2000, ₹5000) or custom input with ₹100 denomination validation.
- **Cash Deposit:** Real-time account balance updates and instant transaction logging.
- **Fund Transfer:** Transfer funds safely to another valid account ID with recipient validation.
- **PIN Change:** Self-service PIN modification with old PIN verification and confirmation matching.
- **Balance Inquiry & Mini Statement:** Displays active account balance and the last 5 transactions.

### 📜 Persistence, Receipts & Logging
- **Thread-Safe Data Persistence:** CSV file storage (`data/accounts.csv`, `data/transactions.csv`) maintained via singleton `DataStore`.
- **Text Receipt Generation:** Exportable formatted transaction receipts saved under `receipts/`.
- **System Logging:** Rotating file logger (`logs/atm.log`) using `java.util.logging`.

---

## 🧩 OOP Concepts Applied

1. **Encapsulation:**
   - Private fields with public getters/setters in models like `Account` and `Transaction`.
   - Internal state protection (e.g., daily withdrawal limit tracking, failed attempt counters).

2. **Abstraction:**
   - Hiding complex file I/O operations behind `DataStore` interface methods (`findByUserId`, `saveAccounts`).
   - Hiding business constraints (minimum balance, maximum single withdrawal) inside `AccountService`.

3. **Inheritance:**
   - Extending custom exceptions from `java.lang.Exception` (`InsufficientFundsException`, `InvalidAmountException`, etc.).
   - Extending `JPanel` and `JFrame` for specialized UI components (`LoginPanel`, `DashboardPanel`, `ATMFrame`).

4. **Polymorphism:**
   - Method overloading in constructors (`Transaction` record initialization with auto-timestamp vs. explicit timestamp).
   - Dynamic dispatch in Swing action listeners and card layout switching.

---

## 🛠 Technologies Used

- **Programming Language:** Java 17+
- **GUI Framework:** Java Swing (`JFrame`, `CardLayout`, `GridBagLayout`, `JTable`)
- **Web Frontend:** HTML5, CSS3 (Glassmorphism), Modern JavaScript (ES6+)
- **Security & Cryptography:** `java.security.MessageDigest` (SHA-256)
- **Persistence Layer:** Java NIO (`java.nio.file`) with CSV format
- **Logging System:** `java.util.logging.Logger` with rotating `FileHandler`

---

## 🏛 Project Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│    (Swing GUI: LoginPanel, DashboardPanel, BalancePanel...) │
│             & Single-File Web Interface (web_app.html)       │
├─────────────────────────────────────────────────────────────┤
│                    Business Service Layer                   │
│ (AuthService, AccountService, TransactionService, Receipts) │
├─────────────────────────────────────────────────────────────┤
│                     Domain Model Layer                      │
│       (Account, Transaction, AccountStatus, Enums)          │
├─────────────────────────────────────────────────────────────┤
│                     Utility Layer                           │
│ (SecurityUtil, InputValidator, CurrencyFormatter, Logger)   │
├─────────────────────────────────────────────────────────────┤
│                   Data Persistence Layer                    │
│          (DataStore — Thread-Safe CSV Persistence)          │
└─────────────────────────────────────────────────────────────┘
```

---

## 📂 Folder Structure

```
OIBSIP/
└── Java-Task3-ATMInterface/
    ├── src/
    │   ├── Main.java                        # Entry Point
    │   ├── model/                           # Domain Data Models
    │   │   ├── Account.java
    │   │   ├── AccountStatus.java
    │   │   ├── Transaction.java
    │   │   └── TransactionType.java
    │   ├── service/                         # Business Services
    │   │   ├── AccountService.java
    │   │   ├── AuthenticationService.java
    │   │   ├── ReceiptService.java
    │   │   └── TransactionService.java
    │   ├── ui/                              # Swing User Interface
    │   │   ├── ATMFrame.java
    │   │   ├── BalancePanel.java
    │   │   ├── ChangePinPanel.java
    │   │   ├── DashboardPanel.java
    │   │   ├── DepositPanel.java
    │   │   ├── HistoryPanel.java
    │   │   ├── LoginPanel.java
    │   │   ├── TransferPanel.java
    │   │   ├── UIConstants.java
    │   │   └── WithdrawPanel.java
    │   ├── util/                            # Utilities & Helpers
    │   │   ├── AppLogger.java
    │   │   ├── CurrencyFormatter.java
    │   │   ├── InputValidator.java
    │   │   └── SecurityUtil.java
    │   ├── exception/                       # Custom Exception Hierarchy
    │   │   ├── AccountLockedException.java
    │   │   ├── AuthenticationException.java
    │   │   ├── InsufficientFundsException.java
    │   │   ├── InvalidAmountException.java
    │   │   └── TransactionException.java
    │   └── data/                            # Persistence Storage Handler
    │       └── DataStore.java
    ├── data/                                # CSV storage files
    │   ├── accounts.csv
    │   └── transactions.csv
    ├── receipts/                            # Generated text receipts
    ├── logs/                                # Execution log files
    ├── screenshots/                         # UI Screenshots
    ├── web_app.html                         # Web interface preview
    ├── README.md                            # Project Documentation
    ├── LICENSE                              # MIT License
    └── .gitignore                           # Git ignore rules
```

---

## 🔑 Pre-Seeded Test Credentials

| User ID | PIN | Account ID | Holder Name | Initial Balance |
|---|---|---|---|---|
| `USR001` | `1234` | `ACC001` | Yagnesh Patel | ₹50,000.00 |
| `USR002` | `5678` | `ACC002` | Priya Sharma | ₹1,25,000.00 |
| `USR003` | `9012` | `ACC003` | Rahul Kumar | ₹75,000.00 |

---

## 💻 Installation & How to Run

### Prerequisites
- Install **JDK 17 or higher** and ensure `javac` / `java` commands are in your system `PATH`.

### Compilation & Execution Steps

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/yagnesh747/OIBSIP.git
   cd OIBSIP/Java-Task3-ATMInterface
   ```

2. **Compile Source Code:**
   ```bash
   mkdir bin
   javac -d bin src/model/*.java src/exception/*.java src/util/*.java src/data/*.java src/service/*.java src/ui/*.java src/Main.java
   ```

3. **Run Desktop Application:**
   ```bash
   java -cp bin Main
   ```

4. **(Optional) Open Web Application Interface:**
   Double-click `web_app.html` or open it directly in any modern browser.

---

## 📸 Screenshots

*(Application screenshots are stored under `screenshots/` directory)*

- **Login Panel:** `screenshots/login.png`
- **Dashboard Menu:** `screenshots/dashboard.png`
- **Cash Withdrawal:** `screenshots/withdraw.png`
- **Transaction History:** `screenshots/history.png`

---

## 🔮 Future Improvements

1. **Database Integration:** Transition from CSV files to PostgreSQL / H2 using Spring Data JPA.
2. **Two-Factor Authentication (2FA):** OTP confirmation via Email/SMS simulation.
3. **Multi-Currency Support:** Real-time FX conversion for international transactions.

---

## 👤 Author

- **Name:** Yagnesh Patel
- **Internship:** Oasis Infobyte Java Development Intern
- **GitHub:** [@yagnesh747](https://github.com/yagnesh747)

---

## 📜 License

This project is open-source under the [MIT License](LICENSE).
