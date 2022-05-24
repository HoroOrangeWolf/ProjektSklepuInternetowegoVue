package com.computer.parts.shop.Opinion;

import com.computer.parts.shop.Product.Product;
import com.computer.parts.shop.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OpinionServiceTest {

    @Mock
    private OpinionRepository opinionRepository;

    private OpinionService opinionService;

    @BeforeEach
    public void setUp(){
        opinionService = new OpinionService(
                opinionRepository
        );
    }

    @Test
    void addOpinion() {
        //given
        Opinion opinion = new Opinion(
                (short) 1,
                "Super",
                new Product(),
                new User()
        );
        //when
        opinionService.addOpinion(opinion);
        //then
        ArgumentCaptor<Opinion> argumentCaptor = ArgumentCaptor.forClass(Opinion.class);
        verify(opinionRepository).save(argumentCaptor.capture());
        Opinion value = argumentCaptor.getValue();

        assertEquals(value, opinion);
    }



}