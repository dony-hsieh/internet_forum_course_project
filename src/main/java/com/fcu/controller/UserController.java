package com.fcu.controller;

import com.fcu.model.EmailVerification;
import com.fcu.model.User;
import com.fcu.model.UserLoginData;
import com.fcu.model.UserRegisterData;
import com.fcu.security.SecurityService;
import com.fcu.service.EmailVerificationService;
import com.fcu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam Map<String, String> loginData,
            HttpServletRequest request,
            Model model
    ) {
        // pack login data and try to get user in database
        UserLoginData userLogin = new UserLoginData(
                loginData.get("username"), loginData.get("password")
        );
        User user = userService.findOneById(userLogin.getUsername());
        if (user == null) {
            model.addAttribute("tip_message", "The user is not existed.");
            return "login";
        }
        // verify enable
        if (!user.isEnable()) {
            model.addAttribute("tip_message", "The user is not verified.");
            return "login";
        }
        // verify password
        boolean passVerification = securityService.verifyPassword(user, userLogin.getPassword());
        if (!passVerification) {
            model.addAttribute("tip_message", "Invalid username or password.");
            return "login";
        }
        securityService.issueNewToken(user, request.getSession());
        return "redirect:/home";
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam Map<String, String> registerData,
            Model model
    ) {
        // pack registration data
        UserRegisterData userRegister = new UserRegisterData(
                registerData.get("username"),
                registerData.get("password"),
                registerData.get("email")
        );
        if (userService.existsById(userRegister.getUsername())) {
            model.addAttribute("tip_message", "The username has been used.");
            return "register";
        }
        // encode password
        userRegister.setPassword(securityService.encodePassword(userRegister.getPassword()));
        User user = new User(
                userRegister.getUsername(),
                userRegister.getPassword(),
                userRegister.getEmail()
        );
        userService.insertOne(user);
        EmailVerification emailVerify = new EmailVerification(user);
        emailVerificationService.insertOne(emailVerify);
        String verifyUrl = "http://localhost:8080/register/verify?token=" + emailVerify.getToken();
        emailVerificationService.sendEmail(
                user.getEmail(),
                "FcuForum | Complete Registration!",
                "To confirm your account, please click here:\n" + verifyUrl
        );
        return "redirect:/register/register_success";
    }

    // TODO: Complete verifyRegister with smtp and token
    @GetMapping("/register/verify")
    public String verifyRegister() {
        return "";
    }

    @GetMapping("/home")
    public String toHome(HttpServletRequest request, Model model) {
        String username = securityService.verifyCurrentUser(request.getSession());
        if (username == null) {
            return "redirect:login";
        }
        model.addAttribute("username", username);
        return "home";
    }
}
