package com.samburnard.website;

import org.json.JSONObject;

import java.io.*;

class ContentPage {

    private final File file;

    private String content = null;
    private JSONObject json = null;

    ContentPage(File file) throws IOException {
        this.file = file;
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                throw new IOException("Could not create file " + file.getName());
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
            this.content = builder.toString();
        }
    }

    private void writeFile(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            writer.flush();
        }
    }

    String getContent() {
        return content;
    }

    JSONObject getContentAsJson() {
        if (content == null || content.length() == 0) {
            return null;
        }
        if (json == null) {
            json = new JSONObject(content);
        }
        return json;
    }

    void setContent(String content) throws IOException {
        this.content = content;
        writeFile(file, content);
        json = null;
    }
}
