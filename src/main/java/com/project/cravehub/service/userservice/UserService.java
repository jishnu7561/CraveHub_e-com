package com.project.cravehub.service.userservice;

import com.project.cravehub.dto.AddressDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);

    String register(UserRegistrationDto registrationDto);

    boolean verifyAccount(String email, String otp_generated);

    void regenerateOtp(String email);

    boolean isValidEmailId(String email);

    public void blockUser(Integer id);


    public void unblockUser(Integer id);


    List<User> getUsersByPartialEmailOrName(String searchTerm);


    void sentOtp(String email);


}
