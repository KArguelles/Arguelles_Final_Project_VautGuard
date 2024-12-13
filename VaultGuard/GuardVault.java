import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// Abstraction
abstract class Account {
    private final String username;
    private final String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public abstract void displayAccountDetails(); // Polymorphism
}

// Inheritance and Encapsulation
class User extends Account {
    private double balance;
    private double emergencyFund;
    private final ArrayList<Goal> goals;
    private final ArrayList<String> transactionHistory;

    public User(String username, String password) {
        super(username, password);
        this.balance = 0.0;
        this.emergencyFund = 0.0;
        this.goals = new ArrayList<>();
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        balance += amount;
        logTransaction("Added " + amount + " to balance.");
    }

    public void withdrawBalance(double amount) {
        if (amount > balance) {
            System.out.println("\033[35mInsufficient balance.\033[0m");
        } else {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("\033[35mEnter a phone number for security verification: \033[0m"); 
                String securityCode = scanner.nextLine();
                if (securityCode.length() == 11 && securityCode.matches("\\d+")) {
                    balance -= amount;
                    String senderName = generateRandomName(); // Random name for added security
                    System.out.println("\033[34mYou have withdrawn " + amount + ". Sent by: " + senderName + "\033[0m"); 
                    logTransaction("Withdrew " + amount + " from balance.");
                } else {
                    System.out.println("\033[35mInvalid security code. Withdrawal failed.\033[0m"); 
                }
            } catch (Exception e) {
                System.out.println("\033[35mAn error occurred while processing the withdrawal.\033[0m"); 
            }
        }
    }

    private String generateRandomName() {
        String[] names = {"Alice", "Bob", "Charlie", "David", "Eve"};
        Random rand = new Random();
        return names[rand.nextInt(names.length)];
    }

    public void addEmergencyFund(double amount) {
        if (amount > balance) {
            System.out.println("\033[35mInsufficient balance to add to emergency fund.\033[0m"); // Purple color
        } else {
            balance -= amount;
            emergencyFund += amount;
            System.out.println("\033[34mAdded " + amount + " to Emergency Fund.\033[0m"); 
            logTransaction("Added " + amount + " to Emergency Fund.");
        }
    }

    public double getEmergencyFund() {
        return emergencyFund;
    }

    public void addGoal(String goalName, double targetAmount) {
        goals.add(new Goal(goalName, targetAmount));
        System.out.println("\033[34mGoal added: " + goalName + "\033[0m"); 
        logTransaction("Goal '" + goalName + "' created with target " + targetAmount);
    }

    public void depositToGoal(String goalName, double amount) {
        for (Goal goal : goals) {
            if (goal.getGoalName().equalsIgnoreCase(goalName)) {
                if (amount > balance) {
                    System.out.println("\033[35mInsufficient balance to deposit.\033[0m");
                } else {
                    goal.save(amount);
                    balance -= amount;
                    System.out.println("\033[34mDeposited " + amount + " to goal: " + goalName + "\033[0m"); 
                    logTransaction("Deposited " + amount + " to goal: " + goalName);
                }
                return;
            }
        }
        System.out.println("\033[35mGoal not found.\033[0m"); 
    }

    public void viewGoals() {
        if (goals.isEmpty()) {
            System.out.println("\033[35mNo goals set.\033[0m"); 
        } else {
            System.out.println("\033[34mYour Goals:\033[0m"); 
            for (Goal goal : goals) {
                System.out.println(goal);
            }
        }
    }

    public void deleteGoal(String goalName) {
        for (Goal goal : goals) {
            if (goal.getGoalName().equalsIgnoreCase(goalName)) {
                double savedAmount = goal.getSavedAmount();
                balance += savedAmount;  // Transfer saved amount back to user's balance
                goals.remove(goal);  
                System.out.println("\033[34mGoal '" + goalName + "' has been deleted, and " + savedAmount + " has been returned to your balance.\033[0m"); 
                return;
            }
        }
        System.out.println("\033[35mGoal '" + goalName + "' not found.\033[0m");  
    }

    public void transferBetweenGoals(String fromGoal, String toGoal, double amount) {
        Goal source = null, target = null;
        for (Goal goal : goals) {
            if (goal.getGoalName().equalsIgnoreCase(fromGoal)) {
                source = goal;
            } else if (goal.getGoalName().equalsIgnoreCase(toGoal)) {
                target = goal;
            }
        }
        if (source != null && target != null && source.getSavedAmount() >= amount) {
            source.save(-amount);  // Reduce from source goal
            target.save(amount);   
            System.out.println("\033[34mTransferred " + amount + " from " + fromGoal + " to " + toGoal + "\033[0m"); 
            logTransaction("Transferred " + amount + " from " + fromGoal + " to " + toGoal);
        } else {
            System.out.println("\033[35mTransfer failed. Check goal names or amount.\033[0m"); 
        }
    }

    public void logTransaction(String transactionDetail) {
        transactionHistory.add(transactionDetail);
    }

    public void viewTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("\033[35mNo transactions yet.\033[0m"); 
        } else {
            System.out.println("\033[34mTransaction History:\033[0m"); 
            transactionHistory.forEach(System.out::println);
        }
    }

    @Override
    public void displayAccountDetails() {
        System.out.println("\033[35mUsername: " + getUsername() + "\033[0m"); 
        System.out.println("\033[35mBalance: " + balance + "\033[0m"); 
        System.out.println("\033[35mEmergency Fund: " + emergencyFund + "\033[0m"); 
    }
}

// Goal class (Encapsulation)
class Goal {
    private final String goalName;
    private final double targetAmount;
    private double savedAmount;

    public Goal(String goalName, double targetAmount) {
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.savedAmount = 0.0;
    }

    public void save(double amount) {
        if (savedAmount + amount > targetAmount) {
            System.out.println("\033[35mExceeds target amount. Adjusting saved amount to the target.\033[0m"); 
            savedAmount = targetAmount;
        } else if (savedAmount + amount < 0) {
            System.out.println("\033[35mCannot withdraw more than saved amount.\033[0m"); 
            savedAmount = 0;
        } else {
            savedAmount += amount;
        }
    }

    public String getGoalName() {
        return goalName;
    }

    public double getSavedAmount() {
        return savedAmount;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    @Override
    public String toString() {
        return "Goal: " + goalName + " | Target: " + targetAmount + " | Saved: " + savedAmount;
    }
}

public class GuardVault {
    private static final ArrayList<User> users = new ArrayList<>();
    private static User currentUser = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (currentUser == null) {
                System.out.println("\033[35m\n╔══════════════════════════════════════╗\033[0m"); 
                System.out.println("\033[37m║          WELCOME TO GUARDVAULT       ║\033[0m"); 
                System.out.println("\033[35m╠══════════════════════════════════════╣\033[0m"); 
                System.out.println("\033[35m║ 1. Sign Up                           ║\033[0m"); 
                System.out.println("\033[35m║ 2. Log In                            ║\033[0m"); 
                System.out.println("\033[35m║ 3. Exit                              ║\033[0m"); 
                System.out.println("\033[35m╚══════════════════════════════════════╝\033[0m"); 
                System.out.print("\033[36mChoose an option: \033[0m");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 

                switch (choice) {
                    case 1 -> signUp(scanner);
                    case 2 -> logIn(scanner);
                    case 3 -> {
                        System.out.println("\033[35mGoodbye!\033[0m"); 
                        return;
                    }
                    default -> System.out.println("\033[35mInvalid option. Please try again.\033[0m"); 
                }
            } else {
                userMenu(scanner);
            }
        }
    }

    private static void signUp(Scanner scanner) {
        System.out.print("\033[36mEnter a username: \033[0m"); 
        String username = scanner.nextLine();
        System.out.print("\033[36mEnter a password: \033[0m"); 
        String password = scanner.nextLine();   
        
        if (isStrongPassword(password)) {
            users.add(new User(username, password));
            System.out.println("\033[34mAccount created successfully!\033[0m"); 
        } else {
            System.out.println("\033[35mPassword too weak! Must be at least 8 characters, contain uppercase letters, numbers, and special symbols.\033[0m"); 
        }
    }

    private static boolean isStrongPassword(String password) {
        return password.length() > 8 && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*].*");
    }

    private static void logIn(Scanner scanner) {
        System.out.print("\033[36mEnter your username: \033[0m"); 
        String username = scanner.nextLine();
        System.out.print("\033[36mEnter your password: \033[0m"); 
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.validatePassword(password)) {
                currentUser = user;
                System.out.println("\033[34mLogged in successfully!\033[0m"); 
                return;
            }
        }
        System.out.println("\033[35mInvalid username or password.\033[0m"); 
    }

    private static void userMenu(Scanner scanner) {
        while (true) {
            System.out.println("\033[35m╔══════════════════════════════════════╗\033[0m");
            System.out.println("\033[37m║          ACCOUNT MANAGEMENT          ║\033[0m");
            System.out.println("\033[35m╠══════════════════════════════════════╣\033[0m");
            System.out.println("\033[35m║ 1. Add Balance                       ║\033[0m");
            System.out.println("\033[35m║ 2. Withdraw Balance                  ║\033[0m");
            System.out.println("\033[35m║ 3. View Account Details              ║\033[0m");
            System.out.println("\033[35m║ 4. View Transaction History          ║\033[0m");
            System.out.println("\033[35m║ 5. Log Out                           ║\033[0m");
            System.out.println("\033[35m╚══════════════════════════════════════╝\033[0m");

            System.out.println("\033[35m╔══════════════════════════════════════╗\033[0m");
            System.out.println("\033[37m║           GOAL MANAGEMENT            ║\033[0m");
            System.out.println("\033[35m╠══════════════════════════════════════╣\033[0m");
            System.out.println("\033[35m║ 6. Set a New Goal                    ║\033[0m");
            System.out.println("\033[35m║ 7. View All Goals                    ║\033[0m");
            System.out.println("\033[35m║ 8. Deposit to Goal                   ║\033[0m");
            System.out.println("\033[35m║ 9. Delete Goal                       ║\033[0m");
            System.out.println("\033[35m║ 10. Transfer Between Goals           ║\033[0m");
            System.out.println("\033[35m╚══════════════════════════════════════╝\033[0m");

            System.out.println("\033[35m╔══════════════════════════════════════╗\033[0m");
            System.out.println("\033[37m║           EMERGENCY FUND             ║\033[0m");
            System.out.println("\033[35m╠══════════════════════════════════════╣\033[0m");
            System.out.println("\033[35m║ 11. Add to Emergency Fund            ║\033[0m");
            System.out.println("\033[35m║ 12. View Emergency Fund              ║\033[0m");
            System.out.println("\033[35m╚══════════════════════════════════════╝\033[0m");
            System.out.print("\033[36mChoose an option: \033[0m"); 
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addBalance(scanner);
                case 2 -> withdrawBalance(scanner);
                case 3 -> currentUser.displayAccountDetails();
                case 4 -> currentUser.viewTransactionHistory();
                case 5 -> {
                    currentUser = null;
                    return;
                }
                case 6 -> setGoal(scanner);
                case 7 -> currentUser.viewGoals();
                case 8 -> depositToGoal(scanner);
                case 9 -> deleteGoal(scanner);
                case 10 -> transferBetweenGoals(scanner);
                case 11 -> addEmergencyFund(scanner);
                case 12 -> System.out.println("\033[34mEmergency Fund: " + currentUser.getEmergencyFund() + "\033[0m"); 
                default -> System.out.println("\033[35mInvalid option. Please try again.\033[0m"); 
            }
        }
    }

    private static void addBalance(Scanner scanner) {
        System.out.print("\033[36mEnter amount to add: \033[0m"); 
        double amount = scanner.nextDouble();
        currentUser.addBalance(amount);
        System.out.println("\033[34mAdded " + amount + " to balance.\033[0m"); 
    }

    private static void withdrawBalance(Scanner scanner) {
        System.out.print("\033[36mEnter amount to withdraw: \033[0m"); 
        double amount = scanner.nextDouble();
        scanner.nextLine();
        currentUser.withdrawBalance(amount);
    }
    private static void setGoal(Scanner scanner) {
        System.out.print("\033[36mEnter goal name: \033[0m"); 
        String goalName = scanner.nextLine();
        System.out.print("\033[36mEnter target amount: \033[0m"); 
        double targetAmount = scanner.nextDouble();
        currentUser.addGoal(goalName, targetAmount);
    }

    private static void depositToGoal(Scanner scanner) {
        System.out.print("\033[36mEnter goal name: \033[0m");
        String goalName = scanner.nextLine();
        System.out.print("\033[36mEnter amount to deposit: \033[0m");
        double amount = scanner.nextDouble();
        currentUser.depositToGoal(goalName, amount);
    }

    private static void deleteGoal(Scanner scanner) {
        System.out.print("\033[36mEnter goal name to delete: \033[0m"); 
        String goalName = scanner.nextLine();
        currentUser.deleteGoal(goalName);
    }

    private static void addEmergencyFund(Scanner scanner) {
        System.out.print("\033[36mEnter amount to add to Emergency Fund: \033[0m"); 
        double amount = scanner.nextDouble();
        currentUser.addEmergencyFund(amount);
    }

    private static void transferBetweenGoals(Scanner scanner) {
        System.out.print("\033[36mEnter source goal name: \033[0m"); 
        String fromGoal = scanner.nextLine();
        System.out.print("\033[36mEnter target goal name: \033[0m"); 
        String toGoal = scanner.nextLine();
        System.out.print("\033[36mEnter amount to transfer: \033[0m"); 
        double amount = scanner.nextDouble();
        currentUser.transferBetweenGoals(fromGoal, toGoal, amount);
    }
}
