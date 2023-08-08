package com.ghtk.request.product_manage;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {
    private String name;
    private int price;
}
