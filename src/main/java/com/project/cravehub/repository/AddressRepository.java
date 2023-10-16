package com.project.cravehub.repository;

import com.project.cravehub.model.user.Address;
import com.project.cravehub.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Integer> {
    List<Address> findByUserId(User userId);
}
