package com.ghtk.service;

import com.ghtk.model.History;
import com.ghtk.model.Product;
import com.ghtk.model.User;
import com.ghtk.repository.HistoryRepository;
import com.ghtk.repository.ProductRepository;
import com.ghtk.repository.UserRepository;
import com.ghtk.request.product_manage.AddProductRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final HistoryRepository historyRepository;

    public String addProduct(
            AddProductRequest addProductRequest,
            HttpServletRequest request
    ) {
        String username = request.getUserPrincipal().getName();
        User user = userRepository.findUserByUsername(username);
        var product = Product.builder()
                .name(addProductRequest.getName())
                .shop(user.getShop())
                .created_at(LocalDateTime.now())
                .price(addProductRequest.getPrice())
                .build();
        if (productRepository.findProductByName(product.getName()) != null) {
            throw new RuntimeException("Product name already exists");
        }
        productRepository.save(product);
//        int productId = product.getId();

        var history = History.builder()
                .user(user)
                .action("Add product")
                .time(LocalDateTime.now())
                .content(
                        "Add product "
                                +
                                addProductRequest.getName()
                                +
                                " with price "
                                +
                                addProductRequest.getPrice()
                                +
                                " by "
                                +
                                username)
                .build();
        historyRepository.save(history);

        return "Product added successfully";
    }
}
