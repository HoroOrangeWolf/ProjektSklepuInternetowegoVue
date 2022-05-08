package com.computer.parts.shop.Filter;

import com.computer.parts.shop.JWT.JWTService;
import com.computer.parts.shop.JWT.JWToken;
import com.computer.parts.shop.User.User;
import com.computer.parts.shop.User.UserService;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
@Order
public class CustomAuthorizationFilter extends OncePerRequestFilter {

  private final UserService userService;
  private final JWTService jwtService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    if (
      request.getServletPath().equals("/login") ||
      request.getServletPath().equals("/register")
    ) {
      filterChain.doFilter(request, response);
    } else {
      Cookie[] cookies = request.getCookies();
      Optional<Cookie> optional_access_token;
      Optional<Cookie> optional_refresh_token;
      if (cookies == null) {
        optional_access_token = Optional.empty();
        optional_refresh_token = Optional.empty();
      } else {
        optional_access_token =
          Arrays
            .stream(cookies)
            .filter(f -> f.getName().equals("access_token"))
            .findFirst();
        optional_refresh_token =
          Arrays
            .stream(cookies)
            .filter(f -> f.getName().equals("refresh_token"))
            .findFirst();
      }

      Authentication authentication = SecurityContextHolder
        .getContext()
        .getAuthentication();

      if (optional_access_token.isEmpty() || optional_refresh_token.isEmpty()) {
        if (authentication != null) {
          User user = (User) authentication.getPrincipal();
          String access_token = jwtService
            .generateJWT(user.getEmail(), 1000 * 60 * 60 * 24L)
            .getToken();
          String refresh_token = jwtService
            .generateJWT(user.getEmail(), 1000 * 60 * 60 * 24L * 30)
            .getToken();

          Cookie cookie_token = new Cookie("access_token", access_token);
          cookie_token.setHttpOnly(true);
          cookie_token.setMaxAge(Integer.MAX_VALUE);
          cookie_token.setDomain("localhost");
          cookie_token.setPath("/");

          Cookie cookie_refresh_token = new Cookie(
            "refresh_token",
            refresh_token
          );
          cookie_refresh_token.setHttpOnly(true);
          cookie_refresh_token.setMaxAge(Integer.MAX_VALUE);
          cookie_refresh_token.setDomain("localhost");
          cookie_refresh_token.setPath("/");

          response.addCookie(cookie_token);
          response.addCookie(cookie_refresh_token);
          filterChain.doFilter(request, response);
          return;
        }
        filterChain.doFilter(request, response);
        return;
      }
      JWToken access_token = jwtService.verifyTokenAndGetSubject(
        optional_access_token.get().getValue()
      );
      JWToken refresh_token = jwtService.verifyTokenAndGetSubject(
        optional_refresh_token.get().getValue()
      );

      if (refresh_token.getIsExpired()) {
        SecurityContextHolder.getContext().setAuthentication(null);
        Arrays
          .stream(cookies)
          .forEach(f -> {
            Cookie cookie = new Cookie(f.getName(), null);
            cookie.setMaxAge(0);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
          });
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
      } else if (access_token.getIsExpired()) {
        access_token =
          jwtService.generateJWT(
            refresh_token.getEmail(),
            1000 * 60 * 60 * 24L
          );
        refresh_token =
          jwtService.generateJWT(
            refresh_token.getEmail(),
            1000 * 60 * 60 * 24 * 7L
          );

        Cookie cookie_token = new Cookie(
          "access_token",
          access_token.getToken()
        );
        cookie_token.setHttpOnly(true);
        cookie_token.setMaxAge(Integer.MAX_VALUE);
        cookie_token.setDomain("localhost");
        cookie_token.setPath("/");

        Cookie cookie_refresh_token = new Cookie(
          "refresh_token",
          refresh_token.getToken()
        );
        cookie_refresh_token.setHttpOnly(true);
        cookie_refresh_token.setMaxAge(Integer.MAX_VALUE);
        cookie_refresh_token.setDomain("localhost");
        cookie_refresh_token.setPath("/");
      }

      String subject = access_token.getEmail();

      if (authentication == null) {
        UserDetails userDetails = userService.loadUserByUsername(subject);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );

        SecurityContextHolder
          .getContext()
          .setAuthentication(usernamePasswordAuthenticationToken);
      } else {
        User user = (User) authentication.getPrincipal();

        if (!Objects.equals(user.getEmail(), subject)) {
          response.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
      }

      filterChain.doFilter(request, response);
    }
  }
}
