package com.computer.parts.shop.Registarion;

import com.computer.parts.shop.Exceptions.BadRequestException;
import com.computer.parts.shop.User.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    private RegistrationService registrationService;

    @BeforeEach
    public void setUp() {
        registrationService = new RegistrationService(
                new UserService(
                        null,
                        userRepository
                ),
                addressRepository,
                new BCryptPasswordEncoder()
        );
    }

    @Test
    void register() {
        //given
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "pass123",
                "test1@gmail.com",
                Gender.MALE,
                "123-456-789",
                "wojciech",
                "nowak",
                Timestamp.from(Instant.now()),
                "12-456",
                "12a",
                "lesna",
                "kielce"
        );

        //when
        registrationService.register(
                registrationRequest
        );
        //then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());
        User value = argumentCaptor.getValue();

        assertEquals(value.getEmail(), registrationRequest.getEmail());

    }
}