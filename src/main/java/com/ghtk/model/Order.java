package com.ghtk.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "increment")
    private int id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @JsonBackReference
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    @JsonBackReference
    private Staff staff;

    @ManyToMany
    @JoinTable(name = "order_list",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonBackReference
    private List<Product> products;

}
