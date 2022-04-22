package com.computer.parts.shop.Registarion;

import com.computer.parts.shop.User.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegistrationRequest registrationRequest){

        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());

        Address address = new Address(
                registrationRequest.getPostCode(),
                registrationRequest.getHomeNumber(),
                registrationRequest.getStreet(),
                registrationRequest.getCity()
        );
        addressRepository.save(address);
        userService.registerUser(new User(
                encodedPassword,
                registrationRequest.getEmail(),
                registrationRequest.getPhoneNumber(),
                Role.USER,
                registrationRequest.getName(),
                registrationRequest.getSurname(),
                registrationRequest.getBirthDay(),
                registrationRequest.getGender(),
                address
       ));
    }

}
