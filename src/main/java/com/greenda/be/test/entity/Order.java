package com.greenda.be.test.entity;

import com.greenda.be.test.repository.generator.OrderSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "T_ORDER")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private String orderNo;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Integer price;

    @PrePersist
    public void generateOrderNo() {
        if (this.orderNo == null) {
            this.orderNo = "O".concat(String.valueOf(OrderSequenceGenerator.next()));
        }
    }
}
