package com.fcu.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "EmailVerification", schema = "forum")
public class EmailVerification {
    private static final long EXPIRED_TIME_INTERVAL = 600000;  // 10 minutes

    @Id
    @Column(name = "token", nullable = false, length = 36, unique = true)
    private String token;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "expired_time", nullable = false)
    private Date expired_time;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    public EmailVerification() {

    }

    public EmailVerification(User user) {
        Calendar curDate = Calendar.getInstance();
        this.token = UUID.randomUUID().toString();
        this.expired_time = new Date(curDate.getTimeInMillis() + EXPIRED_TIME_INTERVAL);
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Date expired_time) {
        this.expired_time = expired_time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
