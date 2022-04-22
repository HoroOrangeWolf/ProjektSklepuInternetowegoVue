package com.computer.parts.shop.Security;

import com.computer.parts.shop.User.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.AddressStandardClaim;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@AllArgsConstructor
public class OidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser>{

    private final UserService userService;
    private final AddressService addressService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcIdToken idToken = userRequest.getIdToken();
        System.out.println("email");
        try{
            return (User) userService.loadUserByUsername(idToken.getClaim("email"));
        }catch (UsernameNotFoundException ignored){

        }
        //:TODO Zrobić pobieranie adresu ~sprawdzić czy działa

        AddressStandardClaim address = idToken.getAddress();

        Address address1 = new Address(
                " 1",
                " 1",
                " 1",
                " 1");

        addressService.saveAddress(address1);

        User user = new User(
                null,
                idToken.getClaim("email"),
                idToken.getPhoneNumber(),
                Role.USER,
                idToken.getGivenName(),
                idToken.getClaim("family_name"),
                new Timestamp(new Date().getTime()),
                Gender.FEMALE,
                address1
        );

        user.setIsSocialAccount(true);
        user.setRole(Role.USER);
        userService.registerUser(user);
        return user;
    }
}
