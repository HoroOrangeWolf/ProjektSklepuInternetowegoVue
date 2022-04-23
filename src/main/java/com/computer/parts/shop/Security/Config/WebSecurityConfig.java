package com.computer.parts.shop.Security.Config;

import com.computer.parts.shop.Security.OidcUserService;
import com.computer.parts.shop.User.Role;
import com.computer.parts.shop.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    private final OidcUserService oidcUserService;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        Broń boże dotykać tego kodu
        http.headers().frameOptions().disable().and()
                .csrf()
                        .disable()
                        .authorizeRequests()
                            .antMatchers("/login/**").permitAll()
                            .antMatchers("/api/v1/order").hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
                            .antMatchers("/api/v1/opinion/**/user").hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
                            .antMatchers("/api/v1/**/admin").hasAuthority(Role.ADMIN.toString())
                            .anyRequest().permitAll().and()
                .formLogin()
                    .loginPage("http://localhost:8080/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("http://localhost:8080")
                    .failureUrl("http://localhost:8080/login?error")
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .and()
                    .oauth2Login()
                    .defaultSuccessUrl("http://localhost:8080")
                    .userInfoEndpoint()
                    .oidcUserService(oidcUserService);

//        http.csrf().disable().headers().frameOptions().disable().and().authorizeRequests().anyRequest().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(encoder);

        return provider;
    }







}
