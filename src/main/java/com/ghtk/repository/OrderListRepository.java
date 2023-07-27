package com.ghtk.repository;

import com.ghtk.model.OrderList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface OrderListRepository extends JpaRepository<OrderList, Long> {


    @Query(
            nativeQuery = true,
            value = "SELECT * FROM order_list WHERE order_id = ?1 AND product_id = ?2"
    )
    OrderList findById(int id, int id1);


    @Query(
            nativeQuery = true,
            value = "SELECT product_id FROM order_list WHERE order_id = ?1"
    )
    List<Integer> findProductIdsByOrderId(int orderId);
}
