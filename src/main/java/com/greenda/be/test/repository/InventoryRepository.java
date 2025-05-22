package com.greenda.be.test.repository;

import com.greenda.be.test.entity.Inventory;
import com.greenda.be.test.model.inventory.InventoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("SELECT SUM(i.qty) FROM Inventory i WHERE i.item.id = :itemId AND i.type = :type")
    Integer sumQtyByItemIdAndType(Long itemId, InventoryType type);
}
