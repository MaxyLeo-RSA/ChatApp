package chatapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

public class ChatApp {
    
    private Registration currentUser;
    private boolean isLoggedIn = false;
    private int maxMessages = 0;
    private int messagesSent = 0;

    // Use the array manager for array operations
    private ChatAppArrayManager arrayManager = new ChatAppArrayManager();

    // Dark theme colors
    private final Color BACKGROUND_COLOR = Color.BLACK;
    private final Color TEXT_COLOR = new Color(173, 216, 230);
    private final Color ACCENT_COLOR = new Color(0, 0, 139);
    private final Color INPUT_BACKGROUND = new Color(30, 30, 30);
    private final Color BORDER_COLOR = new Color(70, 130, 180);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatApp app = new ChatApp();
            app.showWelcomeScreen();
        });
    }

    private void setupDarkTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("OptionPane.background", BACKGROUND_COLOR);
            UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
            UIManager.put("OptionPane.messageBackground", BACKGROUND_COLOR);
            UIManager.put("TextField.background", INPUT_BACKGROUND);
            UIManager.put("TextField.foreground", TEXT_COLOR);
            UIManager.put("TextField.caretForeground", TEXT_COLOR);
            UIManager.put("TextField.border", BorderFactory.createLineBorder(BORDER_COLOR));
            UIManager.put("PasswordField.background", INPUT_BACKGROUND);
            UIManager.put("PasswordField.foreground", TEXT_COLOR);
            UIManager.put("PasswordField.caretForeground", TEXT_COLOR);
            UIManager.put("PasswordField.border", BorderFactory.createLineBorder(BORDER_COLOR));
            UIManager.put("TextArea.background", INPUT_BACKGROUND);
            UIManager.put("TextArea.foreground", TEXT_COLOR);
            UIManager.put("TextArea.caretForeground", TEXT_COLOR);
            UIManager.put("TextArea.border", BorderFactory.createLineBorder(BORDER_COLOR));
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("Label.background", BACKGROUND_COLOR);
            UIManager.put("Button.background", ACCENT_COLOR);
            UIManager.put("Button.foreground", TEXT_COLOR);
            UIManager.put("Button.border", BorderFactory.createLineBorder(BORDER_COLOR));
            UIManager.put("Button.focus", BORDER_COLOR);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWelcomeScreen() {
        setupDarkTheme();
        
        while (true) {
            JPanel panel = createStyledPanel();
            panel.setLayout(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel("Welcome to QuickChat", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(TEXT_COLOR);

            JTextArea menuArea = createStyledTextArea();
            menuArea.setEditable(false);
            menuArea.setFont(new Font("Consolas", Font.PLAIN, 14));
            menuArea.setText(
                "Please select an option:\n\n" +
                "1. Register\n" +
                "2. Login\n" +
                "3. Quit\n\n" +
                "Enter your choice (1-3):"
            );
            menuArea.setBackground(BACKGROUND_COLOR);

            JTextField choiceField = createStyledTextField();
            choiceField.setFont(new Font("Consolas", Font.PLAIN, 14));

            panel.add(titleLabel, BorderLayout.NORTH);
            panel.add(menuArea, BorderLayout.CENTER);
            panel.add(choiceField, BorderLayout.SOUTH);

            int result = JOptionPane.showConfirmDialog(null, panel, 
                "QuickChat Welcome", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                int confirm = JOptionPane.showConfirmDialog(null,
                    createStyledMessage("Are you sure you want to quit?"),
                    "Confirm Quit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    showStyledMessage("Thank you for using QuickChat");
                    System.exit(0);
                }
                continue;
            }

            String choice = choiceField.getText().trim();
            
            switch (choice) {
                case "1":
                    showRegistrationStepByStep();
                    break;
                case "2":
                    showLoginStepByStep();
                    break;
                case "3":
                    showStyledMessage("Thank you for using QuickChat");
                    System.exit(0);
                    break;
                default:
                    showStyledMessage("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private void showStyledMessage(String message) {
        JTextArea textArea = createStyledTextArea();
        textArea.setText(message);
        textArea.setEditable(false);
        textArea.setBackground(BACKGROUND_COLOR);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        
        JOptionPane.showMessageDialog(null, scrollPane, "QuickChat", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createStyledMessage(String message) {
        JPanel panel = createStyledPanel();
        JTextArea textArea = createStyledTextArea();
        textArea.setText(message);
        textArea.setEditable(false);
        textArea.setBackground(BACKGROUND_COLOR);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(textArea);
        return panel;
    }

    private void showRegistrationStepByStep() {
        Registration user = new Registration();
        
        while (true) {
            String firstName = showStyledInputDialog(
                "Registration Step 1/5\n\nEnter your First Name:",
                "Registration - First Name");
            if (firstName == null) return;
            firstName = firstName.trim();
            if (firstName.isEmpty()) {
                showStyledMessage("First name cannot be empty.");
                continue;
            }
            user.firstName = firstName;
            break;
        }
        
        while (true) {
            String lastName = showStyledInputDialog(
                "Registration Step 2/5\n\nEnter your Last Name:",
                "Registration - Last Name");
            if (lastName == null) return;
            lastName = lastName.trim();
            if (lastName.isEmpty()) {
                showStyledMessage("Last name cannot be empty.");
                continue;
            }
            user.lastName = lastName;
            break;
        }
        
        while (true) {
            String username = showStyledInputDialog(
                "Registration Step 3/5\n\nEnter your Username (must contain '_' and be ≤ 5 characters):",
                "Registration - Username");
            if (username == null) return;
            username = username.trim();
            user.username = username;
            if (!user.checkUserName()) {
                showStyledMessage("Invalid username! Must contain underscore (_) and be 5 characters or less.");
                continue;
            }
            break;
        }
        
        while (true) {
            String password = showStyledInputDialog(
                "Registration Step 4/5\n\nEnter your Password:\n• At least 8 characters\n• One uppercase letter\n• One digit\n• One special character",
                "Registration - Password");
            if (password == null) return;
            user.password = password;
            if (!user.checkPasswordComplexity()) {
                showStyledMessage("Password does not meet complexity requirements!");
                continue;
            }
            break;
        }
        
        while (true) {
            String cellNumber = showStyledInputDialog(
                "Registration Step 5/5\n\nEnter your South African Cell Number:\nExamples: +27612345678 or 0712345678",
                "Registration - Cell Number");
            if (cellNumber == null) return;
            cellNumber = cellNumber.trim();
            user.cellNumber = cellNumber;
            if (!user.checkCellPhoneNumber()) {
                showStyledMessage("Invalid South African cell number!\nAccepted formats: +27612345678 or 0712345678");
                continue;
            }
            break;
        }
        
        this.currentUser = user;
        showStyledMessage("Registration Successful!\nYou can now login with your credentials.");
    }

    private String showStyledInputDialog(String message, String title) {
        JPanel panel = createStyledPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea messageArea = createStyledTextArea();
        messageArea.setText(message);
        messageArea.setEditable(false);
        messageArea.setBackground(BACKGROUND_COLOR);

        JTextField inputField = createStyledTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 14));

        panel.add(messageArea, BorderLayout.CENTER);
        panel.add(inputField, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(null, panel, title, 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            return inputField.getText();
        }
        return null;
    }

    private void showLoginStepByStep() {
        if (currentUser == null) {
            showStyledMessage("Please register first before logging in.");
            return;
        }
        
        String username = showStyledInputDialog("Login Step 1/2\n\nEnter your Username:", "Login - Username");
        if (username == null) return;
        
        String password = showStyledInputDialog("Login Step 2/2\n\nEnter your Password:", "Login - Password");
        if (password == null) return;
        
        username = username.trim();
        password = password.trim();
        
        if (currentUser.username.equals(username) && currentUser.password.equals(password)) {
            isLoggedIn = true;
            showStyledMessage("Login Successful!\nWelcome " + currentUser.firstName + " " + currentUser.lastName);
            loadStoredMessagesFromJSON();
            askForMessageCount();
        } else {
            showStyledMessage("Login Failed!\nUsername or password incorrect.");
        }
    }

    private void askForMessageCount() {
        while (true) {
            String input = showStyledInputDialog("How many messages do you wish to enter?", "Message Count");
            if (input == null) return;
            
            try {
                maxMessages = Integer.parseInt(input.trim());
                if (maxMessages <= 0) {
                    showStyledMessage("Please enter a positive number");
                    continue;
                }
                messagesSent = 0;
                showMainMenu();
                break;
            } catch (NumberFormatException e) {
                showStyledMessage("Please enter a valid number");
            }
        }
    }

    private void showMainMenu() {
        while (true) {
            String menuText = "QuickChat Main Menu\n\n" +
                "Welcome " + currentUser.firstName + " " + currentUser.lastName + "\n" +
                "Messages: " + messagesSent + "/" + maxMessages + " sent\n\n" +
                "Please select an option:\n" +
                "1. Send Messages\n" +
                "2. Show Recent Messages\n" +
                "3. View Stored Messages\n" +
                "4. Array Operations\n" +
                "5. Logout\n" +
                "6. Quit\n\n" +
                "Enter your choice (1-6):";
            
            String choice = showStyledInputDialog(menuText, "Main Menu");
            if (choice == null) continue;
            
            choice = choice.trim();
            
            switch (choice) {
                case "1":
                    if (messagesSent >= maxMessages) {
                        showStyledMessage("You have reached your limit of " + maxMessages + " messages.");
                    } else {
                        sendMessageStepByStep();
                    }
                    break;
                case "2":
                    showStyledMessage("Coming Soon");
                    break;
                case "3":
                    viewStoredMessages();
                    break;
                case "4":
                    showArrayOperationsMenu();
                    break;
                case "5":
                    int confirm = JOptionPane.showConfirmDialog(null,
                        createStyledMessage("Are you sure you want to logout?"),
                        "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        isLoggedIn = false;
                        currentUser = null;
                        maxMessages = 0;
                        messagesSent = 0;
                        arrayManager = new ChatAppArrayManager(); // Reset arrays
                        showWelcomeScreen();
                        return;
                    }
                    break;
                case "6":
                    confirm = JOptionPane.showConfirmDialog(null,
                        createStyledMessage("Are you sure you want to quit?\nTotal messages sent: " + Message.returnTotalMessages()),
                        "Confirm Quit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        showStyledMessage("Thank you for using QuickChat\nTotal messages sent: " + Message.returnTotalMessages());
                        System.exit(0);
                    }
                    break;
                default:
                    showStyledMessage("Invalid choice. Please enter 1-6.");
            }
        }
    }

    private void sendMessageStepByStep() {
        Message message = new Message();
        
        while (true) {
            String recipient = showStyledInputDialog(
                "Send Message Step 1/2\n\nMessages sent: " + messagesSent + "/" + maxMessages + 
                "\n\nEnter Recipient South African Cell Number:\nExamples: +27612345678 or 0712345678",
                "Send Message - Recipient");
            if (recipient == null) return;
            recipient = recipient.trim();
            
            if (message.checkRecipientCell(recipient) == 0) {
                showStyledMessage("Invalid South African cell number!\nAccepted formats: +27612345678 or 0712345678");
                continue;
            }
            message.setRecipient(recipient);
            break;
        }
        
        while (true) {
            String messageText = showStyledInputDialog(
                "Send Message Step 2/2\n\nMessages sent: " + messagesSent + "/" + maxMessages + 
                "\nRecipient: " + message.getRecipient() + "\n\nEnter your message (max 250 characters):",
                "Send Message - Content");
            if (messageText == null) return;
            messageText = messageText.trim();
            
            if (messageText.isEmpty()) {
                showStyledMessage("Please enter a message");
                continue;
            }
            if (messageText.length() > 250) {
                showStyledMessage("Please enter a message of less than 250 characters.");
                continue;
            }
            message.setMessage(messageText);
            break;
        }
        
        while (true) {
            String actionChoice = showStyledInputDialog(
                "Message Ready to Send\n\nRecipient: " + message.getRecipient() + 
                "\nMessage: " + message.getMessage() + "\nMessage Hash: " + message.getMessageHash() + 
                "\n\nChoose an action:\n1. Send Message\n2. Discard Message\n3. Store Message for Later\n\nEnter your choice (1-3):",
                "Message Action");
            if (actionChoice == null) continue;
            
            actionChoice = actionChoice.trim();
            String result;
            
            switch (actionChoice) {
                case "1":
                    result = message.sentMessage(1);
                    addToSentMessages(message);
                    messagesSent++;
                    break;
                case "2":
                    result = message.sentMessage(2);
                    addToDisregardedMessages(message);
                    break;
                case "3":
                    result = message.sentMessage(3);
                    addToStoredMessages(message);
                    break;
                default:
                    showStyledMessage("Invalid choice. Please enter 1, 2, or 3.");
                    continue;
            }
            
            String messageDetails = "Message Details:\n---------------\n" +
                "Message ID: " + message.getMessageID() + "\n" +
                "Message Hash: " + message.getMessageHash() + "\n" +
                "Recipient: " + message.getRecipient() + "\n" +
                "Message: " + message.getMessage() + "\n" +
                "Status: " + message.getStatus() + "\n\n" +
                "Result: " + result + "\n\n" +
                "Total messages sent this session: " + Message.returnTotalMessages();
            
            showStyledMessage(messageDetails);
            break;
        }
    }

    private void viewStoredMessages() {
        String messagesText = JSONHandler.getAllMessagesFormatted();
        JTextArea textArea = createStyledTextArea();
        textArea.setText(messagesText);
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.getViewport().setBackground(INPUT_BACKGROUND);
        
        JOptionPane.showMessageDialog(null, scrollPane, "Stored Messages", JOptionPane.PLAIN_MESSAGE);
    }

    private void loadStoredMessagesFromJSON() {
        arrayManager.storedMessages.clear();
        // FIXED: Now using ArrayList instead of List
        ArrayList<Message> messages = JSONHandler.readAllMessages();
        for (Message message : messages) {
            if ("stored".equals(message.getStatus())) {
                arrayManager.addToStoredMessages(message);
            } else if ("sent".equals(message.getStatus())) {
                arrayManager.addToSentMessages(message);
            } else if ("discarded".equals(message.getStatus())) {
                arrayManager.addToDisregardedMessages(message);
            }
        }
    }

    // Array management methods
    private void addToSentMessages(Message message) {
        arrayManager.addToSentMessages(message);
    }

    private void addToDisregardedMessages(Message message) {
        arrayManager.addToDisregardedMessages(message);
    }

    private void addToStoredMessages(Message message) {
        arrayManager.addToStoredMessages(message);
    }

    private void showArrayOperationsMenu() {
        while (true) {
            String menuText = "Array Operations Menu\n\n" +
                "Array Statistics:\n" +
                "• Sent Messages: " + arrayManager.getSentMessages().size() + "\n" +
                "• Disregarded Messages: " + arrayManager.getDisregardedMessages().size() + "\n" +
                "• Stored Messages: " + arrayManager.getStoredMessages().size() + "\n" +
                "• Message Hashes: " + arrayManager.getMessageHashes().size() + "\n" +
                "• Message IDs: " + arrayManager.getMessageIDs().size() + "\n\n" +
                "Please select an operation:\n" +
                "1. Display sender and recipient of all sent messages\n" +
                "2. Display the longest sent message\n" +
                "3. Search for message by ID\n" +
                "4. Search messages by recipient\n" +
                "5. Delete message by hash\n" +
                "6. Display full sent messages report\n" +
                "7. Back to Main Menu\n\n" +
                "Enter your choice (1-7):";
            
            String choice = showStyledInputDialog(menuText, "Array Operations");
            if (choice == null) continue;
            
            choice = choice.trim();
            
            switch (choice) {
                case "1":
                    displaySendersAndRecipients();
                    break;
                case "2":
                    displayLongestMessage();
                    break;
                case "3":
                    searchMessageByID();
                    break;
                case "4":
                    searchMessagesByRecipient();
                    break;
                case "5":
                    deleteMessageByHash();
                    break;
                case "6":
                    displayFullReport();
                    break;
                case "7":
                    return;
                default:
                    showStyledMessage("Invalid choice. Please enter 1-7.");
            }
        }
    }

    private void displaySendersAndRecipients() {
        String result = arrayManager.displaySendersAndRecipients(currentUser.firstName + " " + currentUser.lastName);
        showStyledMessage(result);
    }

    private void displayLongestMessage() {
        Message longest = arrayManager.findLongestMessage();
        if (longest == null) {
            showStyledMessage("No sent messages found.");
            return;
        }
        
        String result = "Longest Sent Message:\n" +
            "====================\n\n" +
            "Length: " + longest.getMessage().length() + " characters\n" +
            "Recipient: " + longest.getRecipient() + "\n" +
            "Message ID: " + longest.getMessageID() + "\n" +
            "Message: " + longest.getMessage() + "\n" +
            "Message Hash: " + longest.getMessageHash();
        
        showStyledMessage(result);
    }

    private void searchMessageByID() {
        String searchID = showStyledInputDialog("Enter Message ID to search:", "Search by Message ID");
        if (searchID == null) return;
        
        searchID = searchID.trim();
        Message foundMessage = arrayManager.searchMessageByID(searchID);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Search Results for Message ID: ").append(searchID).append("\n");
        sb.append("================================\n\n");
        
        if (foundMessage != null) {
            sb.append("Message Found:\n");
            sb.append("  Recipient: ").append(foundMessage.getRecipient()).append("\n");
            sb.append("  Message: ").append(foundMessage.getMessage()).append("\n");
            sb.append("  Status: ").append(foundMessage.getStatus()).append("\n");
            sb.append("  Message Hash: ").append(foundMessage.getMessageHash()).append("\n");
        } else {
            sb.append("No message found with ID: ").append(searchID);
        }
        
        showStyledMessage(sb.toString());
    }

    private void searchMessagesByRecipient() {
        String recipient = showStyledInputDialog("Enter recipient cell number to search:", "Search by Recipient");
        if (recipient == null) return;
        
        recipient = recipient.trim();
        ArrayList<Message> results = arrayManager.searchMessagesByRecipient(recipient);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Messages sent to: ").append(recipient).append("\n");
        sb.append("======================\n\n");
        
        if (results.isEmpty()) {
            sb.append("No messages found for recipient: ").append(recipient);
        } else {
            for (int i = 0; i < results.size(); i++) {
                Message msg = results.get(i);
                sb.append("Message ").append(i + 1).append(":\n");
                sb.append("  Message ID: ").append(msg.getMessageID()).append("\n");
                sb.append("  Message: ").append(msg.getMessage()).append("\n");
                sb.append("  Message Hash: ").append(msg.getMessageHash()).append("\n");
                sb.append("  Status: ").append(msg.getStatus()).append("\n");
                sb.append("-----------------\n");
            }
        }
        
        showStyledMessage(sb.toString());
    }

    private void deleteMessageByHash() {
        String hash = showStyledInputDialog("Enter Message Hash to delete:", "Delete by Hash");
        if (hash == null) return;
        
        hash = hash.trim();
        boolean deleted = arrayManager.deleteMessageByHash(hash);
        
        if (deleted) {
            // Also delete from JSON file
            // We need to find the message ID to delete from JSON
            for (Message msg : arrayManager.getSentMessages()) {
                if (msg.getMessageHash().equals(hash)) {
                    JSONHandler.deleteMessage(msg.getMessageID());
                    break;
                }
            }
            for (Message msg : arrayManager.getStoredMessages()) {
                if (msg.getMessageHash().equals(hash)) {
                    JSONHandler.deleteMessage(msg.getMessageID());
                    break;
                }
            }
            
            showStyledMessage("Message with hash '" + hash + "' has been successfully deleted from arrays and JSON file.");
        } else {
            showStyledMessage("No message found with hash: " + hash);
        }
    }

    private void displayFullReport() {
        String report = arrayManager.generateFullReport(currentUser.firstName + " " + currentUser.lastName);
        
        JTextArea textArea = createStyledTextArea();
        textArea.setText(report);
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.getViewport().setBackground(INPUT_BACKGROUND);
        
        JOptionPane.showMessageDialog(null, scrollPane, "Full Sent Messages Report", JOptionPane.PLAIN_MESSAGE);
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setForeground(TEXT_COLOR);
        return panel;
    }

    private JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setBackground(INPUT_BACKGROUND);
        textArea.setForeground(TEXT_COLOR);
        textArea.setCaretColor(TEXT_COLOR);
        textArea.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return textArea;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(INPUT_BACKGROUND);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(TEXT_COLOR);
        textField.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return textField;
    }
}