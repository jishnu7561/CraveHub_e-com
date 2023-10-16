package com.project.cravehub.dto;

import com.project.cravehub.model.user.User;


public class AddressDto {

    private Integer address_id;
    private User userId;
    private String fullName;
    private String city;
    private String country;
    private String state;
    private String pinCode;
    private Integer number;

    public AddressDto() {
    }

    public AddressDto(User userId, String fullName, String city, String country, String state, String pinCode, Integer number) {
        this.userId = userId;
        this.fullName = fullName;
        this.city = city;
        this.country = country;
        this.state = state;
        this.pinCode = pinCode;
        this.number = number;
    }

    public Integer getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Integer address_id) {
        this.address_id = address_id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }


}
