package com.example.shop.service;

import com.example.shop.Dtos.cart.CartDto;
import com.example.shop.Dtos.cart.CartItemDto;
import com.example.shop.Dtos.cart.CartOrderDto;
import com.example.shop.Dtos.order.OrderDto;
import com.example.shop.domain.*;
import com.example.shop.repository.cart.CartItemRepository;
import com.example.shop.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final ItemService itemService;
    private final MemberService memberService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    @Transactional
    public CartItem addCart(CartItemDto cartItemDto, String username) {
        Item findItem = itemService.getItem(cartItemDto.getItemId());
        Member findMember = memberService.getMemberByUsername(username);

        Optional<Cart> findCart = cartRepository.findByMemberId(findMember.getId());

        Cart cart = findCart.orElseGet(() -> cartRepository.save((new Cart(findMember))));

        Optional<CartItem> findCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), findItem.getId());

        if (findCartItem.isPresent()) {
            findCartItem.get().addCount(cartItemDto.getCount());
            return findCartItem.get();
        } else {
            CartItem saveCartItem = new CartItem(cart, findItem, cartItemDto.getCount());
            cartItemRepository.save(saveCartItem);
            return saveCartItem;
        }
    }

    @Transactional
    public void updateCartItemCount(Long cartItemId, int count) {

        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        cartItem.orElseThrow(EntityNotFoundException::new).updateCount(count);
    }

    @Transactional
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public Order orderCartItem(List<CartOrderDto> cartOrderDtoList, String username) {
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Order order = orderService.orders(orderDtoList, username);

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return order;
    }

    public List<CartDto> getCartList(String username) {

        Member member = memberService.getMemberByUsername(username);
        Optional<Cart> findCart = cartRepository.findByMemberId(member.getId());

        return findCart.isEmpty() ? new ArrayList<>() : cartItemRepository.findCartDtoList(findCart.get().getId());
    }

    public boolean validateCartItem(Long cartItemId, String username) {
        Member member = memberService.getMemberByUsername(username);
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        Member savedMember = cartItem.orElseThrow(EntityNotFoundException::new).getCart().getMember();

        return StringUtils.equals(member.getUsername(), savedMember.getUsername());
    }
}
