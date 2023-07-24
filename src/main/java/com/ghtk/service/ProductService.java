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
            HttpServletRequest request,
            String action
    ) {
        String username = request.getUserPrincipal().getName();
        User user = userRepository.findUserByUsername(username);
        boolean productExists = productRepository.findProductByName(addProductRequest.getName()) != null;
        var product = Product.builder()
                .name(addProductRequest.getName())
                .shop(user.getShop())
                .staff(user.getStaff())
                .created_at(
                        productExists ?
                                productRepository.findProductByName(addProductRequest.getName()).getCreated_at()
                                :
                                LocalDateTime.now()
                )
                .updated_at(LocalDateTime.now())
                .price(addProductRequest.getPrice())
                .build();
        if (productExists && action.equals("Add product")) {
            throw new RuntimeException("Product name already exists");
        }
        if (productExists) {
            product.setId(productRepository.findProductByName(addProductRequest.getName()).getId());
        }
        productRepository.save(product);
//        int productId = product.getId();

        var history = History.builder()
                .user(user)
                .action(productExists ? "Update product" : "Add product")
                .time(LocalDateTime.now())
                .content(
                        (productExists ? "Update product " : "Add product ")
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

        return productExists ? "Update product successfully" : "Add product successfully";
    }
}
