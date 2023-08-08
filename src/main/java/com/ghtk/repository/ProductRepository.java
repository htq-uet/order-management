package com.ghtk.repository;

import com.ghtk.model.DTO.ProductDTO;
import com.ghtk.model.DTO.TotalProductDTO;
import com.ghtk.model.Product;
import com.ghtk.model.Shop;
import com.ghtk.model.Staff;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    @Query(
            nativeQuery = true,
            value = """
            SELECT *
            FROM product
            WHERE staff_id = ?1
"""
    )
    Product findAllByStaff(Integer id);

    @Query(
            nativeQuery = true,
            value = """
            SELECT p.id, p.name, p.price, p.created_at, p.updated_at
            FROM product p
            WHERE shop_id = ?1
"""
    )
    List<ProductDTO> findAllByShop(Integer id);

    @Query(
            nativeQuery = true,
            value = """
            SELECT COUNT(*) as total FROM product
            WHERE shop_id = ?4
              AND (YEAR(created_at) = ?1 OR ?1 IS NULL )
              AND (MONTH(created_at) = ?2 OR ?2 IS NULL )
              AND (DAY(created_at) = ?3 OR ?3 IS NULL )
"""
    )
    TotalProductDTO countProductsByDateLike(String year, String month, String day, int shopId);
}
