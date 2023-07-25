package com.ghtk.service;

import com.ghtk.model.*;
import com.ghtk.repository.*;
import com.ghtk.request.order_manage.CreateOrderRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final StaffRepository staffRepository;

    @Autowired
    private final ShopRepository shopRepository;

    @Autowired
    private final HistoryRepository historyRepository;

    @Autowired
    private final OrderListRepository orderListRepository;

    public String createNewOrder(CreateOrderRequest createOrderRequest, HttpServletRequest request) {
        List<Product> products = orderRepository.findProductsByIds(
                createOrderRequest
                        .product_ids.keySet()
                        .stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())
        );
        String username = request.getUserPrincipal().getName();
        userRepository.findIdByUsername(username);

        Staff staff = null;
        Shop shop = null;

        if ( userRepository.findRoleByUsername(username).equals("STAFF") ) {
            staff = staffRepository.findStaffByUsername(username);
            shop = staff.getShop();

        }
        else if ( userRepository.findRoleByUsername(username).equals("SHOP") ) {
            shop = shopRepository.findShopByUsername(username);
        }

        var order = Order.builder()
                .shop(shop)
                .staff(staff)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .products(products)

                .build();
        orderRepository.save(order);


        for (Product product : products) {
            var orderList = OrderList.builder()
                    .order(order)
                    .product(product)
                    .quantity(createOrderRequest.product_ids.get(String.valueOf(product.getId())).quantity)
                    .build();
            orderListRepository.save(orderList);
        }

        var history = History.builder()
                .user(userRepository.findUserByUsername(username))
                .action("Create order")
                .time(order.getCreated_at())
                .content(
                        "Create order " + order.getId()
                        + " with " + order.getProducts().size() + " products"
                        + " by " + username
                )
                .build();
        historyRepository.save(history);
        return "Order created";
    }
}
