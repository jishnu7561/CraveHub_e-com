package com.project.cravehub.service.userservice;

import com.project.cravehub.dto.AddressDto;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.Otp;
import com.project.cravehub.model.user.Role;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.OtpRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.util.EmailUtil;
import com.project.cravehub.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirstName(),registrationDto.getLastName(), registrationDto.getUserName(), registrationDto.getEmail(), passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_USER")));
        return user;
    }

    @Override
    public String register(UserRegistrationDto registrationDto) {
        String otp_generated = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(registrationDto.getEmail(), otp_generated);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        String email = registrationDto.getEmail();

        Otp newOtp = otpRepository.findByEmail(email);
        if(newOtp==null) {
            newOtp = new Otp(email, otp_generated, LocalDateTime.now());
        }
        newOtp.setOtpGenerated(otp_generated);
        newOtp.setOtpGeneratedTime(LocalDateTime.now());
        otpRepository.save(newOtp);

//        Otp otp = new Otp();
//        otp.setEmail(registrationDto.getEmail());
//        otp.setOtpGenerated(otp_generated);
//        otp.setOtpGeneratedTime(LocalDateTime.now());
//        otpRepository.save(otp);
        return "registration Successfull";
    }

    @Override
    public boolean verifyAccount(String email, String otp_generated) {
        System.out.println(email);
        Otp otp = otpRepository.findByEmail(email);
        if (otp!=null && otp.getOtpGenerated().equals(otp_generated) && Duration.between(otp.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (2 * 60)) {
            return true;
        }
        else {
            System.out.println("regenerate otp,your vefication is failed");
            return false;
        }
    }


    @Override
    public void regenerateOtp(String email) {
        Otp otp = otpRepository.findByEmail(email);
        String otp_generated = otpUtil.generateOtp();
            try {
                emailUtil.sendOtpEmail(email, otp_generated);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send otp please try again");
            }
        otp.setOtpGenerated(otp_generated);
        otp.setOtpGeneratedTime(LocalDateTime.now());
        otpRepository.save(otp);
    }


    @Override
    public boolean isValidEmailId(String email) {
        return userRepository.findByEmail(email) == null;
    }



    @Override
    public void blockUser(Integer id) {
        User lockCustomer = userRepository.findById(id).get();
        lockCustomer.setBlocked(true);
        userRepository.save(lockCustomer);
    }

    @Override
    public void unblockUser(Integer id) {
        User lockCustomer = userRepository.findById(id).get();
        lockCustomer.setBlocked(false);
        userRepository.save(lockCustomer);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if( user == null )
        {
            throw new UsernameNotFoundException("Invalid user name");
        }
        if(user.isBlocked()){
            throw new DisabledException("User is blocked");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles)
    {
        return roles.stream().map(role-> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }


    @Override
    public List<User> getUsersByPartialEmailOrName(String searchTerm) {
        return userRepository.findByEmailContainingOrFirstNameContaining(searchTerm, searchTerm);
    }

    @Override
    public void sentOtp(String email) {
        String otp_generated = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp_generated);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }

        Otp newOtp = otpRepository.findByEmail(email);
        if(newOtp==null) {
            newOtp = new Otp(email, otp_generated, LocalDateTime.now());
        }
        newOtp.setOtpGenerated(otp_generated);
        newOtp.setOtpGeneratedTime(LocalDateTime.now());
        otpRepository.save(newOtp);
    }



}
