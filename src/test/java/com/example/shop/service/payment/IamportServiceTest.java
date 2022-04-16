package com.example.shop.service.payment;

import com.siot.IamportRestClient.exception.IamportResponseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class IamportServiceTest {

    @Autowired
    PaymentService paymentService;

    @Test
    void iamportTest() throws IOException {

        paymentService.outOfStock("ljlkj");
    }
}