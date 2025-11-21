package chatapp;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Message {
    private String messageID;
    private int messageCount;
    private String recipient;
    private String message;
    private String messageHash;
    private String status;
    private static int totalMessagesSent = 0;
    private static int messageCounter = 0;
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1000);

    public Message() {
        this.messageID = generateMessageID();
        this.messageCount = ++messageCounter;
        this.status = "pending";
        
        if (!checkMessageID()) {
            throw new IllegalStateException("Failed to generate valid message ID");
        }
    }

    private String generateMessageID() {
        long timestamp = System.currentTimeMillis() % 10000000000L;
        int sequence = SEQUENCE.getAndIncrement() % 10000;
        long id = (timestamp * 10000 + sequence) % 10000000000L;
        
        String idStr = String.valueOf(id);
        while (idStr.length() < 10) {
            idStr = "0" + idStr;
        }
        
        if (!isValidMessageID(idStr)) {
            return generateRandomMessageID();
        }
        
        return idStr;
    }

    private String generateRandomMessageID() {
        Random rand = new Random();
        String idStr;
        do {
            long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
            idStr = String.valueOf(id);
        } while (!isValidMessageID(idStr));
        
        return idStr;
    }

    public boolean checkMessageID() {
        return isValidMessageID(this.messageID);
    }

    public static boolean isValidMessageID(String messageID) {
        if (messageID == null) {
            return false;
        }
        
        if (messageID.length() != 10) {
            return false;
        }
        
        if (!messageID.matches("\\d{10}")) {
            return false;
        }
        
        if (messageID.startsWith("0")) {
            return false;
        }
        
        return true;
    }

    // South African phone number validation
    public int checkRecipientCell(String recipient) {
        if (recipient == null || recipient.isEmpty()) {
            return 0;
        }
        
        // Format 1: +27XXXXXXXXX (12 characters total)
        if (recipient.startsWith("+27") && recipient.length() == 12) {
            String numberPart = recipient.substring(3);
            if (numberPart.matches("\\d{9}")) {
                char firstDigit = numberPart.charAt(0);
                if (firstDigit == '6' || firstDigit == '7' || firstDigit == '8') {
                    return 1;
                }
            }
        }
        
        // Format 2: 07XXXXXXXX (10 characters - local format)
        if (recipient.startsWith("07") && recipient.length() == 10) {
            String numberPart = recipient.substring(2);
            if (numberPart.matches("\\d{8}")) {
                return 1;
            }
        }
        
        // Format 3: 27XXXXXXXXX (11 characters - without +)
        if (recipient.startsWith("27") && recipient.length() == 11) {
            String numberPart = recipient.substring(2);
            if (numberPart.matches("\\d{9}")) {
                char firstDigit = numberPart.charAt(0);
                if (firstDigit == '6' || firstDigit == '7' || firstDigit == '8') {
                    return 1;
                }
            }
        }
        
        // Format 4: 0XXXXXXXXX (10 characters - other local formats)
        if (recipient.startsWith("0") && recipient.length() == 10) {
            String numberPart = recipient.substring(1);
            if (numberPart.matches("\\d{9}")) {
                return 1;
            }
        }
        
        return 0;
    }

    // Automatically formats phone numbers to international format
    public String formatSouthAfricanNumber(String number) {
        if (number == null) return number;
        
        // If it's in 07 format, convert to +27
        if (number.startsWith("07") && number.length() == 10) {
            return "+27" + number.substring(2);
        }
        
        // If it's in 0 format (other local), convert to +27
        if (number.startsWith("0") && number.length() == 10 && !number.startsWith("07")) {
            return "+27" + number.substring(1);
        }
        
        // If it's in 27 format (without +), add the +
        if (number.startsWith("27") && number.length() == 11 && !number.startsWith("+")) {
            return "+" + number;
        }
        
        // If it's already in +27 format, return as is
        if (number.startsWith("+27") && number.length() == 12) {
            return number;
        }
        
        return number;
    }

    public String createMessageHash() {
        if (messageID == null || !checkMessageID()) {
            throw new IllegalStateException("Cannot create hash without valid message ID");
        }
        
        String firstTwo = messageID.substring(0, 2);
        
        String[] words = message != null ? message.split(" ") : new String[0];
        String firstWord = words.length > 0 ? words[0].toUpperCase() : "";
        String lastWord = words.length > 1 ? words[words.length - 1].toUpperCase() : firstWord;
        
        return firstTwo + ":" + messageCount + ":" + firstWord + lastWord;
    }

    public String sentMessage(int choice) {
        if (!checkMessageID()) {
            return "Error: Invalid message ID";
        }
        
        switch (choice) {
            case 1:
                totalMessagesSent++;
                this.status = "sent";
                JSONHandler.storeMessage(this);
                return "Message sent successfully!";
            case 2:
                this.status = "discarded";
                return "Message discarded";
            case 3:
                this.status = "stored";
                JSONHandler.storeMessage(this);
                return "Message stored successfully!";
            default:
                return "Invalid option";
        }
    }

    public String printMessages() {
        String idStatus = checkMessageID() ? "VALID" : "INVALID";
        return "MessageID: " + messageID + " [" + idStatus + "]" +
               "\nMessage Hash: " + messageHash + 
               "\nRecipient: " + recipient + 
               "\nMessage: " + message +
               "\nStatus: " + status;
    }

    public String getMessageID() { return messageID; }
    public void setMessageID(String messageID) {
        if (!isValidMessageID(messageID)) {
            throw new IllegalArgumentException("Invalid message ID: must be exactly 10 digits, cannot start with 0, and contain only numbers");
        }
        this.messageID = messageID;
    }
    
    public int getMessageCount() { return messageCount; }
    public void setMessageCount(int messageCount) { this.messageCount = messageCount; }
    
    public String getRecipient() { return recipient; }
    
    // CHANGED: Now automatically formats the recipient when setting
    public void setRecipient(String recipient) { 
        // Validate first
        if (checkRecipientCell(recipient) == 0) {
            throw new IllegalArgumentException("Invalid South African cell number: " + recipient);
        }
        // Then format and store
        this.recipient = formatSouthAfricanNumber(recipient);
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { 
        this.message = message;
        this.messageHash = createMessageHash();
    }
    
    public String getMessageHash() { return messageHash; }
    public void setMessageHash(String messageHash) { this.messageHash = messageHash; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public static int returnTotalMessages() { return totalMessagesSent; }
}
