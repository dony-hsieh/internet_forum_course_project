package com.fcu.controller;

import com.fcu.model.User;
import com.fcu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    // for test
//    @GetMapping("/newuser")
//    public String addNewUser(
//            @RequestParam("name") String name,
//            @RequestParam("email") String email,
//            Model model
//    ) {
//        String password = "12345";
//        User user = new User();
//        user.setUsername(name);
//        user.setPassword(password);
//        user.setEmail(email);
//        this.userService.save(user);
//        return "success";
//    }
}
