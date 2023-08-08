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
public class StaffUpdateRequest {
    private int id;
    private String name;
    private String phone;
    private String status;
    private String username;
    private String password;
    private Role role = STAFF;

}
