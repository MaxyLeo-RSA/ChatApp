package chatapp;

import java.util.*;

public class ChatAppArrayManager {
    // Arrays for message management (made public for testing)
    public ArrayList<Message> sentMessages = new ArrayList<>();
    public ArrayList<Message> disregardedMessages = new ArrayList<>();
    public ArrayList<Message> storedMessages = new ArrayList<>();
    public ArrayList<String> messageHashes = new ArrayList<>();
    public ArrayList<String> messageIDs = new ArrayList<>();

    // Array management methods
    public void addToSentMessages(Message message) {
        sentMessages.add(message);
        messageHashes.add(message.getMessageHash());
        messageIDs.add(message.getMessageID());
    }

    public void addToDisregardedMessages(Message message) {
        disregardedMessages.add(message);
    }

    public void addToStoredMessages(Message message) {
        storedMessages.add(message);
        messageHashes.add(message.getMessageHash());
        messageIDs.add(message.getMessageID());
    }

    // Array operation methods
    public String displaySendersAndRecipients(String senderName) {
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Senders and Recipients of Sent Messages:\n");
        sb.append("=======================================\n\n");
        
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            sb.append("Message ").append(i + 1).append(":\n");
            sb.append("  Sender: ").append(senderName).append("\n");
            sb.append("  Recipient: ").append(msg.getRecipient()).append("\n");
            sb.append("  Message ID: ").append(msg.getMessageID()).append("\n");
            sb.append("-----------------\n");
        }
        
        return sb.toString();
    }

    public Message findLongestMessage() {
        if (sentMessages.isEmpty()) {
            return null;
        }
        
        Message longest = sentMessages.get(0);
        for (Message msg : sentMessages) {
            if (msg.getMessage().length() > longest.getMessage().length()) {
                longest = msg;
            }
        }
        return longest;
    }

    public ArrayList<Message> searchMessagesByRecipient(String recipient) {
        ArrayList<Message> results = new ArrayList<>();
        
        // Search in sent messages
        for (Message msg : sentMessages) {
            if (recipient.equals(msg.getRecipient())) {
                results.add(msg);
            }
        }
        
        // Search in stored messages
        for (Message msg : storedMessages) {
            if (recipient.equals(msg.getRecipient())) {
                results.add(msg);
            }
        }
        
        return results;
    }

    public boolean deleteMessageByHash(String hash) {
        boolean deleted = false;
        
        // Search and remove from sent messages
        Iterator<Message> sentIterator = sentMessages.iterator();
        while (sentIterator.hasNext()) {
            Message msg = sentIterator.next();
            if (msg.getMessageHash().equals(hash)) {
                sentIterator.remove();
                messageHashes.remove(hash);
                messageIDs.remove(msg.getMessageID());
                deleted = true;
                break;
            }
        }
        
        // Search and remove from stored messages
        if (!deleted) {
            Iterator<Message> storedIterator = storedMessages.iterator();
            while (storedIterator.hasNext()) {
                Message msg = storedIterator.next();
                if (msg.getMessageHash().equals(hash)) {
                    storedIterator.remove();
                    messageHashes.remove(hash);
                    messageIDs.remove(msg.getMessageID());
                    deleted = true;
                    break;
                }
            }
        }
        
        return deleted;
    }

    public Message searchMessageByID(String messageID) {
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(messageID)) {
                return msg;
            }
        }
        
        for (Message msg : storedMessages) {
            if (msg.getMessageID().equals(messageID)) {
                return msg;
            }
        }
        
        return null;
    }

    public String generateFullReport(String senderName) {
        if (sentMessages.isEmpty()) {
            return "No sent messages found for report.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("FULL SENT MESSAGES REPORT\n");
        sb.append("=========================\n\n");
        
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            sb.append("MESSAGE ").append(i + 1).append(":\n");
            sb.append("  Message ID: ").append(msg.getMessageID()).append("\n");
            sb.append("  Message Hash: ").append(msg.getMessageHash()).append("\n");
            sb.append("  Sender: ").append(senderName).append("\n");
            sb.append("  Recipient: ").append(msg.getRecipient()).append("\n");
            sb.append("  Message: ").append(msg.getMessage()).append("\n");
            sb.append("  Message Length: ").append(msg.getMessage().length()).append(" characters\n");
            sb.append("  Status: ").append(msg.getStatus()).append("\n");
            sb.append("  Message Count: ").append(msg.getMessageCount()).append("\n");
            sb.append("====================================\n\n");
        }
        
        // Add summary
        sb.append("SUMMARY:\n");
        sb.append("--------\n");
        sb.append("Total Sent Messages: ").append(sentMessages.size()).append("\n");
        sb.append("Total Characters Sent: ").append(calculateTotalCharacters()).append("\n");
        sb.append("Average Message Length: ").append(String.format("%.2f", calculateAverageLength())).append(" characters\n");
        
        return sb.toString();
    }

    private int calculateTotalCharacters() {
        int total = 0;
        for (Message msg : sentMessages) {
            total += msg.getMessage().length();
        }
        return total;
    }

    private double calculateAverageLength() {
        if (sentMessages.isEmpty()) return 0;
        return (double) calculateTotalCharacters() / sentMessages.size();
    }

    // Getters for testing
    public ArrayList<Message> getSentMessages() { return sentMessages; }
    public ArrayList<Message> getStoredMessages() { return storedMessages; }
    public ArrayList<String> getMessageHashes() { return messageHashes; }
    public ArrayList<String> getMessageIDs() { return messageIDs; }
    public ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
}