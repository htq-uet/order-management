package com.ghtk.controller.order_management;

import com.ghtk.model.Product;
import com.ghtk.request.order_manage.CreateOrderRequest;
import com.ghtk.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/shop", "/staff"})
public class ManageOrderController {

    @Autowired
    private final OrderService orderService;

    @PostMapping("/create_new_order")
    public String createNewOrder(@RequestBody CreateOrderRequest createOrderRequest, HttpServletRequest request) {
        return orderService.createNewOrder(createOrderRequest, request);
    }
}
