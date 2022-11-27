package com.fcu.security;

import com.fcu.model.User;
import com.fcu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class SecurityService {
    private static final String SESSION_TOKEN_KEY = "STOKEN";

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BcryptUtil bcryptUtil;

    /**
     * Issue a new token with username.
     */
    public void issueNewToken(User user, HttpSession session) {
        if (user == null || session == null) {
            return;
        }
        String token = jwtUtil.generateToken(user.getUsername());
        session.setAttribute(SESSION_TOKEN_KEY, token);
    }

    /**
     * Verify and get username which is resolved by token.
     * @param session http session
     * @return username string
     */
    public String verifyCurrentUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        // get token from session
        String token = (String) session.getAttribute(SESSION_TOKEN_KEY);
        if (token == null) {
            return null;
        }
        // get username with token
        String username = jwtUtil.resolveToken(token);
        if (username == null) {
            return null;
        }
        // check user existed in database
        boolean isUserExisted = userService.existsById(username);
        if (!isUserExisted) {
            return null;
        }
        return username;
    }

    public void eraseSessionToken(HttpSession session) {
        if (session != null) {
            session.removeAttribute(SESSION_TOKEN_KEY);
            session.invalidate();
        }
    }

    public String encodePassword(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        return bcryptUtil.encodePassword(rawPassword);
    }

    public boolean verifyPassword(User user, String rawPassword) {
        if (user == null || rawPassword == null) {
            return false;
        }
        String encodedPassword = user.getPassword();
        if (encodedPassword == null) {
            return false;
        }
        return bcryptUtil.verifyPassword(rawPassword, encodedPassword);
    }
}
