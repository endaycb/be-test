package com.greenda.be.test.entity;

import com.greenda.be.test.model.inventory.InventoryType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "T_INVENTORY")
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVENTORY_SEQ")
    @SequenceGenerator(name = "INVENTORY_SEQ", sequenceName = "INVENTORY_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Integer qty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryType type;

}
