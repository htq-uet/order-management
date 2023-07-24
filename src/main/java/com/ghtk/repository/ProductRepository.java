package com.ghtk.repository;

import com.ghtk.model.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM product WHERE name = ?1")
    Product findProductByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM product WHERE id = ?1")
    Product findById(Integer id);

    @Query (
            nativeQuery = true,
            value = """
                    SELECT IF(p.staff_id IS NOT NULL, a.user_id, s.user_id) as user_id
                    FROM product p
                    LEFT JOIN staff a ON p.staff_id = a.id
                    LEFT JOIN shop s ON p.staff_id IS NULL AND p.shop_id = s.id
                    WHERE p.name = :name
                    """
    )
    Integer findCreatorByName(@Param("name") String name);
}
