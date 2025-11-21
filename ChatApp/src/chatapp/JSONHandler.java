package chatapp;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class JSONHandler {
    private static final String FILE_NAME = "messages.json";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Saves a message object to the JSON file
    public static synchronized void storeMessage(Message message) {
        try {
            ArrayList<Message> existingMessages = readAllMessages();
            existingMessages.add(message);
            writeMessagesToFile(existingMessages);
            
        } catch (IOException e) {
            System.err.println("Error saving message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ChatGPT: This method reads JSON file into arrays - using JSONHandler class
    // Updated to return ArrayList for easier use in ChatApp
    public static synchronized ArrayList<Message> readAllMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            return messages;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line.trim());
            }
            
            String fullJson = jsonContent.toString();
            if (fullJson.isEmpty() || fullJson.equals("[]")) {
                return messages;
            }
            
            messages = parseMessagesFromJson(fullJson);
            
        } catch (IOException e) {
            System.err.println("Error reading messages: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }

    // Parse JSON array into Message objects
    private static ArrayList<Message> parseMessagesFromJson(String json) {
        ArrayList<Message> messages = new ArrayList<>();
        
        try {
            String content = json.substring(1, json.length() - 1).trim();
            if (content.isEmpty()) {
                return messages;
            }
            
            List<String> messageStrings = splitJsonObjects(content);
            
            for (String messageStr : messageStrings) {
                Message message = parseSingleMessage(messageStr);
                if (message != null) {
                    messages.add(message);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
        
        return messages;
    }

    private static List<String> splitJsonObjects(String jsonArrayContent) {
        List<String> objects = new ArrayList<>();
        int braceCount = 0;
        StringBuilder currentObject = new StringBuilder();
        boolean inString = false;
        char prevChar = 0;
        
        for (int i = 0; i < jsonArrayContent.length(); i++) {
            char c = jsonArrayContent.charAt(i);
            
            if (c == '"' && prevChar != '\\') {
                inString = !inString;
            }
            
            if (!inString) {
                if (c == '{') {
                    braceCount++;
                    if (braceCount == 1) {
                        currentObject = new StringBuilder();
                    }
                } else if (c == '}') {
                    braceCount--;
                }
            }
            
            if (braceCount >= 1) {
                currentObject.append(c);
            }
            
            if (braceCount == 0 && currentObject.length() > 0) {
                objects.add(currentObject.toString());
                currentObject = new StringBuilder();
                
                while (i + 1 < jsonArrayContent.length() && 
                       (jsonArrayContent.charAt(i + 1) == ',' || 
                        Character.isWhitespace(jsonArrayContent.charAt(i + 1)))) {
                    i++;
                }
            }
            
            prevChar = c;
        }
        
        return objects;
    }

    private static Message parseSingleMessage(String jsonObject) {
        try {
            String messageID = extractStringValue(jsonObject, "messageID");
            String recipient = extractStringValue(jsonObject, "recipient");
            String messageText = extractStringValue(jsonObject, "message");
            String messageHash = extractStringValue(jsonObject, "messageHash");
            String status = extractStringValue(jsonObject, "status");
            
            Message message = new Message();
            message.setMessageID(messageID);
            message.setRecipient(recipient);
            message.setMessage(messageText);
            message.setMessageHash(messageHash);
            message.setStatus(status);
            
            return message;
            
        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
            return null;
        }
    }

    private static String extractStringValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return "";
            
            int valueStart = keyIndex + searchKey.length();
            int quoteStart = json.indexOf('"', valueStart);
            if (quoteStart == -1) return "";
            
            int quoteEnd = json.indexOf('"', quoteStart + 1);
            if (quoteEnd == -1) return "";
            
            String value = json.substring(quoteStart + 1, quoteEnd);
            return unescapeJSON(value);
            
        } catch (Exception e) {
            return "";
        }
    }

    private static synchronized void writeMessagesToFile(ArrayList<Message> messages) throws IOException {
        try (FileWriter file = new FileWriter(FILE_NAME)) {
            file.write("[\n");
            
            for (int i = 0; i < messages.size(); i++) {
                Message message = messages.get(i);
                file.write(createMessageJSON(message));
                
                if (i < messages.size() - 1) {
                    file.write(",");
                }
                file.write("\n");
            }
            
            file.write("]");
        }
    }

    private static String createMessageJSON(Message message) {
        StringBuilder json = new StringBuilder();
        json.append("  {\n");
        json.append("    \"messageID\": \"").append(escapeJSON(message.getMessageID())).append("\",\n");
        json.append("    \"messageCount\": ").append(message.getMessageCount()).append(",\n");
        json.append("    \"recipient\": \"").append(escapeJSON(message.getRecipient())).append("\",\n");
        json.append("    \"message\": \"").append(escapeJSON(message.getMessage())).append("\",\n");
        json.append("    \"messageHash\": \"").append(escapeJSON(message.getMessageHash())).append("\",\n");
        json.append("    \"status\": \"").append(escapeJSON(message.getStatus())).append("\",\n");
        json.append("    \"timestamp\": \"").append(DATE_FORMAT.format(new Date())).append("\"\n");
        json.append("  }");
        return json.toString();
    }

    public static synchronized boolean deleteMessage(String messageID) {
        try {
            ArrayList<Message> messages = readAllMessages();
            boolean removed = messages.removeIf(msg -> msg.getMessageID().equals(messageID));
            
            if (removed) {
                writeMessagesToFile(messages);
                return true;
            }
            return false;
            
        } catch (IOException e) {
            System.err.println("Error deleting message: " + e.getMessage());
            return false;
        }
    }

    public static String getAllMessagesFormatted() {
        try {
            ArrayList<Message> messages = readAllMessages();
            if (messages.isEmpty()) {
                return "No stored messages found.";
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("Stored Messages:\n");
            sb.append("================\n\n");
            
            for (int i = 0; i < messages.size(); i++) {
                Message message = messages.get(i);
                sb.append("Message ").append(i + 1).append(":\n");
                sb.append("  ID: ").append(message.getMessageID()).append("\n");
                sb.append("  To: ").append(message.getRecipient()).append("\n");
                sb.append("  Message: ").append(message.getMessage()).append("\n");
                sb.append("  Status: ").append(message.getStatus()).append("\n");
                sb.append("  Hash: ").append(message.getMessageHash()).append("\n");
                sb.append("  Count: ").append(message.getMessageCount()).append("\n");
                sb.append("-----------------\n");
            }
            
            return sb.toString();
            
        } catch (Exception e) {
            return "Error reading messages: " + e.getMessage();
        }
    }

    private static String escapeJSON(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    private static String unescapeJSON(String text) {
        if (text == null) return "";
        return text.replace("\\\"", "\"")
                  .replace("\\\\", "\\")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t")
                  .replace("\\b", "\b")
                  .replace("\\f", "\f");
    }
}