package com.project.cravehub.controller.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error"; // Custom error path


    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping(PATH)
    public String handleError() {
        // Custom error handling logic
        return "errorPage";
    }
//    @RequestMapping("/error/401")
//    public String UnAuthorised(){
//        return "401";
//    }
//
//    @RequestMapping("/blocked")
//    public String UserBlocked(){
//        return "userblocked";
//    }
//
//    @RequestMapping("/error/500")
//    public String internalServerError() {
//        // Custom logic for handling 500 errors
//        return "500";
//    }
}