package com.ghtk.service;

import com.ghtk.repository.OrderRepository;
import com.ghtk.repository.ProductRepository;
import com.ghtk.repository.ShopRepository;
import com.ghtk.request.report.DateRequest;
import com.ghtk.response.ReportResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    public ReportResponse getReport(DateRequest dateRequest, HttpServletRequest request) {

        String username = request.getUserPrincipal().getName();
        int shop_id = shopRepository.findShopIdByUsername(username);
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setTotalOrder(
                orderRepository.countOrdersByDateLike
                        (
                            dateRequest.getYear(),
                            dateRequest.getMonth(),
                            dateRequest.getDay(),
                            shop_id
                        )
        );
        reportResponse.setTotalProduct(
                productRepository.countProductsByDateLike
                        (
                            dateRequest.getYear(),
                            dateRequest.getMonth(),
                            dateRequest.getDay(),
                            shop_id
                        )
        );
        return reportResponse;
    }
}
