package com.fcu.service;

import com.fcu.model.EmailVerification;
import com.fcu.repository.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmailVerificationService implements BasicCrudService<EmailVerification, String> {
    @Value("${spring.mail.username}")
    private static String SMTP_USERNAME;

    @Autowired
    private EmailVerificationRepository emailVerificationRepo;
    @Autowired
    private JavaMailSender javaMailSender;

    public EmailVerification findOneById(String token) {
        Optional<EmailVerification> foundEmailVerify = this.emailVerificationRepo.findById(token);
        if (foundEmailVerify.isEmpty()) {
            return null;
        }
        return foundEmailVerify.get();
    }

    public boolean existsById(String token) {
        return emailVerificationRepo.existsById(token);
    }

    public List<EmailVerification> findAllExpiredVerification() {
        return emailVerificationRepo.findAllExpiredVerification();
    }

    public boolean insertOne(EmailVerification emailVerification) {
        if (emailVerification == null ||
                this.emailVerificationRepo.existsById(emailVerification.getToken())
        ) {
            return false;
        }
        this.emailVerificationRepo.save(emailVerification);
        return true;
    }

    public boolean updateOne(EmailVerification emailVerification) {
        if (emailVerification == null ||
                !this.emailVerificationRepo.existsById(emailVerification.getToken())
        ) {
            return false;
        }
        this.emailVerificationRepo.save(emailVerification);
        return true;
    }

    public boolean deleteOne(EmailVerification emailVerification) {
        if (emailVerification == null ||
                !this.emailVerificationRepo.existsById(emailVerification.getToken())
        ) {
            return false;
        }
        this.emailVerificationRepo.delete(emailVerification);
        return true;
    }

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(SMTP_USERNAME);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }
}
