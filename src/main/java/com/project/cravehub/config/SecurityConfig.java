
package com.project.cravehub.config;

import com.project.cravehub.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Lazy
    private UserService userService;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeRequests()
                .antMatchers(
                        "/registration**",
                        "/js/**",
                        "/css/**",
                        "/images/**"
                        ,"/productImages/**"
                        ,"/static**"
                        ,"/fonts/**"
                        ,"/registration"
                        ,"/regenerate-otp"
                        ,"/login",
                        "/verify-account"
                        ,"/confirmEmail"
                        ,"/verifyOtp"
                        ,"/singleProduct/**"
                        ,"/"
                        ,"/changePassword"
                        ,"/regenerateForgotOtp"
                        ,"/shop**"
                        ,"/shop/**"
                        ,"/search**"

                ).permitAll()
                //.antMatchers("/").hasAnyRole("USER","ADMIN")
                .antMatchers("/accessDenied").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                //.antMatchers("/").hasRole("USER")
                .antMatchers("/addToCart").authenticated()
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    request.getSession().setAttribute("authenticated", true);
                    // Customize the redirection based on the user's role
                    if (authentication.getAuthorities().stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin/adminHome");
                    }
                    else {
                        response.sendRedirect("/");
                    }

                })
                .and()


                .sessionManagement()
                .maximumSessions(2) // Allow up to 10 concurrent sessions for different users
                .maxSessionsPreventsLogin(false) // Allow new logins even if the session limit is reached
                .expiredUrl("/login?expires") // Redirect to the login page if the session has expired
                ;

    }
    @Bean
    public BlockCheckFilter blockCheckFilter() {
        return new BlockCheckFilter();
    }
}