package com.ghtk.request;

import com.ghtk.model.Role;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String address;
    private String phone;
    private String username;
    @Size(min = 8, max = 16)
    private String password;
    private Role role = Role.SHOP;
}
