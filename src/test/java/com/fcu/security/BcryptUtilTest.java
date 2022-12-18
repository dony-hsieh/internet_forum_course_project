package com.fcu.security;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BcryptUtilTest {
    @Autowired
    private BcryptUtil bcryptUtil;

    private static final String[] rawPasswords = new String[]{
            "ghSh7Uwz",
            "Byv7XqWNnzy7",
            "BGwsM2acxcq84HYn6QsQ",
            "ghSh7  Uwz",
            " Byv7XqWNnzy7 ",
            "BGwsM2acxc_ q84HYn6QsQ"
    };
    private static final int bcryptEncodingLength = 60;

    @BeforeEach
    void setUp() {

    }

    @Test
    void encodeAndVerifyPassword() {
        String encode_str;
        String[] encodedPasswords = new String[rawPasswords.length];
        Arrays.fill(encodedPasswords, "");
        for (int i = 0; i < rawPasswords.length; i++) {
            encode_str = bcryptUtil.encodePassword(rawPasswords[i]);
            Assert.isTrue(encode_str.length() == bcryptEncodingLength);
            encodedPasswords[i] = encode_str;
        }
        for (int i = 0; i < rawPasswords.length; i++) {
            Assert.isTrue(bcryptUtil.verifyPassword(rawPasswords[i], encodedPasswords[i]));
        }
    }
}