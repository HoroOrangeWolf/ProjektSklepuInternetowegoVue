package com.computer.parts.shop.Security.Config;

import com.computer.parts.shop.Filter.CustomAuthorizationFilter;
import com.computer.parts.shop.JWT.JWTService;
import com.computer.parts.shop.Security.OidcUserService;
import com.computer.parts.shop.User.Role;
import com.computer.parts.shop.User.UserService;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  private final BCryptPasswordEncoder encoder;
  private final OidcUserService oidcUserService;
  private final JWTService jwtService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //        Broń boże dotykać tego kodu
    http
      .addFilterBefore(
        new CustomAuthorizationFilter(userService, jwtService),
        UsernamePasswordAuthenticationFilter.class
      )
      .headers()
      .frameOptions()
      .disable()
      .and()
      .csrf()
      .disable()
      .authorizeRequests()
      .antMatchers("/login/**")
      .permitAll()
      .antMatchers("/api/v1/order")
      .hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
      .antMatchers("/api/v1/opinion/**/user")
      .hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
      .antMatchers("/api/v1/**/admin")
      .hasAuthority(Role.ADMIN.toString())
      .anyRequest()
      .permitAll()
      .and()
      .formLogin()
      .loginPage("http://localhost:8080/login")
      .loginProcessingUrl("/login")
      .defaultSuccessUrl("http://localhost:8080")
      .failureUrl("http://localhost:8080/login?error")
      .and()
      .logout()
      .clearAuthentication(true)
      .logoutUrl("/logout")
      .deleteCookies("JSESSIONID", "access_token", "refresh_token")
      .logoutSuccessHandler(
        (
          (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
          }
        )
      )
      .and()
      .oauth2Login()
      .defaultSuccessUrl("http://localhost:8080")
      .userInfoEndpoint()
      .oidcUserService(oidcUserService);
    //        http.sessionManagement()
    //                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(encoder);

    return provider;
  }
}
