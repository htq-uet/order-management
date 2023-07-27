package com.ghtk.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderListId implements Serializable {

    @Column(name = "order_id")
    private int order_id;

    @Column(name = "product_id")
    private int product_id;

    // Constructors, getters, setters, and equals/hashCode methods
}
