package com.samburnard.website;

import java.io.*;

class About {

    private final File file;

    private String content = null;

    About(File file) throws IOException {
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

    void setContent(String content) throws IOException {
        this.content = content;
        writeFile(file, content);
    }
}
