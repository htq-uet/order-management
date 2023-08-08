package com.ghtk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ghtk.model.*;
import com.ghtk.model.DTO.OrderDTO;
import com.ghtk.model.DTO.OrderListDTO;
import com.ghtk.repository.*;
import com.ghtk.request.order_manage.CreateOrderRequest;
import com.ghtk.request.order_manage.OrderItem;
import com.ghtk.request.order_manage.UpdateOrderRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
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

    ObjectMapper objectMapper = new ObjectMapper();

    public String createNewOrder(CreateOrderRequest createOrderRequest, HttpServletRequest request) throws JsonProcessingException {
        List<Product> products = orderRepository.findProductsByIds(
                createOrderRequest
                        .product_ids.keySet()
                        .stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())
        );
        String username = request.getUserPrincipal().getName();


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
                    .order_id(order.getId())
                    .product_id(product.getId())
                    .quantity(createOrderRequest.product_ids.get(String.valueOf(product.getId())).quantity)
                    .build();
            orderListRepository.save(orderList);
        }

        var history = History.builder()
                .user(userRepository.findUserByUsername(username))
                .action("Create order")
                .time(order.getCreated_at())
                .content(
                        objectMapper.writeValueAsString(createOrderRequest)
                )
                .build();
        historyRepository.save(history);
        return "Order created";
    }

    public String updateOrder(UpdateOrderRequest updateOrderRequest, HttpServletRequest request) throws AccessDeniedException, JsonProcessingException {
        int creatorId = orderRepository.findCreatorById(updateOrderRequest.order_id);
        String username = request.getUserPrincipal().getName();

        Staff staff = null;
        Shop shop = null;

        if ( userRepository.findRoleByUsername(username).equals("STAFF") ) {
            staff = staffRepository.findStaffByUsername(username);
            shop = staff.getShop();

            if (creatorId != staff.getId()) {
                throw new AccessDeniedException("You are not allowed to update this order");
            }

        }
        else if ( userRepository.findRoleByUsername(username).equals("SHOP") ) {
            shop = shopRepository.findShopByUsername(username);

            if (creatorId != shop.getId()) {
                throw new AccessDeniedException("You are not allowed to update this order");
            }
        }

        var order = orderRepository.findById(Long.parseLong(updateOrderRequest.order_id)).get();
        order.setUpdated_at(LocalDateTime.now());
        orderRepository.save(order);

        Set<Integer> updatedProductIds = new HashSet<>();



        for (Product product : order.getProducts()) {
            updatedProductIds.add(product.getId());
            if (updateOrderRequest.product_ids.containsKey(String.valueOf(product.getId()))) {
                var orderList = orderListRepository.findById(order.getId(), product.getId());
                orderList.setQuantity(updateOrderRequest.product_ids.get(String.valueOf(product.getId())).quantity);
                orderListRepository.save(orderList);
            }
            else {
                orderListRepository.deleteByOrderIdAndProductId(order.getId(), product.getId());
            }
        }

        int orderId = Integer.parseInt(updateOrderRequest.order_id);
        Map<String, OrderItem> productIds = updateOrderRequest.product_ids;

        for (Map.Entry<String, OrderItem> entry : productIds.entrySet()) {
            int productId = Integer.parseInt(entry.getKey());
            if (!updatedProductIds.contains(productId)) {
                int quantity = entry.getValue().quantity;
                // Update the existing OrderList or create a new one with the given productId and quantity
                var orderList = OrderList.builder()
                                .order_id(orderId)
                                .product_id(productId)
                                .quantity(quantity)
                                .build();

                // Set the updated quantity
                orderList.setQuantity(quantity);

                // Save the OrderList to the repository
                orderListRepository.save(orderList);
            }
        }

        var history = History.builder()
                .user(userRepository.findUserByUsername(username))
                .action("Update order")
                .time(order.getUpdated_at())
                .content(
                        objectMapper.writeValueAsString(updateOrderRequest)
                )
                .build();
        historyRepository.save(history);
        return "Order updated";
    }

    public List<OrderDTO> getOrderList(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();

        Staff staff = null;
        Shop shop = null;

        if ( userRepository.findRoleByUsername(username).equals("STAFF") ) {
            staff = staffRepository.findStaffByUsername(username);

            return orderRepository.findOrdersByStaffId(staff.getId());
        }
        else if ( userRepository.findRoleByUsername(username).equals("SHOP") ) {
            shop = shopRepository.findShopByUsername(username);

            return orderRepository.findOrdersByShopId(shop.getId());
        }

        return Collections.emptyList();
    }

    public List<OrderListDTO> getOrder(int orderId, HttpServletRequest request) throws AccessDeniedException {
        String username = request.getUserPrincipal().getName();
        boolean accessDenied = checkAccess(username, orderId);

        if (accessDenied) {
            throw new AccessDeniedException("You are not allowed to view this order");
        }
        else {
            List<Integer> productIds = orderListRepository.findProductIdsByOrderId(orderId);
            return orderRepository.findOrderProductsById(productIds, orderId);
        }
    }

    public String deleteOrder(int orderId, HttpServletRequest request) throws AccessDeniedException, JsonProcessingException {
        String username = request.getUserPrincipal().getName();
        boolean accessDenied = checkAccess(username, orderId);

        if (accessDenied) {
            throw new AccessDeniedException("You are not allowed to delete this order");
        }
        else {
            orderRepository.deleteById((long) orderId);
            var history = History.builder()
                    .user(userRepository.findUserByUsername(username))
                    .action("Delete order")
                    .time(LocalDateTime.now())
                    .content(
                            objectMapper.writeValueAsString("orderId:" + orderId )
                    )
                    .build();
            historyRepository.save(history);
            return "Order deleted";
    }

}

    private boolean checkAccess(String username, int orderId) {

        Staff staff = null;
        Shop shop = null;

        boolean accessDenied = true;

        if (userRepository.findRoleByUsername(username).equals("STAFF")) {
            staff = staffRepository.findStaffByUsername(username);
            if (orderRepository.findStaffIdById(orderId) == staff.getId()) {
                accessDenied = false;
            }
        } else if (userRepository.findRoleByUsername(username).equals("SHOP")) {
            shop = shopRepository.findShopByUsername(username);
            if (orderRepository.findShopIdById(orderId) == shop.getId()) {
                accessDenied = false;
            }
        }
        return accessDenied;
    }
}
