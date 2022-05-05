package com.computer.parts.shop.JWT;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.computer.parts.shop.User.User;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JWTService {

  private static final String secret =
    "eI06ReHIodipKZEvDLyxNBrZYCdmbJTq3xQfcYmO1kmPgIC0G6gc6d3dENAfTlBBFI4sSZfy50qxjTyaE8Jt2rdEaVTWjrXcMjAzvIgldgGAafkTP9WcKLXLh4Y8RJWqUGfZsoet1KHaAMvlLQBmfW";

  public JWToken generateJWT(String userEmail, Long valid) {
    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

    String access_token = com.auth0.jwt.JWT
      .create()
      .withSubject(userEmail)
      .withExpiresAt(new Date(System.currentTimeMillis() + valid))
      .sign(algorithm);

    return new JWToken(access_token, userEmail, false);
  }

  public JWToken verifyTokenAndGetSubject(String token) {
    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
    JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).build();
    try {
      DecodedJWT decodedJWT = verifier.verify(token);

      return new JWToken(token, decodedJWT.getSubject(), false);
    } catch (TokenExpiredException e) {}

    return new JWToken("token", "", true);
  }
}
