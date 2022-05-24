package com.computer.parts.shop.User;

import com.computer.parts.shop.Exceptions.BadRequestException;
import com.computer.parts.shop.Order.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository repository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService(orderRepository, repository);
    }

    @AfterEach
    void cleanAll() throws Exception {
    }

    @Test
    void getUserByEmail() {
        //given
        Address address = new Address(
                "",
                "",
                "",
                ""
        );

        User user = new User(
                "pass",
                "test1@gmail.com",
                "",
                Role.USER,
                "Wojciech",
                "Nowak",
                Timestamp.from(Instant.now()),
                Gender.MALE,
                address
        );


        //when
        userService.getUserByEmail(user.getEmail());
        //then
        verify(repository).findByEmail(user.getEmail());
    }

    @Test
    void blockUser() {
        //given
        Address address = new Address(
                "",
                "",
                "",
                ""
        );

        User user = new User(
                "pass",
                "test1@gmail.com",
                "",
                Role.USER,
                "Wojciech",
                "Nowak",
                Timestamp.from(Instant.now()),
                Gender.MALE,
                address
        );
        given(repository.findById(1L)).willReturn(Optional.of(user));
        //when
        userService.blockUser(1L, true);

        //then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(repository).save(argumentCaptor.capture());
        User captured = argumentCaptor.getValue();
        Boolean isEnabled = captured.getIsEnabled();

        assertTrue(isEnabled);
    }

    @Test
    void blockUserIfUserNotFound() {
        //given
        given(repository.findById(1L)).willReturn(Optional.empty());
        //when
        assertThrows(BadRequestException.class, () -> userService.blockUser(1L, true), "User not found");
        //then

    }
}