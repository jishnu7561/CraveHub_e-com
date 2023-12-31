package com.project.cravehub.repository;

import com.project.cravehub.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmail(String email);

    List<User> findByEmailContainingOrFirstNameContaining(String email, String firstName);

    User findByReferralCode(String referralCode);

//    Optional<User> findByUsername(String username);
}
