package com.ghtk.request.auth;

import com.ghtk.model.Role;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.ghtk.model.Role.SHOP;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopRegisterRequest {
    private String name;
    private String address;
    private String phone;
    private String username;
    @Size(min = 8, max = 16)
    private String password;
    private Role role = SHOP;
}
