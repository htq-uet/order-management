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
public class StaffUpdateRequest {
    private int id;
    private int user_id;
    private String name;
    private String phone;
    private String username;
    private String password;
    @Size(min = 8, max = 16)
    private String newPassword;
    private String confirmPassword;
    private Role role = STAFF;

}
