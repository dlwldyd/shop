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

    /**
     * 장바구니에 담기,
     * 해당 상품이 장바구니에 담겨있으면 개수만 늘리고 없으면 엔티티 생성 후 저장함
     * @param cartItemDto 상품 id, 상품 개수가 담긴 DTO
     * @param username 사용자 아이디
     * @return 데이터베이스에 저장한, 혹은 개수를 늘린 CartItem 엔티티
     */
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

    /**
     * 장바구니에 담겨있는 상품의 개수를 수정함
     * @param cartItemId CartItem 엔티티의 id
     * @param count 상품 개수
     */
    @Transactional
    public void updateCartItemCount(Long cartItemId, int count) {

        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        cartItem.orElseThrow(EntityNotFoundException::new).updateCount(count);
    }

    /**
     * 장바구니에 담긴 상품을 삭제함
     * @param cartItemId CartItem 엔티티의 id
     */
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    /**
     * 장바구니에 담긴 상품을 주문함
     * @param cartOrderDtoList 주문할 상품의 id 리스트
     * @param username 사용자 아이디
     * @return 주문한 결과 생성된 주문 엔티티
     */
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

    /**
     * 사용자의 이름으로 주문 내역을 검색
     * @param username 사용자 아이디
     * @return 사용자의 주문내역 에 대한 정보를 담은 DTO 리스트
     */
    public List<CartDto> getCartList(String username) {

        Member member = memberService.getMemberByUsername(username);
        Optional<Cart> findCart = cartRepository.findByMemberId(member.getId());

        return findCart.isEmpty() ? new ArrayList<>() : cartItemRepository.findCartDtoList(findCart.get().getId());
    }

    /**
     * 현재 사용자가 CartItem 엔티티를 수정할 수 있는지 검사함
     * @param cartItemId 수정할 CartItem 엔티티의 id
     * @param username 사용자 아이디
     * @return true 면 수정 가능, false 면 수정 불가
     */
    public boolean validateCartItem(Long cartItemId, String username) {
        Member member = memberService.getMemberByUsername(username);
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        Member savedMember = cartItem.orElseThrow(EntityNotFoundException::new).getCart().getMember();

        return StringUtils.equals(member.getUsername(), savedMember.getUsername());
    }
}
