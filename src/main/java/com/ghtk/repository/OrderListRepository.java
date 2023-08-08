package com.ghtk.repository;

import com.ghtk.model.OrderList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface OrderListRepository extends JpaRepository<OrderList, Long> {


    @Query(
            nativeQuery = true,
            value = "SELECT * FROM order_list WHERE order_id = ?1 AND product_id = ?2"
    )
    OrderList findById(int order_id, int product_id);

//    @Modifying
//    @Query(
//            nativeQuery = true,
//            value = "DELETE FROM order_list WHERE order_id = ?1 AND product_id = ?2"
//    )
//    void delete(int order_id, int product_id);

    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM order_list WHERE order_id = ?1 AND product_id = ?2"
    )
    void deleteByOrderIdAndProductId(int order_id, int product_id);


    @Query(
            nativeQuery = true,
            value = "SELECT product_id FROM order_list WHERE order_id = ?1"
    )
    List<Integer> findProductIdsByOrderId(int orderId);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM order_list WHERE order_id = ?1 AND product_id = ?2"
    )
    Optional<OrderList> findByOrderIdAndProductId(Integer orderId, int productId);
}
