package com.example.shop.controller;

import com.example.shop.Dtos.order.OrderDto;
import com.example.shop.Dtos.order.OrderHistDto;
import com.example.shop.builder.ErrorMessageBuilder;
import com.example.shop.domain.Order;
import com.example.shop.exception.DeletedItemException;
import com.example.shop.exception.OutOfStockException;
import com.example.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ErrorMessageBuilder errorMessageBuilder;

    /**
     * 단일 상품 주문
     */
    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<String> order(@RequestBody @Validated OrderDto orderDto,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(errorMessageBuilder.buildErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }

        String username = getUsername();

        try {
            Order order = orderService.order(orderDto, username);
            return new ResponseEntity<>(order.getId().toString(), HttpStatus.OK);
        } catch (OutOfStockException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DeletedItemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 주문 내역 화면
     */
    @GetMapping("/orders")
    public String orderHist(@PageableDefault(size = 4) Pageable pageable, Model model) {

        String username = getUsername();
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
    public ResponseEntity<String> cancelOrder(@PathVariable("orderId") Long orderId) {

        String username = getUsername();

        if (!orderService.validateOrder(orderId, username)) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<String>(orderId.toString(), HttpStatus.OK);
    }

    /**
     * 사용자의 아이디를 가져오는 메서드
     * @return 사용자의 아이디
     */
    private String getUsername() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        return username;
    }
}
