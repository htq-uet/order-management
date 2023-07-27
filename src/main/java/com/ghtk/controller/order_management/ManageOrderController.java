package com.ghtk.controller.order_management;

import com.ghtk.model.DTO.ProductDTO;
import com.ghtk.model.Order;
import com.ghtk.model.Product;
import com.ghtk.request.order_manage.CreateOrderRequest;
import com.ghtk.request.order_manage.UpdateOrderRequest;
import com.ghtk.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/shop", "/staff"})
public class ManageOrderController {

    @Autowired
    private final OrderService orderService;

    @PostMapping("/create_new_order")
    public ResponseEntity<String> createNewOrder(@RequestBody CreateOrderRequest createOrderRequest, HttpServletRequest request) {
        return ResponseEntity.ok(orderService.createNewOrder(createOrderRequest, request));
    }

    @PutMapping("/update_order")
    public ResponseEntity<String> updateOrder(@RequestBody UpdateOrderRequest updateOrderRequest, HttpServletRequest request) throws AccessDeniedException {
        return ResponseEntity.ok(orderService.updateOrder(updateOrderRequest, request));
    }

    @GetMapping("/get_order_list")
    public ResponseEntity<List<Order>> getOrderList(HttpServletRequest request) throws AccessDeniedException {
        return ResponseEntity.ok(orderService.getOrderList(request));
    }

    @GetMapping("/get_order")
    public ResponseEntity<List<ProductDTO>> getOrder(@RequestParam int order_id, HttpServletRequest request) throws AccessDeniedException {
        return ResponseEntity.ok(orderService.getOrder(order_id, request));
    }

    @DeleteMapping("/delete_order")
    public ResponseEntity<String> deleteOrder(@RequestParam int order_id, HttpServletRequest request) throws AccessDeniedException {
        return ResponseEntity.ok(orderService.deleteOrder(order_id, request));
    }
}
