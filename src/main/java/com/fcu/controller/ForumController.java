package com.fcu.controller;

import com.fcu.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ForumController {
    @Autowired
    private SecurityService securityService;

    @GetMapping("/")
    public String rootSelector(HttpServletRequest request) {
        String username = securityService.verifyCurrentUser(request.getSession());
        if (username == null) {
            return "redirect:login";
        }
        return "redirect:home";
    }

    @GetMapping("/login")
    public String toLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String toRegister() {
        return "register";
    }

    @GetMapping("/register/register_success")
    public String toRegisterSuccess() {
        return "register_success";
    }
}
