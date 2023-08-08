package com.ghtk.repository;

import com.ghtk.model.DTO.OrderDTO;
import com.ghtk.model.DTO.OrderListDTO;
import com.ghtk.model.DTO.ProductDTO;
import com.ghtk.model.DTO.TotalOrderDTO;
import com.ghtk.model.Order;
import com.ghtk.model.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findProductsByIds(@Param("productIds") List<Integer> productIds);

    @Query( nativeQuery = true, value =
            """
            SELECT p.id, p.name, p.price, ol.quantity
            FROM product p
            LEFT JOIN order_list ol on p.id = ol.product_id 
            WHERE p.id IN ?1 AND ol.order_id = ?2
            """)
    List<OrderListDTO> findOrderProductsById(@Param("productIds") List<Integer> productIds, Integer order_id);


    @Query(
            nativeQuery = true,
            value = "SELECT IF(staff_id IS NOT NULL, staff_id, shop_id ) as creator_id FROM _order WHERE id = ?1"
    )
    int findCreatorById(String orderId);

    @Query(
            nativeQuery = true,
            value = """
            SELECT o.id, s.name as creator , o.created_at, o.updated_at, SUM(p.price * l.quantity) AS total_cost
            FROM _order o
            LEFT JOIN order_list l ON o.id = l.order_id
            LEFT JOIN staff s on s.id = o.staff_id
            LEFT JOIN product p ON l.product_id = p.id
            WHERE o.staff_id = ?
            GROUP BY o.id, o.created_at, o.updated_at;
"""
    )
    List<OrderDTO> findOrdersByStaffId(int id);

    @Query(
            nativeQuery = true,
            value = """
            SELECT o.id, if(s2.name IS NOT NULL , s2.name, s1.name) as creator, o.created_at, o.updated_at, SUM(p.price * l.quantity) AS total_cost
            FROM _order o
            LEFT JOIN order_list l ON o.id = l.order_id
            LEFT JOIN shop s1 on s1.id = o.shop_id
            LEFT JOIN staff s2 on s2.id = o.staff_id
            LEFT JOIN product p ON l.product_id = p.id
            WHERE o.shop_id = ?
            GROUP BY o.id, o.created_at, o.updated_at;
"""
    )
    List<OrderDTO> findOrdersByShopId(int id);

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

    @Query(
            nativeQuery = true,
            value = """
            SELECT COUNT(DISTINCT (o.id)) AS count, IF(SUM(p.price * l.quantity) IS NULL, 0, SUM(p.price * l.quantity))  AS cost
            FROM _order o
            LEFT JOIN order_list l ON o.id = l.order_id
            LEFT JOIN product p ON l.product_id = p.id
            WHERE o.shop_id = ?4
              AND (YEAR(o.created_at) = ?1 OR ?1 IS NULL )
              AND (MONTH(o.created_at) = ?2 OR ?2 IS NULL )
              AND (DAY(o.created_at) = ?3 OR ?3 IS NULL )
"""
    )
    TotalOrderDTO countOrdersByDateLike(String year, String month, String day, int shopId);
}
