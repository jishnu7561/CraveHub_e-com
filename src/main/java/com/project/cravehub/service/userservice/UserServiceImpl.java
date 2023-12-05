package com.project.cravehub.service.userservice;

import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.*;
import com.project.cravehub.repository.*;
import com.project.cravehub.service.categoryservice.CategoryService;
import com.project.cravehub.service.productservice.ProductService;
import com.project.cravehub.util.EmailReferral;
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
//import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
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
    private EmailReferral emailReferral;

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

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public User save(UserRegistrationDto registrationDto) {
        String referralCode = generateReferralCode();
        System.out.println(referralCode);
        User user = new User(registrationDto.getFirstName(),registrationDto.getLastName(),
                registrationDto.getUserName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                Arrays.asList(new Role("ROLE_USER")),referralCode);
        return user;
    }

    public static String generateReferralCode() {
        UUID uuid = UUID.randomUUID();
        // Extract the first 8 characters from the UUID
        String code = uuid.toString().substring(0, 4);
        return code.toUpperCase();
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

        return "registration Successful";
    }

    @Override
    public boolean verifyAccount(String email, String otp_generated) {
        System.out.println(email);
        Otp otp = otpRepository.findByEmail(email);
        if (otp!=null && otp.getOtpGenerated().equals(otp_generated) && Duration.between(otp.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
            return true;
        }
        else {
            System.out.println("regenerate otp,your verification is failed");
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
         User user = userRepository.findByEmail(email);
        return user == null;
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

        productService.updateIsEnabled();
        categoryService.updateIsEnabled();

        List<CartItem> cartItems =  cartItemRepository.findByCart(cart);
        int totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            Integer quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();
            Double productPrice = 0.0;
            if(product.getProductOffer() != null && product.getProductOffer().isEnabled()){
                productPrice = product.getDiscountedPrice();
            }else {
                productPrice = product.getPrice(); // Assuming you have a 'price' field in your Product entity
            }
            // Calculate subtotal for this cart item and add it to the total price
            Double subtotal = quantity * productPrice;
            totalPrice += subtotal;
        }

        return totalPrice;
    }


//===============  Method for saving ordered items in database and remove them from the cart  =============
    @Override
    @Transactional
    public boolean addOrderItems(Cart cart, PurchaseOrder purchaseOrder) {
        try {
            List<CartItem> cartItems = new ArrayList<>(cart.getCartItem());

            // Saving the cartItems that ordered to the database
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setItemCount(cartItem.getQuantity());
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setOrder(purchaseOrder);
                orderItem.setOrderStatus("placed");
                orderItemRepository.save(orderItem);
            }

                cart.clearCartItems(); // Remove cart items from cart after order is placed
                cartRepository.save(cart);
                return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception Message: " + e.getMessage());
            // Rethrow the exception or throw a new runtime exception
            throw new RuntimeException("Error processing order items", e);
        }
    }

    @Override
    public int getCartItemsCount(User user) {
        Cart cart = cartRepository.findByUser(user);
        int count = 0;
        if (cart != null)
        {
          List<CartItem> cartItemList = cartItemRepository.findByCart(cart);
          for(CartItem cartItem : cartItemList)
          {
              count ++;
          }
        }
        return  count;
    }

    @Override
    public boolean addProductToWishlist(Principal principal, ProductDto productDTO) {
        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail);
        if (user != null) {
            Wishlist wishlist = user.getWishlist();
            if(wishlist == null) {
                wishlist = new Wishlist();
                wishlist.setUser(user);
                wishlistRepository.save(wishlist);
            }
            List<Product> productList = wishlist.getProducts();
            for(Product product : productList)
            {
                if (product.getProductId().equals(productDTO.getProductId())) {
                    return false;
                }
            }
            Product product = productService.getProductById(productDTO.getProductId());
            productList.add(product);
            wishlistRepository.save(wishlist);
            return true;
        }
        return false;
    }

    @Override
    public void deleteFromWishlist(Product productId,String email) {
        User user = userRepository.findByEmail(email);
        Wishlist wishlist = user.getWishlist();
        List<Product> productList = wishlist.getProducts();
        productList.remove(productId);
        wishlistRepository.save(wishlist);
    }

    @Override
    public void createWallet(User user) {
        if (user != null && user.getWallet() == null) {
            Wallet wallet = new Wallet();
            wallet.setUser(user);
            user.setWallet(wallet);

            // Save user and wallet in a single transaction
            userRepository.save(user);
        }
    }

    @Override
    public List<OrderItem> getAllOrderItem(User user) {
       List<PurchaseOrder> purchaseOrderList =  purchaseOrderRepository.findByUser(user);
       List<OrderItem> orderItemList = new ArrayList<>();
       for(PurchaseOrder purchaseOrder : purchaseOrderList) {
           orderItemList.addAll(purchaseOrder.getOrderItems());
       }
       return orderItemList;
    }

    @Override
    public void addTransaction(User user, PurchaseOrder purchaseOrder) {
        Transactions transactions = new Transactions();

        LocalDateTime date = LocalDateTime.now();
        transactions.setTransactionDate(date);
        transactions.setOrderAmount(purchaseOrder.getOrderAmount());
        transactions.setPaymentMethod(purchaseOrder.getPaymentMethod());
        transactions.setPurchaseOrder(purchaseOrder);
        transactions.setPurpose("purchase");
        transactions.getUser().add(user);
        user.getTransaction().add(transactions);

        userRepository.save(user);
        transactionRepository.save(transactions);
    }

    @Override
    public void addTransactionAndRefund(OrderItem orderItem) {
        System.out.println("called the refund");
        Transactions transactions = new Transactions();
        transactions.getUser().add(orderItem.getOrder().getUser());
        LocalDateTime date = LocalDateTime.now();
        transactions.setTransactionDate(date);
        Double discount =0.0;
        if(!orderItem.getOrder().isRefund_used() && orderItem.getOrder().getCoupon() != null)
        {
            discount = orderItem.getOrder().getCoupon().getAmount();
        }
        transactions.setOrderAmount(orderItem.getOrder().getOrderAmount()-discount);
        transactions.setPaymentMethod(orderItem.getOrder().getPaymentMethod());
        transactions.setPurchaseOrder(orderItem.getOrder());
        transactions.setPurpose("refund");
//        Set<Transactions> transactionsSet = new HashSet<>();
        transactionRepository.save(transactions);

// #################       wallet  ######################

        User user1 = orderItem.getOrder().getUser();
        Wallet wallet = user1.getWallet();
        if(wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user1);
            user1.setWallet(wallet);
        }
             wallet.setBalance(wallet.getBalance()+((orderItem.getItemCount() * orderItem.getProduct().getPrice())- discount));
             PurchaseOrder purchaseOrder = orderItem.getOrder();
             purchaseOrder.setRefund_used(true);
             purchaseOrderRepository.save(purchaseOrder);
             walletRepository.save(wallet);
//             transactionsSet.add(transactions);
//             user1.setTransaction(transactionsSet);
            user1.getTransaction().add(transactions);
             userRepository.save(user1);
    }


    @Override
    public void addBalanceToWallet(User user,double totalAmount) {
        Double balance = user.getWallet().getBalance();
        Wallet wallet = user.getWallet();
        wallet.setBalance(balance-totalAmount);
        walletRepository.save(wallet);
    }

    @Override
    public boolean verifyReferralCode(String referralCode, String email) {
        User user = userRepository.findByEmail(email);
        String  referral = user.getReferralCode();
        User referralUser = userRepository.findByReferralCode(referralCode);
        System.out.println(user);
        if (referralUser != null && !referral.equals(referralCode)) {
            Wallet walletOfUser = user.getWallet();
            Wallet walletOfReferralUser = referralUser.getWallet();
            walletOfUser.setBalance(walletOfUser.getBalance()+50);
            walletOfReferralUser.setBalance(walletOfReferralUser.getBalance()+100);
            walletOfUser.setReferralUsed(true);
            walletRepository.save(walletOfUser);
            walletRepository.save(walletOfReferralUser);
            return true;
        }
        return false;
    }

    @Override
    public void createCartForUser(String userEmail) {
        if(userEmail != null) {
            User user = userRepository.findByEmail(userEmail);
            System.out.println(user);

            if (user != null) {
                Cart cart = user.getCart();

                if (cart == null) {
                    cart = new Cart();
                    cart.setUser(user);
                    user.setCart(cart);
                    cartRepository.save(cart);
                }
            }
        }
    }

    @Override
    public void sentReferralLink(String email, Principal principal) throws MessagingException {
        User user = userRepository.findByEmail(principal.getName());
        emailReferral.sendLinkEmail(email,user);
    }

    @Override
    public void addReferralOffer(HttpSession session,String email) {
        String referralCode = session.getAttribute("referralCode").toString();
        User user1 = userRepository.findByEmail(email);

        if(referralCode != null && user1 != null)
        {
            User user = userRepository.findByReferralCode(referralCode);
            if(user != null) {
                user.getWallet().setBalance(user.getWallet().getBalance() + 100);
                user1.getWallet().setBalance(50.0);
                user1.getWallet().setReferralUsed(true);
                userRepository.save(user);
                userRepository.save(user1);
                session.removeAttribute("referralCode");
            }
        }
    }

}
