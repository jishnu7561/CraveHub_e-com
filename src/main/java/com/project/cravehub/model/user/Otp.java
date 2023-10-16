package com.project.cravehub.model.user;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    @Column(name = "otp_generated")
    private String otpGenerated;

    @Column(name = "otp_generated_time")
    private LocalDateTime otpGeneratedTime;

    public Otp() {
    }

    public Otp(String email, String otpGenerated, LocalDateTime otpGeneratedTime) {
        this.email = email;
        this.otpGenerated = otpGenerated;
        this.otpGeneratedTime = otpGeneratedTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpGenerated() {
        return otpGenerated;
    }

    public void setOtpGenerated(String otpGenerated) {
        this.otpGenerated = otpGenerated;
    }

    public LocalDateTime getOtpGeneratedTime() {
        return otpGeneratedTime;
    }

    public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
        this.otpGeneratedTime = otpGeneratedTime;
    }

}
