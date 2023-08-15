package com.ghtk.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "increment")
    private Integer id;
    private String name;
    private String phone;
    private String status;

    @OneToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Shop shop;

    @OneToMany(mappedBy = "staff")
    private List<Product> products;

    @OneToMany(mappedBy = "staff")
    private List<Order> orders;
}
