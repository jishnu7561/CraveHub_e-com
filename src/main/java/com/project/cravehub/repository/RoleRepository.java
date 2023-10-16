package com.project.cravehub.repository;

import com.project.cravehub.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

   Optional<Role> findByAuthority(String authority);
}
