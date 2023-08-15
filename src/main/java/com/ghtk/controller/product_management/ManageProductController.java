package com.ghtk.controller.product_management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ghtk.model.ES.ProductES;
import com.ghtk.request.product_manage.AddProductRequest;
import com.ghtk.service.ESService.SearchService;
import com.ghtk.service.ProductService;
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
public class ManageProductController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final SearchService searchService;

    @GetMapping("/get_all_products")
    public ResponseEntity<?> getAllProducts(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) throws AccessDeniedException {
        return ResponseEntity.ok(productService.getAllProducts(request, page));
    }


    @PostMapping("/create_new_product")
    public ResponseEntity<String> createNewProduct(
            @RequestBody AddProductRequest addProductRequest,
            HttpServletRequest request
    ) throws AccessDeniedException, JsonProcessingException {
        return ResponseEntity.ok(productService.addProduct(addProductRequest, request, "Add product"));
    }

    @PutMapping("/update_product")
    public ResponseEntity<String> updateProduct(
            @RequestBody AddProductRequest addProductRequest,
            HttpServletRequest request
    ) throws AccessDeniedException, JsonProcessingException {
        return ResponseEntity.ok(productService.addProduct(addProductRequest, request, "Update product"));
    }

    @DeleteMapping("/delete_product")
    public ResponseEntity<String> deleteProduct(
            @RequestParam("id") Integer id,
            HttpServletRequest request
    ) throws AccessDeniedException {
        return ResponseEntity.ok(productService.deleteProduct(id, request));
    }

    @GetMapping("/search_product")
    public ResponseEntity<List<ProductES>> searchProduct(
            @RequestParam("keyword") String keyword,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(searchService.searchProduct(keyword, request));
    }

}
