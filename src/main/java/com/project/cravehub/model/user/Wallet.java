package com.project.cravehub.model.user;

import javax.persistence.*;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer walletId;

    @Column(name = "balance")
    private Double balance = 0.0;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean referralUsed = false;

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isReferralUsed() {
        return referralUsed;
    }

    public void setReferralUsed(boolean referralUsed) {
        this.referralUsed = referralUsed;
    }
}
