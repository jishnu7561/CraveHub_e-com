package com.project.cravehub.config;


import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.categoryservice.CategoryService;
import com.project.cravehub.service.productservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class BlockCheckFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            if (username != null) {
                User user = userRepository.findByEmail(username);

                if (user != null && user.isBlocked()) {
                    // Redirect to an error page or perform another action
                    response.sendRedirect("/logout");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }


    // Other methods
}

