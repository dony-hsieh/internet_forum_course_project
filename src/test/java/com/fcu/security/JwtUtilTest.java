package com.fcu.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtUtilTest {
    private static final String username = "fcu0987654";
    private static final String testKeyString = "riSypuIsJQm7hsmCQnDdFUhJvn88wqfi";
    private static final String errorKeyString = "DHkAhdJ1CNN3T3wXxV2RHvj5lJjqX1IP";
    private static final Key testKey = Keys.hmacShaKeyFor(testKeyString.getBytes());
    private static final Key errorKey = Keys.hmacShaKeyFor(errorKeyString.getBytes());
    private static String[] generatedTokenCase = new String[3];

    private static final String anoUsername = "fcu0812345";
    private static final String anoToken = "eyJhbGciOiJIUzI1NiJ9." +
                        "eyJzdWIiOiJmY3UwODEyMzQ1IiwiaWF0IjoxNjcxMjg4Mjg3LCJle" +
                        "HAiOjE2NzI0OTc4ODcsImlzcyI6IkZDVV9RQUZPUlVNXzExMiJ9." +
                        "8nj561FPHyO4ZpsyVMd09nVNfRJG_xJIT8y_nW_4D6Y";

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {

    }

    @Test
    @Order(1)
    void generateToken() {
        // set 7 days expiration time
        generatedTokenCase[0] = jwtUtil.generateToken(username, testKey, 604800000);
        // set 0 ms expiration, so that it will expire immediately
        generatedTokenCase[1] = jwtUtil.generateToken(username, testKey, 0);
        // use another key (errorKey) to issue the token
        generatedTokenCase[2] = jwtUtil.generateToken(username, errorKey, 604800000);
        Assert.noNullElements(generatedTokenCase);
    }

    @Test
    @Order(2)
    void resolveToken() {
        String gotUsername;

        gotUsername = jwtUtil.resolveToken(generatedTokenCase[0], testKey);
        Assert.notNull(gotUsername);

        gotUsername = jwtUtil.resolveToken(generatedTokenCase[1], testKey);
        Assert.isNull(gotUsername);

        gotUsername = jwtUtil.resolveToken(generatedTokenCase[2], testKey);
        Assert.isNull(gotUsername);

        gotUsername = jwtUtil.resolveToken(anoToken, testKey);
        Assert.isTrue(gotUsername.equals(anoUsername));
    }
}