package com.ghtk.controller.product_management;

import com.ghtk.request.product_manage.AddProductRequest;
import com.ghtk.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/shop", "/staff"})
public class ManageProductController {

    @Autowired
    private final ProductService productService;

    @GetMapping("/get_all_products")
    public ResponseEntity<?> getAllProducts(
            HttpServletRequest request
    ) throws AccessDeniedException {
        return ResponseEntity.ok(productService.getAllProducts(request));
    }


    @PostMapping("/create_new_product")
    public ResponseEntity<String> createNewProduct(
            @RequestBody AddProductRequest addProductRequest,
            HttpServletRequest request
    ) throws AccessDeniedException {
        return ResponseEntity.ok(productService.addProduct(addProductRequest, request, "Add product"));
    }

    @PutMapping("/update_product")
    public ResponseEntity<String> updateProduct(
            @RequestBody AddProductRequest addProductRequest,
            HttpServletRequest request
    ) throws AccessDeniedException {
        return ResponseEntity.ok(productService.addProduct(addProductRequest, request, "Update product"));
    }

    @DeleteMapping("/delete_product")
    public ResponseEntity<String> deleteProduct(
            @RequestParam("id") Integer id,
            HttpServletRequest request
    ) throws AccessDeniedException {
        return ResponseEntity.ok(productService.deleteProduct(id, request));
    }

}
