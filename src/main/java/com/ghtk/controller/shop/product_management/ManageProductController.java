package com.ghtk.controller.shop.product_management;

import com.ghtk.request.product_manage.AddProductRequest;
import com.ghtk.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ManageProductController {

    @Autowired
    private final ProductService productService;


    @PostMapping("/create_new_product")
    public ResponseEntity<String> createNewProduct(
            @RequestBody AddProductRequest addProductRequest,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(productService.addProduct(addProductRequest, request, "Add product"));
    }

    @PutMapping("/update_product")
    public ResponseEntity<String> updateProduct(
            @RequestBody AddProductRequest addProductRequest,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(productService.addProduct(addProductRequest, request, "Update product"));
    }
}
