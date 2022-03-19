package com.example.shop.repository.order;

import com.example.shop.domain.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o join fetch o.member m where m.username = :username order by o.orderDate desc")
    List<Order> findOrders(@Param("username") String username, Pageable pageable);

    @Query("select count(o) from Order o where o.member.username = :username")
    Long countOrder(@Param("username") String username);
}
