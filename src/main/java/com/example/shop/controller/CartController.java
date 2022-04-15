package com.example.shop.controller;

import com.example.shop.Dtos.cart.CartDto;
import com.example.shop.Dtos.cart.CartItemDto;
import com.example.shop.Dtos.cart.CartOrderDto;
import com.example.shop.Dtos.member.PaymentInfoDto;
import com.example.shop.builder.ErrorMessageBuilder;
import com.example.shop.domain.CartItem;
import com.example.shop.domain.Member;
import com.example.shop.exception.PaymentFailException;
import com.example.shop.repository.cart.CartItemRepository;
import com.example.shop.service.CartService;
import com.example.shop.service.MemberService;
import com.example.shop.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ErrorMessageBuilder errorMessageBuilder;

    private final PaymentService paymentService;

    private final CartItemRepository cartItemRepository;

    private final MemberService memberService;

    /**
     * 장바구니 페이지
     */
    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {

        String username = principal.getName();
        List<CartDto> cartList = cartService.getCartList(username);
        Member findMember = memberService.getMemberByUsername(username);

        int orderTotalPrice = 0;
        for (CartDto cartDto : cartList) {
            orderTotalPrice += cartDto.getPrice() * cartDto.getCount();
        }

        model.addAttribute("cartItems", cartList);
        model.addAttribute("orderTotalPrice", orderTotalPrice);
        model.addAttribute("paymentInfoDto", PaymentInfoDto.of(findMember));
        return "cart/cartList";
    }

    /**
     * 장바구니에 담기
     */
    @PostMapping("/cart")
    @ResponseBody
    public ResponseEntity<String> addCart(@RequestBody @Validated CartItemDto cartItemDto,
                                          BindingResult bindingResult,
                                          Principal principal) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(errorMessageBuilder.buildErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }

        String username = principal.getName();

        cartService.addCart(cartItemDto, username);

        return new ResponseEntity<>("상품을 장바구니에 담았습니다.", HttpStatus.OK);
    }

    /**
     * 장바구니에 담긴 상품 수량 조절
     */
    @PatchMapping("/cartItem/{cartItemId}")
    @ResponseBody
    public ResponseEntity<String> updateCartItem(@PathVariable Long cartItemId, int count, Principal principal) {

        String username = principal.getName();

        if (count <= 0) {
            return new ResponseEntity<>("장바구니에는 1개 이상 담아야 합니다.", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(cartItemId, username)) {
            return new ResponseEntity<>("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<>("장바구니에 상품을 담았습니다.", HttpStatus.OK);
    }

    /**
     * 장바구니에서 상품 삭제
     */
    @DeleteMapping("/cartItem/{cartItemId}")
    @ResponseBody
    public ResponseEntity<String> deleteCartItem(@PathVariable Long cartItemId, Principal principal) {

        String username = principal.getName();

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
    public ResponseEntity<String> orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal) {

        String username = principal.getName();

        List<Long> cartItemIdList = cartOrderDto.getCartItemIdList();

        if (cartItemIdList == null || cartItemIdList.size() == 0) {
            return new ResponseEntity<>("장바구니에 상품이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        for (Long cartItemId : cartItemIdList) {
            if (!cartService.validateCartItem(cartItemId, username)) {
                return new ResponseEntity<>("권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        long totalPrice = getTotalPrice(cartItemIdList);

        try {
            String msg = paymentService.validatePayment(cartOrderDto.getImpUid(), totalPrice);
            cartService.orderCartItem(cartOrderDto, username, totalPrice);
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (PaymentFailException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private long getTotalPrice(List<Long> cartItemIdList) {
        long totalPrice = 0;
        List<CartItem> cartItemList = cartItemRepository.findCartItemListWithItemById(cartItemIdList);
        for (CartItem cartItem : cartItemList) {
            totalPrice += (long) cartItem.getItem().getPrice() * cartItem.getCount();
        }
        return totalPrice;
    }
}
