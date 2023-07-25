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

import java.nio.file.AccessDeniedException;
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
    ) throws AccessDeniedException {
        String username = request.getUserPrincipal().getName();
        User user = userRepository.findUserByUsername(username);
        boolean productExists = productRepository.findProductByName(addProductRequest.getName()) != null;
        if (productExists && user.getId() != productRepository.findCreatorByName(addProductRequest.getName()))
        {
            throw new AccessDeniedException("You are not allowed to do this action");
        }
        var product = Product.builder()
                .name(addProductRequest.getName())
                .shop(user.getShop() == null ? user.getStaff().getShop() : user.getShop())
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

    public String deleteProduct(Integer id, HttpServletRequest request) throws AccessDeniedException {
        String username = request.getUserPrincipal().getName();
        User user = userRepository.findUserByUsername(username);
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        Integer product_user_id = productRepository.findCreatorByName(product.getName());

        if (user.getId() != product_user_id)
        {
            throw new AccessDeniedException("You are not allowed to do this action");
        }


        productRepository.delete(product);
        var history = History.builder()
                .user(user)
                .action("Delete product")
                .time(LocalDateTime.now())
                .content("Delete product " + product.getName() + " by " + username)
                .build();
        historyRepository.save(history);
        return "Delete product successfully";
    }

    public Product getAllProducts(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        User user = userRepository.findUserByUsername(username);
        if (user.getShop() == null) {
            return productRepository.findAllByShop(user.getStaff().getShop().getId());
        }
        return productRepository.findAllByShop(user.getShop().getId());
    }
}
