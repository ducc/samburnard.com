package com.samburnard.website;

import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Service;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Routes {

    private final Authentication authentication;
    private final Service service;
    private final TemplateEngine engine;

    Routes() throws IOException {
        this.authentication = new Authentication(new File(Website.CREDENTIALS_FILE));
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
        admin();
    }

    private void index() {
        service.get("/", (request, response) -> {
            Map model = new HashMap<>();
            return new ModelAndView(model, "index.ftl");
        }, engine);
    }

    private void portfolio() {
        service.get("/portfolio", (request, response) -> {
            Map model = new HashMap<>();
            return new ModelAndView(model, "portfolio.ftl");
        }, engine);
    }

    private void project() {
        service.get("/projects/:project", (request, response) -> {
            Map model = new HashMap<>();
            return new ModelAndView(model, "project.ftl");
        }, engine);
    }

    private void about() {
        service.get("/about", (request, response) -> {
            Map model = new HashMap<>();
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
            String page = "login.ftl";
            if (authentication.isAuthenticated(request.session())) {
                page = "error.ftl";
                model.put("code", 403);
                model.put("message", "You are already logged in!");
            }
            return new ModelAndView(model, page);
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

    private void admin() {
        service.get("/admin", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String page = "admin/admin_index.ftl";
            if (!authentication.isAuthenticated(request.session())) {
                page = "error.ftl";
                model.put("code", 401);
                model.put("message", "You must be logged in!");
            }
            return new ModelAndView(model, page);
        }, engine);
        service.get("/admin/add", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String page = "admin/admin_add.ftl";
            if (!authentication.isAuthenticated(request.session())) {
                page = "error.ftl";
                model.put("code", 401);
                model.put("message", "You must be logged in!");
            }
            return new ModelAndView(model, page);
        }, engine);
        service.get("/admin/manage", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String page = "admin/admin_manage.ftl";
            if (!authentication.isAuthenticated(request.session())) {
                page = "error.ftl";
                model.put("code", 401);
                model.put("message", "You must be logged in!");
            }
            return new ModelAndView(model, page);
        }, engine);
    }

}
