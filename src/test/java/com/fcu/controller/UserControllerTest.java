package com.fcu.controller;

import com.fcu.model.EmailVerification;
import com.fcu.model.User;
import com.fcu.model.UserLoginData;
import com.fcu.model.UserRegisterData;
import com.fcu.repository.UserRepository;
import com.fcu.security.JwtUtil;
import com.fcu.security.SecurityService;
import com.fcu.service.EmailVerificationService;
import com.fcu.service.UserService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    private static final String username = "fcu0987654";
    private static final String password = "iiNk787a5foPl";
    private static final String email = "d0987654@o365.fcu.edu.tw";

    private Map<String, String> registerData;
    private UserRegisterData userRegister;
    private User user;
    private EmailVerification emailVerify;

    private UserLoginData loginData;


    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    @MockBean
    private EmailVerificationService emailVerificationService;
    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        registerData = new HashMap<>();
        registerData.put("username", username);
        registerData.put("password", password);
        registerData.put("email", email);

        userRegister = new UserRegisterData(username, password, email);
        Mockito.when(userRepo.existsById(userRegister.getUsername())).thenReturn(false);
        Mockito.when(userRepo.existsById(username)).thenReturn(false);
    }

    @Test
    @Order(1)
    void signUpTest() {
        userRegister = new UserRegisterData(
                registerData.get("username"),
                registerData.get("password"),
                registerData.get("email")
        );

        // test data is existed
        Assert.hasLength(userRegister.getUsername());
        Assert.hasLength(userRegister.getPassword());
        Assert.hasLength(userRegister.getEmail());

        // test no existed data in DB
        Assert.isTrue(!userRepo.existsById(userRegister.getUsername()));

        user = new User(
                userRegister.getUsername(),
                securityService.encodePassword(userRegister.getPassword()),
                userRegister.getEmail()
        );
        // test password encoding function
        Assert.isTrue(!user.getPassword().equals(password));
        Assert.isTrue(securityService.verifyPassword(user, password));

        emailVerify = new EmailVerification(user);
        // test email sender function
        emailVerificationService.sendEmail(
                emailVerify.getUser().getEmail(),
                "Test email verification",
                "test email verification {" + emailVerify.getToken() + "}."
        );
        Date curDate = new Date();
        // test expiration time setting of verification token
        Assert.isTrue(emailVerify.getExpired_time().after(curDate));
    }

    @Test
    @Order(2)
    void signInTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        loginData = new UserLoginData(username, password);
        User user = new User(loginData.getUsername(), securityService.encodePassword(password), email);
        user.setEnable(true);
        // test constructor and password encoding
        Assert.isTrue(loginData.getUsername().equals(username));
        Assert.isTrue(user.isEnable());
        Assert.isTrue(securityService.verifyPassword(user, loginData.getPassword()));

        String token = securityService.issueNewToken(user, request.getSession());
        // test issuing token function
        Assert.notNull(token);

        String gotToken = (String) request.getSession().getAttribute("STOKEN");
        // test operation on session attribute
        Assert.notNull(gotToken);
        String gotUsername = jwtUtil.resolveToken(gotToken);
        // test jwt function with correct username
        Assert.notNull(gotUsername);
        Assert.isTrue(gotUsername.equals(user.getUsername()));
    }
}