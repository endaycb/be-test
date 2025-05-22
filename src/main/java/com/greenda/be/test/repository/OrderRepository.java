package com.greenda.be.test.repository;

import com.greenda.be.test.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT SUM(o.qty) FROM Order o WHERE o.item.id = :itemId")
    Integer sumQtyByItemId(Long itemId);

    boolean existsByItemId(Long itemId);

}
