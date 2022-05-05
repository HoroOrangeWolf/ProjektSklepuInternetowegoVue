package com.computer.parts.shop.Security;

import com.computer.parts.shop.User.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.AddressStandardClaim;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OidcUserService
  implements OAuth2UserService<OidcUserRequest, OidcUser> {

  private final UserService userService;
  private final AddressService addressService;

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest)
    throws OAuth2AuthenticationException {
    OidcIdToken idToken = userRequest.getIdToken();
    System.out.println("email");
    try {
      return (User) userService.loadUserByUsername(idToken.getClaim("email"));
    } catch (UsernameNotFoundException ignored) {}

    AddressStandardClaim address = idToken.getAddress();

    Address address1 = new Address(
      address.getPostalCode() == null ? "" : address.getPostalCode(),
      address.getStreetAddress() == null ? "" : address.getStreetAddress(),
      "",
      address.getRegion() == null ? "" : address.getRegion()
    );

    addressService.saveAddress(address1);

    Timestamp timestamp = new Timestamp(new Date().getTime());

    try {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

      TemporalAccessor parse = dateTimeFormatter.parse(idToken.getBirthdate());

      LocalDate from = LocalDate.from(parse);

      timestamp =
        new Timestamp(
          from.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        );
    } catch (Exception exception) {}

    User user = new User(
      null,
      idToken.getClaim("email"),
      idToken.getPhoneNumber() == null ? "" : idToken.getPhoneNumber(),
      Role.USER,
      idToken.getGivenName() == null ? "" : idToken.getGivenName(),
      idToken.getClaim("family_name") == null
        ? ""
        : idToken.getClaim("family_name"),
      timestamp,
      Gender.FEMALE,
      address1
    );

    user.setIsSocialAccount(true);
    user.setRole(Role.USER);
    userService.registerUser(user);
    return user;
  }
}
