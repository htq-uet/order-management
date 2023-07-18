package com.ghtk.controller.shop.staff_management;

import com.ghtk.request.StaffRegisterRequest;
import com.ghtk.request.StaffUpdateRequest;
import com.ghtk.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class StaffAccountController {

    @Autowired
    private final UserService userService;

    @PostMapping("/create_new_staff")
    public ResponseEntity<String> createStaffAccount(
            @Valid @RequestBody StaffRegisterRequest staffRegisterRequest,
            HttpServletRequest request,
            HttpServletResponse response

    ) {
       return ResponseEntity.ok(userService.addStaff(staffRegisterRequest, request , response));
    }

    @PutMapping("/update_staff")
    public ResponseEntity<String> updateStaffAccount(
            @Valid @RequestBody StaffUpdateRequest staffUpdateRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
       return ResponseEntity.ok(userService.updateStaff(staffUpdateRequest, request , response));
    }
}
