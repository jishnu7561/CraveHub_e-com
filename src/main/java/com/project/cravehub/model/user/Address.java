package com.project.cravehub.model.user;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Address_Id")
    private Integer address_id;
    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User userId;
    @Column(name = "FullName")
    private String fullName;
    @Column(name = "City")
    private String city;
    @Column(name = "Country")
    private String country;
    @Column(name = "State")
    private String state;
    @Column(name = "pin_code")
    private String pinCode;
    @Column(name = "Mobile_Number")
    private String number;

    public Address(User userId, String fullName, String city, String country, String state, String pinCode, String number) {
        this.userId = userId;
        this.fullName = fullName;
        this.city = city;
        this.country = country;
        this.state = state;
        this.pinCode = pinCode;
        this.number = number;
    }

    public Address() {

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
