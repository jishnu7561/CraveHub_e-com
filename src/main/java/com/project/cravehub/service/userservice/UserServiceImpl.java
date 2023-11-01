package com.project.cravehub.service.userservice;

import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.*;
import com.project.cravehub.repository.*;
import com.project.cravehub.service.productservice.ProductService;
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
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartRepository cartRepository;

//    @Autowired
//    private OrderItem orderItem;

    @Autowired
    private OrderItemRepository orderItemRepository;

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

    @Override
    public boolean addProductToCart(Principal principal, ProductDto productDto) {
        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail);
        System.out.println(user);
        if(user != null) {
            Cart cart = user.getCart();

            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                user.setCart(cart);
                cartRepository.save(cart);
            }

            CartItem cartItem = new CartItem();
            System.out.println(productDto.getProductId());
            Optional<Product> isProduct = productRepository.findById(productDto.getProductId());
            if(isProduct.isPresent()) {
                System.out.println("product exist in db");
                Optional<CartItem> productExist = cartItemRepository.findByCartAndProduct(cart, isProduct.get());
                if (productExist.isPresent()) {
                    System.out.println("product already exist in this user cart");
                    return false;//product already exist

                }
                Product product = isProduct.get();
                System.out.println(product);
                cartItem.setProduct(product);
                cartItem.setQuantity(productDto.getQuantity());
                cartItem.setCart(cart);
                cartItem.setPrice(product.getPrice() * productDto.getQuantity());

                cart.getCartItem().add(cartItem);
                userRepository.save(user);
                return true;
            }
           return false;//product not present
        }
        return false;//user not found
    }

    @Override
    public int   updateQuantity(int count, Integer productId,String username) {
        // Retrieve the authenticated user (you might have your own way to do this)
        User user = userRepository.findByEmail(username);// Retrieve the authenticated user (use Spring Security, etc.)

                // Retrieve the product by ID
            Product product = productService.getProductById(productId);

            Cart cart = cartRepository.findByUser(user);
        // Find existing CartItem or create a new one
        Optional<CartItem> cartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if(cartItem.isPresent()) {
            // Update the quantity based on the count (add or subtract)
            int updatedQuantity = cartItem.get().getQuantity() + count;

            // Ensure the quantity doesn't go below 0
            updatedQuantity = Math.max(updatedQuantity, 1);

            // Set the updated quantity
            cartItem.get().setQuantity(updatedQuantity);
           // cartItem.(updatedQuantity);

            // Save the CartItem to the database
            cartItemRepository.save(cartItem.get());


            // Return the updated quantity
            return updatedQuantity;
        }
        return 1;
    }

    @Override
    public double totalPrice(String username) {
        User user = userRepository.findByEmail(username);
        Cart cart = cartRepository.findByUser(user);

        List<CartItem> cartItems =  cartItemRepository.findByCart(cart);
        int totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            int quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();
            Double productPrice = product.getPrice(); // Assuming you have a 'price' field in your Product entity

            // Calculate subtotal for this cart item and add it to the total price
            Double subtotal = quantity * productPrice;
            totalPrice += subtotal;
        }

        return totalPrice;
    }

    @Transactional
    @Override
    public boolean addOrderItems(Cart cart,PurchaseOrder purchaseOrder) {
        try {
            cartRepository.save(cart);
            List<CartItem> cartItems = cartItemRepository.findByCart(cart);


            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setItemCount(cartItem.getQuantity());
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setOrder(purchaseOrder);
                orderItem.setOrderStatus("placed");
                orderItemRepository.save(orderItem);
            }
            Optional<Cart> carts = cartRepository.findById(cart.getCartId());
            if(carts.isPresent()) {
                //cartRepository.deleteById(cart.getCartId());
                cartItemRepository.deleteByCart(cart);
                return true;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return false;
    }

}
