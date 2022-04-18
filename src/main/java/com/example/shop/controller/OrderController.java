package com.example.shop.controller;

import com.example.shop.Dtos.order.OrderDto;
import com.example.shop.Dtos.order.OrderHistDto;
import com.example.shop.builder.ErrorMessageBuilder;
import com.example.shop.domain.Item;
import com.example.shop.domain.Order;
import com.example.shop.exception.DeletedItemException;
import com.example.shop.exception.OutOfStockException;
import com.example.shop.exception.PaymentFailException;
import com.example.shop.exception.RefundFailException;
import com.example.shop.service.ItemService;
import com.example.shop.service.OrderService;
import com.example.shop.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ErrorMessageBuilder errorMessageBuilder;
    private final PaymentService paymentService;

    private final ItemService itemService;

    /**
     * 단일 상품 주문
     */
    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<String> order(@RequestBody @Validated OrderDto orderDto,
                                        BindingResult bindingResult,
                                        Principal principal) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(errorMessageBuilder.buildErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }

        Item item = itemService.getItem(orderDto.getItemId());
        long totalPrice = (long) item.getPrice() * (long) orderDto.getCount();

        try {
            String msg = paymentService.validatePayment(orderDto.getImpUid(), totalPrice);
            orderService.order(orderDto, principal.getName(), totalPrice);
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (OutOfStockException | PaymentFailException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DeletedItemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 주문 내역 화면
     */
    @GetMapping("/orders")
    public String orderHist(@PageableDefault(size = 4) Pageable pageable, Model model, Principal principal) {

        String username = principal.getName();
        Page<OrderHistDto> orderHistDtos = orderService.getOrderList(username, pageable);

        model.addAttribute("orders", orderHistDtos);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    /**
     * 주문 취소
     */
    @PostMapping("/order/{orderId}/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {

        String username = principal.getName();

        if (!orderService.validateOrder(orderId, username)) {
            return new ResponseEntity<>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        try {
            Order order = orderService.getOrderWithOrderItems(orderId);
            paymentService.cancelPayment(order);
            orderService.cancelOrder(order);
            return new ResponseEntity<>("주문이 취소되었습니다.", HttpStatus.OK);
        } catch (RefundFailException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
