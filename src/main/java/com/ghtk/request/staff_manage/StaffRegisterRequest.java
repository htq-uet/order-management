package com.ghtk.request.staff_manage;

import com.ghtk.model.Role;
import jakarta.validation.constraints.Size;
import lombok.*;


import static com.ghtk.model.Role.STAFF;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffRegisterRequest {
    private String name;
    private String phone;
    private String username;
    private String status = "active";
    @Size(min = 8, max = 16)
    private String password;
    private Role role = STAFF;

}
