package com.project.cravehub.util;

import javax.mail.MessagingException;

import com.project.cravehub.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailReferral {
    @Autowired
    private JavaMailSender javaMailSender;


    public void sendLinkEmail(String email, User user) {
        String registrationLink = "http://localhost:8081/registration";
        String referralCode = user.getReferralCode();

        // You can customize the email body as per your requirements
        String emailBody = "Hello,\n\n"
                + "Thank you for using our service! To register, please click on the following link:\n"
                + registrationLink + "?referralCode=" + user.getReferralCode() + "\n\n"
                + "Your referral code is: " + referralCode;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Referral Link");
        simpleMailMessage.setText(emailBody);

        javaMailSender.send(simpleMailMessage);
    }

}