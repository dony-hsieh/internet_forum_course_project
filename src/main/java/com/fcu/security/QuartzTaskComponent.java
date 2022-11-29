package com.fcu.security;

import com.fcu.model.EmailVerification;
import com.fcu.service.EmailVerificationService;
import com.fcu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuartzTaskComponent {
    private static final long SCHEDULING_DELAY_TIME = 300000;  // 5 minutes

    @Autowired
    private EmailVerificationService emailVerificationService;
    @Autowired
    private UserService userService;

    /**
     * Every 5 minutes, delete expired token from DB.
     * Scheduled by Quartz.
     */
    @Scheduled(fixedDelay = SCHEDULING_DELAY_TIME)
    public void checkExpiredEmailVerification() {
        // get expired verification
        List<EmailVerification> expiredVerify = emailVerificationService.findAllExpiredVerification();
        if (expiredVerify == null || expiredVerify.size() == 0) {
            return;
        }
        // delete expired verification
        for (EmailVerification ev: expiredVerify) {
            emailVerificationService.deleteOne(ev);
            if (ev.getUser().isEnable()) {
                userService.insertOne(ev.getUser());
            }
        }
    }
}
