# GuardVault - Personal Finance and Goal Management System

## I. Project Overview

**GuardVault** is a personal finance management system that enables users to manage their finances by adding and withdrawing money, setting and tracking goals, and maintaining an emergency fund. The system allows users to create accounts, sign in, manage their balance, and set specific financial goals. It integrates several features that enhance security, such as phone number verification for withdrawals and random name generation for added security.

### Key Features:
- **User Account Management**: Create accounts, log in with secure passwords, and view account details.
- **Balance Management**: Add and withdraw funds from a user’s balance.
- **Goal Setting and Tracking**: Set financial goals, deposit money towards them, transfer funds between goals, and delete goals if needed.
- **Emergency Fund**: Users can create and manage an emergency fund.
- **Transaction History**: Track all actions performed by the user for transparency.
- **Security**: Each withdrawal requires a security phone number verification, enhancing the overall security of the system.

---

## II. Explanation of How OOP Principles Were Applied

The **GuardVault** system makes use of Object-Oriented Programming (OOP) principles such as Encapsulation, Inheritance, Polymorphism, and Abstraction to design and implement the system:

### 1. **Encapsulation**:
The system encapsulates the user's financial details (balance, emergency fund, transaction history) within the `User` class. Access to these details is controlled through getter methods and internal methods that ensure appropriate updates are made to the data (e.g., withdrawing funds, adding to emergency fund).

### 2. **Inheritance**:
The `User` class inherits from the `Account` class. The `Account` class handles user authentication (username and password validation) and provides a base structure for account-related operations. The `User` class builds upon this base by adding specific financial operations like managing balance and goals.

### 3. **Polymorphism**:
The system demonstrates polymorphism with the abstract method `displayAccountDetails()` in the `Account` class, which is overridden in the `User` class to display user-specific details. This method provides flexibility to extend the system in the future if different account types (e.g., business accounts) are introduced.

### 4. **Abstraction**:
The `Account` class abstracts common account details (username, password) and user-related operations (authentication). This allows users to interact with the system without needing to understand the underlying implementation of authentication, goal management, or balance management.

---

## III. Chosen SDG and Integration into the Project

This project is designed with **Sustainable Development Goal (SDG) 1: No Poverty** in mind. The financial tools provided by GuardVault empower users to manage their personal finances effectively, save for their future goals, and create an emergency fund. By encouraging users to save and manage their money wisely, the system aids in reducing financial insecurity, which is a key component of fighting poverty. Additionally, users can track their financial goals and progress, making it easier to plan for a stable financial future.

### How it contributes to SDG 1:
1. **Financial Empowerment**: GuardVault helps individuals develop better financial habits, which contributes to improving their financial security.
2. **Emergency Fund Management**: Users can set up an emergency fund, which helps provide financial stability during unforeseen circumstances.
3. **Goal Setting**: By allowing users to set financial goals, GuardVault promotes responsible money management, helping users work towards long-term financial security.

---

## IV. Instructions for Running the Program

### Prerequisites:
- **Java 8** or higher should be installed on your system.

### Steps to Run:

1. **Compile the Java files**:
    ```bash
    javac GuardVault.java
    ```

2. **Run the program**:
    ```bash
    java GuardVault
    ```

### Interacting with the Program:

Upon running the program, the user will be prompted with a menu to either sign up or log in.
After logging in successfully, users can access various functionalities such as adding balance, withdrawing funds, setting goals, and more.
The program uses a simple console interface with color-coded text to provide a user-friendly experience.

