package com.samburnard.website;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

class Routes {

    private final Service service;
    private final TemplateEngine engine;

    Routes() throws IOException {
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

}
