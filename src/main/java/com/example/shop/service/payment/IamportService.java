package com.example.shop.service.payment;

import com.example.shop.domain.Order;
import com.example.shop.exception.PaymentFailException;
import com.example.shop.exception.RefundFailException;
import com.example.shop.service.MemberService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:iamport-api-key.properties")
public class IamportService implements PaymentService {

    private final MemberService memberService;
    private IamportClient client;

    @Value("${apiKey}")
    private String API_KEY;

    @Value("${apiSecret}")
    private String API_SECRET;

    private IamportResponse<Payment> getPaymentData(String impUid) throws IOException {

        if (this.client == null) {
            this.client = new IamportClient(API_KEY, API_SECRET);
        }

        try {
            return client.paymentByImpUid(impUid);
        } catch (IamportResponseException e) {
            throw new PaymentFailException(e.getMessage());
        }
    }

    @Override
    public String validatePayment(String impUid, long totalPrice) throws IOException {
        Payment response = getPaymentData(impUid).getResponse();
        String status = response.getStatus();
        if (response.getAmount().longValue() != totalPrice) {
            throw new PaymentFailException("위조된 결제 시도");
        }
        if ("paid".equals(status)) {
            return "결제에 성공하였습니다.";
        }
        throw new PaymentFailException("결제에 실패하였습니다.");
    }

    @Override
    public void cancelPayment(Order order) throws IOException {

        if (this.client == null) {
            this.client = new IamportClient(API_KEY, API_SECRET);
        }

        CancelData cancelData = new CancelData(order.getImpUid(), true);
        cancelData.setChecksum(BigDecimal.valueOf(order.getTotalPrice()));

        try {
            IamportResponse<Payment> response = client.cancelPaymentByImpUid(cancelData);
            if (response.getCode() != 0) {
                throw new RefundFailException(response.getMessage());
            }
        } catch (IamportResponseException e) {
            throw new RefundFailException("환불 실패");
        }
    }

    @Override
    public void outOfStock(String impUid) throws IOException {

        if (this.client == null) {
            this.client = new IamportClient(API_KEY, API_SECRET);
        }

        CancelData cancelData = new CancelData(impUid, true);

        try {
            IamportResponse<Payment> response = client.cancelPaymentByImpUid(cancelData);
            if (response.getCode() != 0) {
                throw new RefundFailException(response.getMessage());
            }
        } catch (IamportResponseException e) {
            throw new RefundFailException("환불 실패");
        }
    }
}