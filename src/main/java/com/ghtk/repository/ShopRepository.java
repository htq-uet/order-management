package com.ghtk.repository;

import com.ghtk.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    @Query(
            nativeQuery = true,
            value = "SELECT s.*" +
                    "from shop s " +
                    "left join user u " +
                    "on s.user_id = u.id " +
                    "where u.username = ?1"
    )
    Shop findShopByUsername(String username) throws RuntimeException;

}
