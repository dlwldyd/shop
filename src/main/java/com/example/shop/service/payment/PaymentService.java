package com.example.shop.service.payment;

import com.example.shop.domain.Order;

import java.io.IOException;

public interface PaymentService {

    /**
     * 클라이언트가 PG 사에 실제 결제한 금액과 클라이언트측 으로부터 온 결제금액 정보를 비교하기 위한 메서드
     * @param pgUid 실제 결제 금액을 알기 위한 PG 사의 결제번호
     * @param totalPrice 클라이언트측 으로부터의 결제금액 정보
     * @return 클라이언트의 alert 창에 띄울 메시지
     * @throws IOException PG 사의 서버나 네트워크 환경 때문에 실제 결제 금액 정보를 받아도지 못함
     */
    String validatePayment(String pgUid, long totalPrice) throws IOException;

    /**
     * 주문에대한 환불을 하는 메서드
     * @param order 환불할 주문
     * @throws IOException PG 사의 서버나 네트워크 환경 때문에 실제 결제 금액 정보를 받아도지 못함
     */
    void cancelPayment(Order order) throws IOException;

    /**
     * 클라이언트가 재고량 이상으로 주문하여 클라이언트에게 결제한 금액을 다시 환불하는 메서드
     * @param pgUid 결제 금액을 알기 위한 PG 사의 결제번호
     * @throws IOException PG 사의 서버나 네트워크 환경 때문에 실제 결제 금액 정보를 받아도지 못함
     */
    void outOfStock(String pgUid) throws IOException;
}
