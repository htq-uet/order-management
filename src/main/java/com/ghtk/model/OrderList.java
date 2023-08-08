package com.ghtk.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.nio.CharBuffer;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderListId.class)
@Table(name = "order_list")
public class OrderList {
    @Id
    @JsonBackReference
    @JoinColumn(name = "order_id")
    private int order_id;

    @Id
    @JsonBackReference
    @JoinColumn(name = "product_id")
    private int product_id;

    @Column(name = "quantity")
    private Integer quantity;


    // Constructors, getters, and setters
}
