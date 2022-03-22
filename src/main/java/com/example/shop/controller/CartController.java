package com.example.shop.controller;

import com.example.shop.Dtos.cart.CartDto;
import com.example.shop.Dtos.cart.CartItemDto;
import com.example.shop.Dtos.cart.CartOrderDto;
import com.example.shop.domain.CartItem;
import com.example.shop.domain.Order;
import com.example.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 페이지
     */
    @GetMapping("/cart")
    public String cart(Model model) {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        List<CartDto> cartList = cartService.getCartList(username);

        int orderTotalPrice = 0;
        for (CartDto cartDto : cartList) {
            orderTotalPrice += cartDto.getPrice() * cartDto.getCount();
        }

        model.addAttribute("cartItems", cartList);
        model.addAttribute("orderTotalPrice", orderTotalPrice);
        return "cart/cartList";
    }

    /**
     * 장바구니에 담기
     */
    @PostMapping("/cart")
    @ResponseBody
    public ResponseEntity<String> addCart(@RequestBody @Validated CartItemDto cartItemDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        CartItem cartItem = cartService.addCart(cartItemDto, username);

        return new ResponseEntity<String>(cartItem.getId().toString(), HttpStatus.OK);
    }

    /**
     * 장바구니에 담긴 상품 수량 조절
     */
    @PatchMapping("/cartItem/{cartItemId}")
    @ResponseBody
    public ResponseEntity<String> updateCartItem(@PathVariable Long cartItemId, int count) {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        if (count <= 0) {
            return new ResponseEntity<String>("장바구니에는 1개 이상 담아야 합니다.", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(cartItemId, username)) {
            return new ResponseEntity<String>("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<String>(cartItemId.toString(), HttpStatus.OK);
    }

    /**
     * 장바구니에서 상품 삭제
     */
    @DeleteMapping("/cartItem/{cartItemId}")
    @ResponseBody
    public ResponseEntity<String> deleteCartItem(@PathVariable Long cartItemId) {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        if (!cartService.validateCartItem(cartItemId, username)) {
            return new ResponseEntity<>("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<>(cartItemId.toString(), HttpStatus.OK);
    }

    /**
     * 장바구니에 담긴 상품 주문
     */
    @PostMapping("/cart/orders")
    @ResponseBody
    public ResponseEntity<String> orderCartItem(@RequestBody CartOrderDto cartOrderDto) {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
            return new ResponseEntity<>("장바구니에 상품이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        for (CartOrderDto cartOrder : cartOrderDtoList) {
            if (!cartService.validateCartItem(cartOrder.getCartItemId(), username)) {
                return new ResponseEntity<>("권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        Order order = cartService.orderCartItem(cartOrderDtoList, username);
        return new ResponseEntity<>(order.getId().toString(), HttpStatus.OK);
    }
}
