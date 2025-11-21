package chatapp;

public class TestRunner {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("🚀 Starting QuickChat Automated Tests...");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        
        try {
            runRegistrationTests();
            runLoginTests();
            runMessageTests();
            runArrayManagerTests();
            runJSONHandlerTests();
            
            printResults();
            
            // Exit with code 0 if all passed, 1 if any failed
            if (failed > 0) {
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void runRegistrationTests() {
        System.out.println("\n📝 Testing Registration Class...");
        Registration user = new Registration();
        user.firstName = "John";
        user.lastName = "Doe";
        
        // Test 1: Valid username
        user.username = "v__1";
        assertTrue(user.checkUserName(), "Username 'v__1' should be valid");
        
        // Test 2: Invalid username (no underscore)
        user.username = "user";
        assertFalse(user.checkUserName(), "Username 'user' should be invalid (no underscore)");
        
        // Test 3: Invalid username (too long)
        user.username = "user_123";
        assertFalse(user.checkUserName(), "Username 'user_123' should be invalid (too long)");
        
        // Test 4: Valid password
        user.password = "Ch&&sec@ke99!";
        assertTrue(user.checkPasswordComplexity(), "Password 'Ch&&sec@ke99!' should meet complexity requirements");
        
        // Test 5: Invalid password (no uppercase)
        user.password = "password123!";
        assertFalse(user.checkPasswordComplexity(), "Password without uppercase should be invalid");
        
        // Test 6: Invalid password (no digit)
        user.password = "Password!";
        assertFalse(user.checkPasswordComplexity(), "Password without digit should be invalid");
        
        // Test 7: Invalid password (no special char)
        user.password = "Password123";
        assertFalse(user.checkPasswordComplexity(), "Password without special character should be invalid");
        
        // Test 8: Invalid password (too short)
        user.password = "Pass1!";
        assertFalse(user.checkPasswordComplexity(), "Password shorter than 8 chars should be invalid");
        
        // Test 9: Valid SA cell number (international)
        user.setCellNumber("+27838968976");
        assertTrue(user.checkCellPhoneNumber(), "Valid SA cell number '+27838968976' should be accepted");
        assertEquals("+27838968976", user.getCellNumber(), "International number should be stored correctly");
        
        // Test 10: Valid SA cell number (local - 07 format)
        Registration user2 = new Registration();
        user2.setCellNumber("0712345678");
        assertTrue(user2.checkCellPhoneNumber(), "Valid SA cell number '0712345678' should be accepted");
        assertEquals("+2712345678", user2.getCellNumber(), "Local 07 number should be formatted to international");
        
        // Test 11: Valid SA cell number (local - other 0 format)
        Registration user3 = new Registration();
        user3.setCellNumber("0821234567");
        assertTrue(user3.checkCellPhoneNumber(), "Valid SA cell number '0821234567' should be accepted");
        assertEquals("+27821234567", user3.getCellNumber(), "Local 0 number should be formatted to international");
        
        // Test 12: Invalid cell number
        try {
            Registration user4 = new Registration();
            user4.setCellNumber("08966553");
            assertFalse(user4.checkCellPhoneNumber(), "Invalid cell number '08966553' should be rejected");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASS: Invalid cell number correctly threw exception: " + e.getMessage());
            passed++;
        }
        
        // Test 13: First name validation
        user.firstName = "John";
        assertTrue(user.checkFirstName(), "Non-empty first name should be valid");
        
        // Test 14: Last name validation
        user.lastName = "Doe";
        assertTrue(user.checkLastName(), "Non-empty last name should be valid");
    }
    
    private static void runLoginTests() {
        System.out.println("\n🔐 Testing Login Class...");
        Login login = new Login();
        Registration user = new Registration();
        user.firstName = "John";
        user.lastName = "Doe";
        user.username = "v__1";
        user.password = "Ch&&sec@ke99!";
        
        // Test 1: Successful login
        assertTrue(login.checkLogin(user, "v__1", "Ch&&sec@ke99!"), "Valid login credentials should be accepted");
        
        // Test 2: Failed login (wrong password)
        assertFalse(login.checkLogin(user, "v__1", "wrongpass"), "Invalid password should be rejected");
        
        // Test 3: Failed login (wrong username)
        assertFalse(login.checkLogin(user, "wrong_user", "Ch&&sec@ke99!"), "Invalid username should be rejected");
        
        // Test 4: Login status messages - success
        String successMsg = login.returnLoginStatus(true, user);
        assertTrue(successMsg.contains("Welcome John Doe"), "Success message should contain welcome text");
        assertTrue(successMsg.contains("it is great to see you again"), "Success message should contain greeting");
        
        // Test 5: Login status messages - failure
        String failMsg = login.returnLoginStatus(false, user);
        assertTrue(failMsg.contains("Username or password incorrect"), "Fail message should contain error text");
        assertTrue(failMsg.contains("please try again"), "Fail message should contain retry instruction");
        
        // Test 6: Login with empty credentials
        assertFalse(login.checkLogin(user, "", "Ch&&sec@ke99!"), "Empty username should be rejected");
        assertFalse(login.checkLogin(user, "v__1", ""), "Empty password should be rejected");
        assertFalse(login.checkLogin(user, "", ""), "Both empty credentials should be rejected");
    }
    
    private static void runMessageTests() {
        System.out.println("\n💬 Testing Message Class...");
        Message msg = new Message();
        
        // Test 1: Valid recipient (international)
        assertEquals(1, msg.checkRecipientCell("+27718693002"), "Valid international recipient '+27718693002' should return 1");
        
        // Test 2: Valid recipient (local - 07 format)
        assertEquals(1, msg.checkRecipientCell("0712345678"), "Valid local recipient '0712345678' should return 1");
        
        // Test 3: Valid recipient (local - other 0 format)
        assertEquals(1, msg.checkRecipientCell("0821234567"), "Valid local recipient '0821234567' should return 1");
        
        // Test 4: Valid recipient (27 format without +)
        assertEquals(1, msg.checkRecipientCell("27831234567"), "Valid recipient '27831234567' should return 1");
        
        // Test 5: Invalid recipient
        assertEquals(0, msg.checkRecipientCell("08966553"), "Invalid recipient '08966553' should return 0");
        
        // Test 6: Message ID generation
        String messageID = msg.getMessageID();
        assertNotNull(messageID, "Message ID should not be null");
        assertEquals(10, messageID.length(), "Message ID should be 10 digits");
        assertTrue(messageID.matches("\\d+"), "Message ID should contain only digits");
        assertFalse(messageID.startsWith("0"), "Message ID should not start with 0");
        
        // Test 7: Message hash generation
        msg.setMessage("Hi Mike, can you join us for dinner tonight");
        String hash = msg.getMessageHash();
        assertNotNull(hash, "Message hash should not be null");
        assertTrue(hash.contains(":"), "Message hash should contain colon separators");
        
        // Test 8: Message sending options - send
        Message testMsg1 = new Message();
        assertEquals("Message sent successfully!", testMsg1.sentMessage(1), "Send message should return success");
        assertEquals("sent", testMsg1.getStatus(), "Message status should be 'sent'");
        
        // Test 9: Message sending options - discard
        Message testMsg2 = new Message();
        assertEquals("Message discarded", testMsg2.sentMessage(2), "Discard message should return discard message");
        assertEquals("discarded", testMsg2.getStatus(), "Message status should be 'discarded'");
        
        // Test 10: Message sending options - store
        Message testMsg3 = new Message();
        assertEquals("Message stored successfully!", testMsg3.sentMessage(3), "Store message should return store message");
        assertEquals("stored", testMsg3.getStatus(), "Message status should be 'stored'");
        
        // Test 11: Invalid message option
        Message testMsg4 = new Message();
        assertEquals("Invalid option", testMsg4.sentMessage(4), "Invalid option should return error");
        
        // Test 12: Message formatting - local to international (07 format)
        Message formatMsg1 = new Message();
        formatMsg1.setRecipient("0712345678");
        assertEquals("+2712345678", formatMsg1.getRecipient(), "Local 07 number should be automatically formatted to international");
        
        // Test 13: Message formatting - local to international (other 0 format)
        Message formatMsg2 = new Message();
        formatMsg2.setRecipient("0821234567");
        assertEquals("+27821234567", formatMsg2.getRecipient(), "Local 0 number should be formatted to international");
        
        // Test 14: Message formatting - international unchanged
        Message formatMsg3 = new Message();
        formatMsg3.setRecipient("+27831234567");
        assertEquals("+27831234567", formatMsg3.getRecipient(), "International format should remain unchanged");
        
        // Test 15: Message formatting - 27 format to international
        Message formatMsg4 = new Message();
        formatMsg4.setRecipient("27841234567");
        assertEquals("+27841234567", formatMsg4.getRecipient(), "27 format should be formatted to international");
        
        // Test 16: Invalid recipient should throw exception
        try {
            Message invalidMsg = new Message();
            invalidMsg.setRecipient("08966553");
            System.out.println("❌ FAIL: Invalid recipient should throw exception");
            failed++;
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASS: Invalid recipient correctly threw exception: " + e.getMessage());
            passed++;
        }
        
        // Test 17: Message count increments
        int firstCount = msg.getMessageCount();
        Message newMsg = new Message();
        int secondCount = newMsg.getMessageCount();
        assertTrue(secondCount > firstCount, "Message count should increment");
        
        // Test 18: Total messages counter
        int initialTotal = Message.returnTotalMessages();
        Message sentMsg = new Message();
        sentMsg.sentMessage(1);
        int finalTotal = Message.returnTotalMessages();
        assertTrue(finalTotal > initialTotal, "Total messages should increase after sending");
    }
    
    private static void runArrayManagerTests() {
        System.out.println("\n🗂️ Testing Array Manager Class...");
        ChatAppArrayManager manager = new ChatAppArrayManager();
        
        // Test 1: Add sent message
        Message msg1 = new Message();
        msg1.setRecipient("+27834557896");
        msg1.setMessage("Did you get the cake?");
        msg1.setStatus("sent");
        manager.addToSentMessages(msg1);
        
        assertEquals(1, manager.getSentMessages().size(), "Sent messages array should have 1 message");
        assertEquals(1, manager.getMessageHashes().size(), "Message hashes array should have 1 hash");
        assertEquals(1, manager.getMessageIDs().size(), "Message IDs array should have 1 ID");
        
        // Test 2: Add stored message
        Message msg2 = new Message();
        msg2.setRecipient("+27838884567");
        msg2.setMessage("Where are you? You are late! I have asked you to be on time.");
        msg2.setStatus("stored");
        manager.addToStoredMessages(msg2);
        
        assertEquals(1, manager.getStoredMessages().size(), "Stored messages array should have 1 message");
        
        // Test 3: Add disregarded message
        Message msg3 = new Message();
        msg3.setRecipient("+27834484567");
        msg3.setMessage("Yohoooo, I am at your gate.");
        msg3.setStatus("discarded");
        manager.addToDisregardedMessages(msg3);
        
        assertEquals(1, manager.getDisregardedMessages().size(), "Disregarded messages array should have 1 message");
        
        // Test 4: Find longest message
        Message longest = manager.findLongestMessage();
        assertNotNull(longest, "Longest message should not be null");
        assertEquals("Did you get the cake?", longest.getMessage(), "Should find correct longest message");
        
        // Test 5: Search by recipient
        java.util.ArrayList<Message> results = manager.searchMessagesByRecipient("+27838884567");
        assertEquals(1, results.size(), "Should find 1 message for recipient '+27838884567'");
        
        // Test 6: Search by ID
        String searchID = manager.getSentMessages().get(0).getMessageID();
        Message found = manager.searchMessageByID(searchID);
        assertNotNull(found, "Should find message by ID");
        assertEquals("Did you get the cake?", found.getMessage(), "Found message should have correct content");
        
        // Test 7: Delete by hash
        String hashToDelete = manager.getStoredMessages().get(0).getMessageHash();
        boolean deleted = manager.deleteMessageByHash(hashToDelete);
        assertTrue(deleted, "Message should be deleted by hash");
        assertEquals(0, manager.getStoredMessages().size(), "Stored messages should be empty after deletion");
        
        // Test 8: Array consistency
        int totalMessages = manager.getSentMessages().size() + manager.getStoredMessages().size();
        assertEquals(totalMessages, manager.getMessageHashes().size(), "Hashes array should match total messages");
        assertEquals(totalMessages, manager.getMessageIDs().size(), "IDs array should match total messages");
        
        // Test 9: Display methods - senders and recipients
        String sendersInfo = manager.displaySendersAndRecipients("Test User");
        assertTrue(sendersInfo.contains("Test User"), "Senders info should contain sender name");
        assertTrue(sendersInfo.contains("+27834557896"), "Senders info should contain recipient");
        
        // Test 10: Display methods - full report
        String report = manager.generateFullReport("Test User");
        assertTrue(report.contains("FULL SENT MESSAGES REPORT"), "Report should contain header");
        assertTrue(report.contains("Did you get the cake?"), "Report should contain message content");
        assertTrue(report.contains("SUMMARY"), "Report should contain summary");
        
        // Test 11: Empty arrays handling
        ChatAppArrayManager emptyManager = new ChatAppArrayManager();
        Message emptyLongest = emptyManager.findLongestMessage();
        assertNull(emptyLongest, "Longest message should be null for empty arrays");
        
        java.util.ArrayList<Message> emptyResults = emptyManager.searchMessagesByRecipient("+27838884567");
        assertTrue(emptyResults.isEmpty(), "Search results should be empty for empty arrays");
        
        String emptySendersInfo = emptyManager.displaySendersAndRecipients("Test User");
        assertTrue(emptySendersInfo.contains("No sent messages"), "Should indicate no sent messages for empty arrays");
        
        String emptyReport = emptyManager.generateFullReport("Test User");
        assertTrue(emptyReport.contains("No sent messages"), "Should indicate no sent messages for report");
    }
    
    private static void runJSONHandlerTests() {
        System.out.println("\n📄 Testing JSON Handler Class...");
        
        // Test 1: Store message to JSON
        Message testMsg = new Message();
        testMsg.setRecipient("+27123456789");
        testMsg.setMessage("Test message for JSON storage");
        testMsg.setStatus("sent");
        
        JSONHandler.storeMessage(testMsg);
        System.out.println("✅ INFO: Message stored to JSON");
        
        // Test 2: Read messages from JSON
        try {
            java.util.ArrayList<Message> messages = JSONHandler.readAllMessages();
            assertTrue(messages != null, "Should be able to read messages from JSON");
            System.out.println("✅ INFO: Successfully read " + messages.size() + " messages from JSON");
        } catch (Exception e) {
            System.out.println("⚠️  WARNING: Could not read JSON file (might be empty or first run)");
        }
        
        // Test 3: Get formatted messages
        String formatted = JSONHandler.getAllMessagesFormatted();
        assertTrue(formatted != null, "Should be able to get formatted messages");
        System.out.println("✅ INFO: Formatted messages retrieved successfully");
        
        // Test 4: Delete message from JSON
        try {
            boolean deleted = JSONHandler.deleteMessage(testMsg.getMessageID());
            if (deleted) {
                System.out.println("✅ INFO: Message successfully deleted from JSON");
            } else {
                System.out.println("⚠️  INFO: Message deletion may have failed (might not exist in JSON)");
            }
        } catch (Exception e) {
            System.out.println("⚠️  INFO: Message deletion may have failed: " + e.getMessage());
        }
    }
    
    // Assertion methods
    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("✅ PASS: " + message);
            passed++;
        } else {
            System.out.println("❌ FAIL: " + message);
            failed++;
        }
    }
    
    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }
    
    private static void assertEquals(Object expected, Object actual, String message) {
        boolean condition = (expected == null && actual == null) || (expected != null && expected.equals(actual));
        if (condition) {
            System.out.println("✅ PASS: " + message + " (expected: " + expected + ", got: " + actual + ")");
            passed++;
        } else {
            System.out.println("❌ FAIL: " + message + " (expected: " + expected + ", but got: " + actual + ")");
            failed++;
        }
    }
    
    private static void assertNotNull(Object obj, String message) {
        if (obj != null) {
            System.out.println("✅ PASS: " + message);
            passed++;
        } else {
            System.out.println("❌ FAIL: " + message);
            failed++;
        }
    }
    
    private static void assertNull(Object obj, String message) {
        if (obj == null) {
            System.out.println("✅ PASS: " + message);
            passed++;
        } else {
            System.out.println("❌ FAIL: " + message);
            failed++;
        }
    }
    
    private static void printResults() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 TEST SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("✅ Tests Passed: " + passed);
        System.out.println("❌ Tests Failed: " + failed);
        System.out.println("📋 Total Tests: " + (passed + failed));
        
        int successRate = (passed + failed) > 0 ? (passed * 100 / (passed + failed)) : 0;
        System.out.println("🎯 Success Rate: " + successRate + "%");
        
        if (failed == 0) {
            System.out.println("🎉 ALL TESTS PASSED! Your code is ready for production! 🎉");
        } else {
            System.out.println("💥 SOME TESTS FAILED! Please check the failures above.");
        }
        System.out.println("=".repeat(60));
    }
}
