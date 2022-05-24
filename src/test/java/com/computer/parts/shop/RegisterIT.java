package com.computer.parts.shop;

import com.computer.parts.shop.Registarion.RegistrationRequest;
import com.computer.parts.shop.User.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.apache.qpid.proton.engine.TransportResultFactory.ok;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class RegisterIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @AfterEach
    public void clear(){
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    public void registerUser() throws Exception {
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
        ResultActions perform = mockMvc.perform(
                post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(registrationRequest))
        );
        //then
        perform.andExpect(status().isOk());
        Optional<User> byEmail = userRepository.findByEmail("test1@gmail.com");
        assertTrue(byEmail.isPresent());

    }

    @Test
    public void registerUserIfUserExists() throws Exception {
        //given
        Address address = new Address(
                "",
                "",
                "",
                ""
        );

        addressRepository.save(address);

        User user = new User(
                null,
                "test1@gmail.com",
                "123-456-789",
                Role.USER,
                "wojciech",
                "nowak",
                Timestamp.from(Instant.now()),
                Gender.MALE,
                address
        );

        userRepository.save(user);

        RegistrationRequest registrationRequest = new RegistrationRequest(
                "pass12345",
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
        try{
            mockMvc.perform(
                    post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON).
                            content(objectMapper.writeValueAsString(registrationRequest))
            );
        }catch (Exception exception){
            Throwable cause = exception.getCause();
            assertTrue(cause instanceof org.springframework.dao.DataIntegrityViolationException);
        }

        //then

    }
}
