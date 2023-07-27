package com.ghtk.repository;

import com.ghtk.model.DTO.ProductDTO;
import com.ghtk.model.Order;
import com.ghtk.model.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findProductsByIds(@Param("productIds") List<Integer> productIds);

    @Query("SELECT new com.ghtk.model.DTO.ProductDTO(p.id, p.name, p.price, p.created_at, p.updated_at) FROM Product p WHERE p.id IN :productIds")
    List<ProductDTO> findOrderProductsById(@Param("productIds") List<Integer> productIds);


    @Query(
            nativeQuery = true,
            value = "SELECT IF(staff_id IS NOT NULL, staff_id, shop_id ) as creator_id FROM _order WHERE id = ?1"
    )
    int findCreatorById(String orderId);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM _order WHERE staff_id = ?1"
    )
    List<Order> findOrdersByStaffId(int id);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM _order WHERE shop_id = ?1"
    )
    List<Order> findOrdersByShopId(int id);

    @Query(
            nativeQuery = true,
            value = "SELECT staff_id FROM _order WHERE id = ?1"
    )
    int findStaffIdById(int orderId);

    @Query(
            nativeQuery = true,
            value = "SELECT shop_id FROM _order WHERE id = ?1"
    )
    int findShopIdById(int orderId);
}
