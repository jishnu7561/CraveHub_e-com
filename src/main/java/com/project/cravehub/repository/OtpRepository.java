package com.project.cravehub.repository;

import com.project.cravehub.model.user.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp,Integer> {

    Otp findByEmail(String email);
}
