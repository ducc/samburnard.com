package com.samburnard.website;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.*;

class Projects {

    private final Map<String, Project> projects = new HashMap<>();

    private final File directory;

    private int numberOfProjects;

    Projects(File directory) throws IOException {
        this.directory = directory;
        if (!directory.exists()) {
            throw new FileNotFoundException(directory.getName());
        }
        if (!directory.isDirectory()) {
            throw new NotDirectoryException(directory.getName());
        }
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            this.numberOfProjects = 0;
            return;
        }
        this.numberOfProjects = files.length;
        loadProjects(projects, files);
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

        Project(String id, String title, String summary, String description, String image, Collection<Image> images) {
            this.id = id;
            this.title = title;
            this.summary = summary;
            this.description = description;
            this.image = image;
            if (images != null && !images.isEmpty()) {
                this.images.addAll(images);
            }
        }

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

        JSONObject toJson() {
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

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("title", title);
            map.put("summary", summary);
            map.put("description", description);
            map.put("image", image);
            String[][] images = new String[this.images.size()][3];
            for (int i = 0; i < this.images.size(); i++) {
                images[i] = this.images.get(i).toArray();
            }
            map.put("images", images);
            return map;
        }

        String getId() {
            return id;
        }

        String getTitle() {
            return title;
        }

        String getSummary() {
            return summary;
        }

        String getDescription() {
            return description;
        }

        String getImage() {
            return image;
        }

        List<Image> getImages() {
            return images;
        }
    }

    class Image {
        private final String id;
        private final String thumbnail;
        private final String image;

        Image(String id, String thumbnail, String image) {
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

        String[] toArray() {
            String[] array = new String[3];
            array[0] = id;
            array[1] = thumbnail;
            array[2] = image;
            return array;
        }
    }

    class ProjectBuilder {

        private String id = null;
        private String title = null;
        private String summary = null;
        private String description = null;
        private String image = null;
        private Set<Image> images = new HashSet<>();

        ProjectBuilder() {
            this.id = String.valueOf(numberOfProjects + 1);
        }

        ProjectBuilder with(String key, String value) {
            switch (key.toLowerCase()) {
                case "title":
                    this.title = value;
                    break;
                case "summary":
                    this.summary = value;
                    break;
                case "description":
                    this.description = value;
                    break;
                case "mainimage":
                    this.image = value;
                    break;
                default:
                    System.err.println("Invalid project attribute name \"" + key + "\"?");
            }
            return this;
        }

        ProjectBuilder with(Image image) {
            images.add(image);
            return this;
        }

        Project build() {
            return new Project(id, title, summary, description, image, images);
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

    Collection<Project> getProjects() {
        return projects.values();
    }

    int countProjects() {
        return projects.size();
    }

}
