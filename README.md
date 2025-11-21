# QuickChat Messaging App

![Java CI Tests](https://github.com/your-username/QuickChat-App/actions/workflows/ci.yml/badge.svg)

A comprehensive Java messaging application with GUI, array operations, and JSON storage.

## Features

- User registration and login
- Message sending with validation
- Array operations for message management
- JSON file storage
- Automated testing

## Running Tests Locally

```bash
# Compile all Java files
javac -d build src/chatapp/*.java

# Run tests
java -cp build chatapp.TestRunner
