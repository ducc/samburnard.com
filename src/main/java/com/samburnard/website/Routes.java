package com.samburnard.website;

import freemarker.template.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.Response;
import spark.Service;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;
import sun.misc.Launcher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class Routes {

    private final Authentication authentication;
    private final Projects projects;
    private final ContentPage about;
    private final ContentPage contact;
    private final ContentPage home;
    private final ContentPage social;
    private final Service service;
    private final TemplateEngine engine;

    Routes() throws IOException, URISyntaxException {
        this.authentication = new Authentication(new File(Website.CREDENTIALS_FILE));
        this.projects = new Projects(new File(Website.PROJECTS_DIRECTORY));
        this.about = new ContentPage(new File(Website.ABOUT_FILE));
        this.contact = new ContentPage(new File(Website.CONTACT_FILE));
        this.home = new ContentPage(new File(Website.HOME_FILE));
        this.social = new ContentPage(new File(Website.SOCIAL_FILE));
        this.service = Service.ignite();
        this.service.port(Website.PORT);
        this.service.exception(Exception.class, (e, request, response) -> e.printStackTrace());
        this.service.staticFileLocation(Website.STATIC_DIRECTORY);
        this.service.staticFiles.expireTime(Website.STATIC_EXPIRE_DURATION);
        Configuration configuration = new Configuration();
        ClassLoader classLoader = getClass().getClassLoader();

        /*@SuppressWarnings("ConstantConditions") URL url = classLoader.getResource(Website.TEMPLATE_DIRECTORY);
        System.out.println(url);
        File file = new File(url.getFile());*/
        //File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        //System.out.println(file);
        //System.out.println(Arrays.toString(file.list()));
        //configuration.setDirectoryForTemplateLoading(file);
        //URL url = classLoader.getResource(Website.TEMPLATE_DIRECTORY);
        //JarURLConnection connection = (JarURLConnection) url.openConnection();
        //URL u = connection.getJarFileURL();
        //System.out.println(u.getFile());

        configuration.setDirectoryForTemplateLoading(new File(Website.TEMPLATE_DIRECTORY));

        configuration.setTemplateExceptionHandler((e, environment, writer) -> e.printStackTrace());
        this.engine = new FreeMarkerEngine(configuration);
        index();
        portfolio();
        project();
        about();
        contact();
        login();
        logout();
        new Admin();
        service.get("*", (request, response) -> error(response, 404, "Page not found"), engine);
    }

    private File getFileFromJar(String path) {
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {  // Run with JAR file
            final JarFile jar;
            try {
                jar = new JarFile(jarFile);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while(entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(path + "/")) { //filter according to the path
                    File file = new File(name);
                    //System.out.println(name);
                    return file;
                }
            }
            try {
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // Run with IDE
            final URL url = Launcher.class.getResource("/" + path);
            if (url != null) {
                try {
                    final File apps = new File(url.toURI());
                    //noinspection ConstantConditions
                    for (File app : apps.listFiles()) {
                        System.out.println(app);
                        return app;
                    }
                } catch (URISyntaxException ex) {
                    // never happens
                }
            }
        }
        return null;
    }

    private ModelAndView error(Response response, int code, String message) {
        Map<String, Object> model = getNewModel();
        response.status(code);
        model.put("code", code);
        model.put("message", message);
        return new ModelAndView(model, "error.ftl");
    }

    private Map<String, Object> getNewModel() {
        class ModelBuilder {
            private final Map<String, Object> model = new HashMap<>();

            private final JSONObject json;
            private final String[] items;

            private ModelBuilder(JSONObject json, String... items) {
                this.json = json;
                this.items = items;
            }

            private Map<String, Object> build() {
                if (json != null) {
                    for (String item : items) {
                        if (!json.isNull(item)) {
                            model.put(item, json.get(item));
                        }
                    }
                }
                return model;
            }
        }
        JSONObject json = social.getContentAsJson();
        return new ModelBuilder(json, "instagram", "twitter", "facebook", "youtube", "behance", "imgur").build();
    }

    private void index() {
        service.get("/", (request, response) -> {
            Map<String, Object> model = getNewModel();
            JSONObject json = home.getContentAsJson();
            if (json != null) {
                model.put("json", json);
            }
            return new ModelAndView(model, "index.ftl");
        }, engine);
    }

    private void portfolio() {
        service.get("/portfolio", (request, response) -> {
            Map<String, Object> model = getNewModel();
            List<Map<String, Object>> projects = new ArrayList<>();
            this.projects.getProjects().forEach(project -> projects.add(project.toMap()));
            model.put("projects", projects);
            return new ModelAndView(model, "portfolio.ftl");
        }, engine);
    }

    private void project() {
        service.get("/projects/:project", (request, response) -> {
            Map<String, Object> model = getNewModel();
            String id = request.params("project");
            if (id == null) {
                return error(response, 404, "Project not found");
            }
            Projects.Project project = projects.getProject(id);
            if (project == null) {
                return error(response, 404, "Project not loaded");
            }
            model.put("project", project.toMap());
            return new ModelAndView(model, "project.ftl");
        }, engine);
    }

    private void about() {
        service.get("/about", (request, response) -> {
            Map<String, Object> model = getNewModel();
            model.put("content", about.getContent());
            return new ModelAndView(model, "about.ftl");
        }, engine);
    }

    private void contact() {
        service.get("/contact", (request, response) -> {
            Map<String, Object> model = getNewModel();
            model.put("content", contact.getContent());
            return new ModelAndView(model, "contact.ftl");
        }, engine);
    }

    private void login() {
        service.get("/login", (request, response) -> {
            Map<String, Object> model = getNewModel();
            if (authentication.isAuthenticated(request.session())) {
                return error(response, 403, "You are already logged in!");
            }
            return new ModelAndView(model, "login.ftl");
        }, engine);
        service.post("/auth/login", (request, response) -> {
            if (authentication.isAuthenticated(request.session())) {
                response.redirect("/admin");
                return "You are already logged in!";
            }
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            try {
                authentication.login(request.session(), username, password);
            } catch (Authentication.LoginException e) {
                return "Could not login: " + e.getMessage();
            }
            response.redirect("/admin");
            return "Logged in!";
        });
    }

    private void logout() {
        service.get("/logout", (request, response) -> {
            try {
                authentication.logout(request.session());
            } catch (Authentication.NotLoggedInException e) {
                return "Could not logout: " + e.getMessage();
            }
            response.redirect("/");
            return "Logged out!";
        });
    }

    /*
    Class to contain admin procedures
     */
    private class Admin {

        private Admin() {
            index();
            add();
            manage();
            edit();
            delete();
            about();
            contact();
            home();
            social();
        }

        private void index() {
            service.get("/admin", (request, response) -> {
                Map<String, Object> model = getNewModel();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                return new ModelAndView(model, "admin/admin_index.ftl");
            }, engine);
        }

        private void add() {
            service.get("/admin/add", (request, response) -> {
                Map<String, Object> model = getNewModel();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                return new ModelAndView(model, "admin/admin_add.ftl");
            }, engine);
            service.post("/admin/add", (request, response) -> {
                if (!authentication.isAuthenticated(request.session())) {
                    return "no.";
                }
                Set<String> params = request.queryParams();
                Projects.ProjectBuilder builder = projects.new ProjectBuilder();
                List<String> strings = new ArrayList<>(params);
                Collections.sort(strings);
                for (String param : strings) {
                    String value = request.queryParams(param);
                    if (value == null || value.length() == 0) continue;
                    if (param.startsWith("image_")) {
                        int length = param.length();
                        int id = Integer.parseInt(String.valueOf(param.charAt(length - 1)));
                        Projects.Image image = projects.new Image(String.valueOf(id), value);
                        builder.with(image);
                        continue;
                    }
                    builder.with(param, value);
                }
                Projects.Project project = builder.build();
                projects.createProject(project);
                response.redirect("/admin/manage");
                return "OK! Redirecting...";
            });
        }

        private void manage() {
            service.get("/admin/manage", (request, response) -> {
                Map<String, Object> model = getNewModel();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                // ew wtf have i done :'(
                List<Map<String, Object>> projectsList = new ArrayList<>();
                projects.getProjects().forEach(project -> projectsList.add(project.toMap()));
                model.put("projects", projectsList);
                return new ModelAndView(model, "admin/admin_manage.ftl");
            }, engine);
        }

        private void edit() {
            service.get("/admin/edit/:project", (request, response) -> {
                Map<String, Object> model = getNewModel();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                String id = request.params("project");
                if (id == null) {
                    return error(response, 404, "Project not found!");
                }
                Projects.Project project = projects.getProject(id);
                model.put("project", project.toMap());
                return new ModelAndView(model, "admin/admin_edit.ftl");
            }, engine);
            service.post("/admin/edit", ((request, response) -> {
                if (!authentication.isAuthenticated(request.session())) {
                    return "no.";
                }
                String id = request.queryParams("id");
                if (id == null) {
                    return "invalid project id";
                }
                Projects.Project project = projects.getProject(id);
                boolean updated = false;
                String title = request.queryParams("name");
                String summary = request.queryParams("summary");
                String description = request.queryParams("description");
                String image = request.queryParams("mainimage");
                if (title != null) {
                    project.setTitle(title);
                    updated = true;
                }
                if (summary != null) {
                    project.setSummary(summary);
                    updated = true;
                }
                if (description != null) {
                    project.setDescription(description);
                    updated = true;
                }
                if (image != null) {
                    project.setImage(image);
                    updated = true;
                }
                Set<String> params = request.queryParams();
                List<String> strings = new ArrayList<>(params);
                Collections.sort(strings);
                project.getImages().clear();
                for (String param : strings) {
                    String value = request.queryParams(param);
                    if (value == null || value.length() == 0) continue;
                    if (param.startsWith("image_")) {
                        int length = param.length();
                        int imageId = Integer.parseInt(String.valueOf(param.charAt(length - 1)));
                        Projects.Image i = projects.new Image(String.valueOf(imageId), value);
                        project.getImages().add(i);
                        updated = true;
                    }
                }
                if (updated) {
                    project.update();
                }
                response.redirect("/admin/manage");
                return "ok";
            }));
        }

        private void delete() {
            service.get("/admin/delete/:project", (request, response) -> {
                Map<String, Object> model = getNewModel();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                String id = request.params("project");
                if (id == null) {
                    return error(response, 404, "Project not found!");
                }
                Projects.Project project = projects.getProject(id);
                model.put("project", project.toMap());
                return new ModelAndView(model, "admin/admin_delete.ftl");
            }, engine);
            service.get("/admin/delete/:project/confirm", (request, response) -> {
                if (!authentication.isAuthenticated(request.session())) {
                    return "no.";
                }
                String id = request.params("project");
                if (id == null) {
                    return "invalid project";
                }
                Projects.Project project = projects.getProject(id);
                projects.deleteProject(project);
                response.redirect("/admin/manage");
                return "ok";
            });
        }

        private void about() {
            service.get("/admin/about", (request, response) -> {
                Map<String, Object> model = getNewModel();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                model.put("content", about.getContent());
                return new ModelAndView(model, "admin/admin_about.ftl");
            }, engine);
            service.post("/admin/about", (request, response) -> {
                if (!authentication.isAuthenticated(request.session())) {
                    return "no.";
                }
                String content = request.queryParams("content");
                if (content == null) {
                    return "content is null";
                }
                about.setContent(content);
                response.redirect("/admin/about");
                return "ok";
            });
        }

        private void contact() {
            service.get("/admin/contact", (request, response) -> {
                Map<String, Object> model = getNewModel();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                model.put("content", contact.getContent());
                return new ModelAndView(model, "admin/admin_contact.ftl");
            }, engine);
            service.post("/admin/contact", (request, response) -> {
                if (!authentication.isAuthenticated(request.session())) {
                    return "no.";
                }
                String content = request.queryParams("content");
                if (content == null) {
                    return "content is null";
                }
                contact.setContent(content);
                response.redirect("/admin/contact");
                return "ok";
            });
        }

        @SuppressWarnings("CodeBlock2Expr")
        private void home() {
            service.get("/admin/home", (request, response) -> {
                Map<String, Object> model = getNewModel();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                JSONObject json = home.getContentAsJson();
                if (json != null && !json.isNull("images")) {
                    model.put("images", json.getJSONArray("images").toString().substring(1).replace("]", "").replace("\"", ""));
                }
                return new ModelAndView(model, "admin/admin_home.ftl");
            }, engine);
            service.post("/admin/home/carousel", (request, response) -> {
                if (!authentication.isAuthenticated(request.session())) {
                    return "no.";
                }
                String rawImages = request.queryParams("images");
                if (rawImages == null) {
                    return "images is null";
                }
                if (rawImages.length() == 0 || !rawImages.contains("c")) {
                    return "invalid images input";
                }
                String[] split = rawImages.split(",");
                JSONArray array = new JSONArray();
                for (String s : split) {
                    array.put(s);
                }
                home.setContent(new JSONObject().put("images", array).toString());
                response.redirect("/admin/home");
                return "ok";
            });
        }

        private void social() {
            service.get("/admin/social", (request, response) -> {
                Map<String, Object> model = getNewModel();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                return new ModelAndView(model, "admin/admin_social.ftl");
            }, engine);
            service.post("/admin/social", (request, response) -> {
                if (!authentication.isAuthenticated(request.session())) {
                    return "no.";
                }
                JSONObject json = social.getContentAsJson();
                if (json == null) {
                    json = new JSONObject();
                }
                String instagram = request.queryParams("instagram");
                if (instagram != null) {
                    json.put("instagram", instagram);
                }
                String twitter = request.queryParams("twitter");
                if (twitter != null) {
                    json.put("twitter", twitter);
                }
                String facebook = request.queryParams("facebook");
                if (facebook != null) {
                    json.put("facebook", facebook);
                }
                String youtube = request.queryParams("youtube");
                if (youtube != null) {
                    json.put("youtube", youtube);
                }
                String behance = request.queryParams("behance");
                if (behance != null) {
                    json.put("behance", behance);
                }
                String imgur = request.queryParams("imgur");
                if (imgur != null) {
                    json.put("imgur", imgur);
                }
                social.setContent(json.toString());
                response.redirect("/admin/social");
                return "ok";
            });
        }

    }

}
