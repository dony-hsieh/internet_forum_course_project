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
import java.util.Date;
import java.util.Map;

@Controller
public class UserController {
    private static final String VERIFICATION_SESSION_KEY = "VERIFICATION_TOKEN";
    private static final String EMAIL_SUBJECT = "FcuForum | Complete Registration!";
    private static final String EMAIL_TEXT =
            "To confirm your account, please click here:\nhttp://localhost:8080/register/verify?token=";

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
            Model model,
            HttpSession session
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
        // send email
        emailVerificationService.sendEmail(user.getEmail(), EMAIL_SUBJECT, EMAIL_TEXT + emailVerify.getToken());
        session.setAttribute(VERIFICATION_SESSION_KEY, emailVerify.getToken());
        return "redirect:/register/register_success";
    }

    @GetMapping("/register/verify")
    public String verifyRegister(@RequestParam String token, Model model) {
        // check token is in DB
        EmailVerification emailVerify = emailVerificationService.findOneById(token);
        if (emailVerify == null) {
            model.addAttribute("tip_message", "Invalid verification.");
            return "verify_result";
        }
        // check referenced user is existed
        User user = emailVerify.getUser();
        if (user == null) {
            emailVerificationService.deleteOne(emailVerify);
            model.addAttribute("tip_message", "Invalid verification.");
            return "verify_result";
        }
        // check expired time
        Date expiredDate = emailVerify.getExpired_time();
        Date curDate = new Date();
        if (expiredDate == null) {
            emailVerificationService.deleteOne(emailVerify);
            model.addAttribute("tip_message", "Invalid verification.");
            return "verify_result";
        }
        if (curDate.after(expiredDate)) {
            // expired
            userService.deleteOne(user);
            model.addAttribute("tip_message", "Invalid verification.");
            return "verify_result";
        }
        // valid
        if (!user.isEnable()) {
            user.setEnable(true);
        }
        emailVerificationService.deleteOne(emailVerify);
        // if emailVerify was deleted, the user was also deleted, so insert user data again
        userService.insertOne(user);
        model.addAttribute("tip_message", "Verify successfully.");
        return "verify_result";
    }

    @GetMapping("/register/verify/resend")
    public String resendVerifyEmail(HttpSession session, RedirectAttributes redirectAttributes) {
        // get token
        String verifyToken = (String) session.getAttribute(VERIFICATION_SESSION_KEY);
        if (verifyToken == null) {
            return "redirect:/register";
        }
        // check token valid
        EmailVerification emailVerify = emailVerificationService.findOneById(verifyToken);
        if (emailVerify == null) {
            session.removeAttribute(VERIFICATION_SESSION_KEY);
            session.invalidate();
            return "redirect:/register";
        }
        if (emailVerify.getUser() == null) {
            emailVerificationService.deleteOne(emailVerify);
            session.removeAttribute(VERIFICATION_SESSION_KEY);
            session.invalidate();
            return "redirect:/register";
        }
        // check expiration
        Date curDate = new Date();
        if (curDate.after(emailVerify.getExpired_time())) {
            emailVerificationService.deleteOne(emailVerify);
            session.removeAttribute(VERIFICATION_SESSION_KEY);
            session.invalidate();
            return "redirect:/register";
        }
        // resend
        emailVerificationService.sendEmail(
                emailVerify.getUser().getEmail(), EMAIL_SUBJECT, EMAIL_TEXT + emailVerify.getToken()
        );
        redirectAttributes.addFlashAttribute(
                "tip_message", "The verification email has been sent again to you."
        );
        return "redirect:/register/register_success";
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
