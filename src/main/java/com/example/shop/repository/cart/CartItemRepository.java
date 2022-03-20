package com.example.shop.repository.cart;

import com.example.shop.Dtos.cart.CartDto;
import com.example.shop.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.example.shop.Dtos.cart.CartDto(ci.id, i.itemName, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where im.item.id = ci.item.id and ci.cart.id = :cartId and im.repImg = true")
    List<CartDto> findCartDtoList(@Param("cartId") Long cartId);
}
