package com.samburnard.website;

import java.io.IOException;
import java.net.URISyntaxException;

final class Website {

    static final int PORT = 80;
    static final String CREDENTIALS_FILE = "credentials.txt";
    static final String PROJECTS_DIRECTORY = "projects";
    static final String INFORMATION_FILE = "information.txt";
    static final String HOME_FILE = "home.json";
    static final String SOCIAL_FILE = "social.json";
    static final String TEMPLATE_DIRECTORY = "templates";
    static final String STATIC_DIRECTORY = "/static";
    static final long STATIC_EXPIRE_DURATION = 1;
    static final int HASHING_ROUNDS = 50;

    public static void main(String[] args) throws IOException {
        try {
            new Routes();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
