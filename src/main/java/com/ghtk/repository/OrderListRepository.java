package com.ghtk.repository;

import com.ghtk.model.OrderList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderListRepository extends JpaRepository<OrderList, Long> {

//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Transactional
//    public void save(OrderList orderList) {
//        int quantity = orderList.getQuantity();
//        int orderId = orderList.getOrder().getId();
//        int productId = orderList.getProduct().getId();
//        Query query = entityManager.createNativeQuery(
//                        "UPDATE order_list SET quantity = ?1 WHERE order_id = ?2 AND product_id = ?3"
//                )
//                .setParameter(1, quantity)
//                .setParameter(2, orderId)
//                .setParameter(3, productId);
//        query.executeUpdate();
//    }

}
