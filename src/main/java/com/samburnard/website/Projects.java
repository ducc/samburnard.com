package com.samburnard.website;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.*;

class Projects {

    private final Map<String, Project> projects = new HashMap<>();

    private final File directory;

    Projects(File directory) throws IOException, NotDirectoryException {
        this.directory = directory;
        if (!directory.exists()) {
            throw new FileNotFoundException(directory.getName());
        }
        if (!directory.isDirectory()) {
            throw new NotDirectoryException(directory.getName());
        }
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new EmptyDirectoryException(directory.getName());
        }
        loadProjects(projects, files);
    }

    class EmptyDirectoryException extends IOException {
        private final String message;

        EmptyDirectoryException(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    private JSONObject readFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
            return new JSONObject(builder.toString());
        }
    }

    class Project {
        private final List<Image> images = new ArrayList<>();

        private final String id;
        private final String title;
        private final String summary;
        private final String description;
        private final String image;

        Project(File file) throws IOException {
            JSONObject json = readFile(file);
            this.id = json.getString("id");
            this.title = json.getString("title");
            this.summary = json.getString("summary");
            this.description = json.getString("description");
            this.image = json.getString("image");
            if (!json.isNull("images")) {
                JSONArray images = json.getJSONArray("images");
                // edgy iterator for that java 8 lambda magic
                Iterable<JSONObject> iterable = () -> new Iterator<JSONObject>() {
                    private int pointer = 0;

                    @Override
                    public boolean hasNext() {
                        return pointer < images.length();
                    }

                    @Override
                    public JSONObject next() {
                        JSONObject object = images.getJSONObject(pointer);
                        pointer++;
                        return object;
                    }
                };
                iterable.forEach(object -> {
                    String id = object.getString("id");
                    String thumbnail = object.getString("thumbnail");
                    String image = object.getString("image");
                    Image img = new Image(id, thumbnail, image);
                    this.images.add(img);
                });
            }
        }

        private class Image {
            private final String id;
            private final String thumbnail;
            private final String image;

            private Image(String id, String thumbnail, String image) {
                this.id = id;
                this.thumbnail = thumbnail;
                this.image = image;
            }

            private JSONObject toJson() {
                return new JSONObject()
                        .put("id", id)
                        .put("thumbnail", thumbnail)
                        .put("image", image);
            }
        }

        public JSONObject toJson() {
            JSONObject json = new JSONObject()
                    .put("id", id)
                    .put("title", title)
                    .put("summary", summary)
                    .put("description", description)
                    .put("image", image);
            if (images.size() > 0) {
                JSONArray array = new JSONArray();
                images.forEach(image -> array.put(image.toJson()));
                json.put("images", array);
            }
            return json;
        }
    }

    private void loadProjects(Map<String, Project> map, File[] files) {
        for (File file : files) {
            Project project;
            try {
                project = new Project(file);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            map.put(project.id, project);
        }
    }

    private void writeFile(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            writer.flush();
        }
    }

    void createProject(Project project) throws IOException {
        this.projects.put(project.id, project);
        String content = project.toJson().toString(4);
        File file = new File(directory, project.id + ".json");
        writeFile(file, content);
    }

}
