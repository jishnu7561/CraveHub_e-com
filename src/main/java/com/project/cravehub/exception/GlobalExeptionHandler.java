//package com.project.cravehub.exception;
//
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.nio.channels.AcceptPendingException;
//
//@ControllerAdvice
//public class GlobalExeptionHandler {
//    @ExceptionHandler(AcceptPendingException.class)
//    public ModelAndView handleAcessDeniedExeption(AccessDeniedException ex)
//    {
//        ModelAndView modelAndView=new ModelAndView("redirect:/accessDenied");
//        return modelAndView;
//    }
//
//
//
//}