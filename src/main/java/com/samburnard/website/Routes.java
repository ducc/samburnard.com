package com.samburnard.website;

import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Response;
import spark.Service;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.util.*;

class Routes {

    private final Authentication authentication;
    private final Projects projects;
    private final About about;
    private final Service service;
    private final TemplateEngine engine;

    Routes() throws IOException {
        this.authentication = new Authentication(new File(Website.CREDENTIALS_FILE));
        this.projects = new Projects(new File(Website.PROJECTS_DIRECTORY));
        this.about = new About(new File(Website.ABOUT_FILE));
        this.service = Service.ignite();
        this.service.port(Website.PORT);
        this.service.exception(Exception.class, (e, request, response) -> e.printStackTrace());
        this.service.staticFileLocation(Website.STATIC_DIRECTORY);
        this.service.staticFiles.expireTime(Website.STATIC_EXPIRE_DURATION);
        Configuration configuration = new Configuration();
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
    }

    private ModelAndView error(Response response, int code, String message) {
        Map<String, Object> model = new HashMap<>();
        response.status(code);
        model.put("code", code);
        model.put("message", message);
        return new ModelAndView(model, "error.ftl");
    }

    private void index() {
        service.get("/", (request, response) -> {
            Map model = new HashMap<>();
            return new ModelAndView(model, "index.ftl");
        }, engine);
    }

    private void portfolio() {
        service.get("/portfolio", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            List<Map<String, Object>> projects = new ArrayList<>();
            this.projects.getProjects().forEach(project -> projects.add(project.toMap()));
            model.put("projects", projects);
            return new ModelAndView(model, "portfolio.ftl");
        }, engine);
    }

    private void project() {
        service.get("/projects/:project", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
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
            Map model = new HashMap<>();
            model.put("content", about.getContent());
            return new ModelAndView(model, "about.ftl");
        }, engine);
    }

    private void contact() {
        service.get("/contact", (request, response) -> {
            Map model = new HashMap<>();
            return new ModelAndView(model, "contact.ftl");
        }, engine);
    }

    private void login() {
        service.get("/login", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            if (authentication.isAuthenticated(request.session())) {
                return error(response, 403, "You are already logged in!");
            }
            return new ModelAndView(model, "login.ftl");
        }, engine);
        service.post("/auth/login", (request, response) -> {
            if (authentication.isAuthenticated(request.session())) {
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
        }

        private void index() {
            service.get("/admin", (request, response) -> {
                Map<String, Object> model = new HashMap<>();
                if (!authentication.isAuthenticated(request.session())) {
                    return error(response, 401, "You must be logged in!");
                }
                return new ModelAndView(model, "admin/admin_index.ftl");
            }, engine);
        }

        private void add() {
            service.get("/admin/add", (request, response) -> {
                Map<String, Object> model = new HashMap<>();
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
                    if (param.startsWith("image_image_")) {
                        int length = param.length();
                        int id = Integer.parseInt(String.valueOf(param.charAt(length - 1)));
                        String thumbnailKey = "image_thumbnail_" + id;
                        String thumbnailValue = request.queryParams(thumbnailKey);
                        Projects.Image image = projects.new Image(String.valueOf(id), thumbnailValue, value);
                        builder.with(image);
                        continue;
                    } else if (param.startsWith("image_thumbnail_")) {
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
                Map<String, Object> model = new HashMap<>();
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
                Map<String, Object> model = new HashMap<>();
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
                    if (param.startsWith("image_image_")) {
                        int length = param.length();
                        int imageId = Integer.parseInt(String.valueOf(param.charAt(length - 1)));
                        String thumbnailKey = "image_thumbnail_" + imageId;
                        String thumbnailValue = request.queryParams(thumbnailKey);
                        Projects.Image i = projects.new Image(String.valueOf(imageId), thumbnailValue, value);
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
                Map<String, Object> model = new HashMap<>();
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
                Map<String, Object> model = new HashMap<>();
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
    }

}
