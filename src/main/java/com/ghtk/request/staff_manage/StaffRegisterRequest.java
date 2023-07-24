package com.ghtk.request.staff_manage;

import com.ghtk.model.Role;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import static com.ghtk.model.Role.STAFF;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffRegisterRequest {
    private String name;
    private String phone;
    private String username;
    @Size(min = 8, max = 16)
    private String password;
    private Role role = STAFF;

}
