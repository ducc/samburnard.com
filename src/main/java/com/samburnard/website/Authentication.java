package com.samburnard.website;

import spark.Session;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Authentication {

    private static final String AUTHENTICATION_TOKEN_SESSION_KEY = "auth-token";
    private static final Random RANDOM = new SecureRandom();

    private final String salt;
    private final String credentialsHash;

    private String authenticationToken = null;

    Authentication(File credentials) throws FileNotFoundException {
        if (!credentials.exists()) {
            throw new FileNotFoundException(credentials.getName());
        }
        List<String> lines;
        try {
            lines = readFile(credentials);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String username = lines.get(0);
        String password = lines.get(1);
        this.salt = lines.get(2);
        this.credentialsHash = hashCredentials(username, password, this.salt);
    }

    private List<String> readFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private String generateAuthenticationToken() {
        return new BigInteger(130, RANDOM).toString(32);
    }

    private byte[] hash(MessageDigest digest, byte[] bytes) {
        return digest.digest(bytes);
    }

    private String toHexString(byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    private String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = input.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < Website.HASHING_ROUNDS; i++) {
            hash = hash(digest, hash);
        }
        return toHexString(hash);
    }

    private String hashCredentials(String username, String password, String salt) {
        try {
            return hash(username + salt + password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    boolean isAuthenticated(Session session) {
        return authenticationToken != null && session.attribute(AUTHENTICATION_TOKEN_SESSION_KEY) != null
                && session.attribute(AUTHENTICATION_TOKEN_SESSION_KEY).equals(authenticationToken);
    }

    private boolean validateCredentials(String username, String password) {
        if (credentialsHash == null) return false;
        String hash = hashCredentials(username, password, salt);
        return hash != null && hash.equals(credentialsHash);
    }

    abstract class LoginException extends Exception {
        private final String message;

        private LoginException(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    private class AlreadyLoggedInException extends LoginException {
        private AlreadyLoggedInException() {
            super("You are already logged in!");
        }
    }

    class NotLoggedInException extends LoginException {
        private NotLoggedInException() {
            super("You are not logged in!");
        }
    }

    private class InvalidCredentialsException extends LoginException {
        private InvalidCredentialsException() {
            super("Invalid username or password!");
        }
    }

    void login(Session session, String username, String password) throws LoginException {
        if (isAuthenticated(session)) {
            throw new AlreadyLoggedInException();
        }
        if (!validateCredentials(username, password)) {
            throw new InvalidCredentialsException();
        }
        authenticationToken = generateAuthenticationToken();
        session.attribute(AUTHENTICATION_TOKEN_SESSION_KEY, authenticationToken);
    }

    void logout(Session session) throws NotLoggedInException {
        if (!isAuthenticated(session)) {
            throw new NotLoggedInException();
        }
        session.removeAttribute(AUTHENTICATION_TOKEN_SESSION_KEY);
        this.authenticationToken = null;
    }

}
