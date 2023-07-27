package com.ghtk.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.CharBuffer;

@Entity
@Data
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
    private int quantity;


    // Constructors, getters, and setters
}
