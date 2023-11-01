package com.project.cravehub.dto;

import com.project.cravehub.model.user.Coupon;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;

public class UserRegistrationDto {

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;

    private String otp;

    public UserRegistrationDto()
    {
    }

    public UserRegistrationDto(String firstName, String lastName, String userName,
                               String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


}
