package com.project.cravehub.model.user;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
//@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name ="last_name")
    private String lastName;

    @Column(name ="username")
    private String userName;
    private String email;
    private  String password;

    private boolean isBlocked;

    @Column(name = "ref_code")
    private String referralCode;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<Address> addresses;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns = @JoinColumn(name ="user_id" ,referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name = "role_id" ,referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Cart cart;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Wishlist wishlist;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_coupon",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id"))
    private Set<Coupon> coupons = new HashSet<>();

    @OneToMany(mappedBy="user",cascade=CascadeType.REMOVE)
    private List<PurchaseOrder> orders;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_transaction",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    private Set<Transactions> transaction = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    public User()
    {
    }

    public User(String email, String password, Collection<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String firstName, String lastName, String userName, String email,
                String password, Collection<Role> roles,String referralCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.referralCode = referralCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

    public List<PurchaseOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<PurchaseOrder> orders) {
        this.orders = orders;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Set<Transactions> getTransaction() {
        return transaction;
    }

    public void setTransaction(Set<Transactions> transaction) {
        this.transaction = transaction;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
