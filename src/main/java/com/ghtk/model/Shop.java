package com.ghtk.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "increment")
    private int id;
    private String name;
    private String address;
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "shop")
    @JsonManagedReference
    private List<Staff> staffs;

    @OneToMany(mappedBy = "shop")
    @JsonManagedReference
    private List<Product> products;

    @OneToMany(mappedBy = "shop")
    @JsonManagedReference
    private List<Order> orders;
}
