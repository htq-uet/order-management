package com.ghtk.service;

import com.ghtk.model.Staff;
import com.ghtk.model.User;
import com.ghtk.repository.ShopRepository;
import com.ghtk.repository.StaffRepository;
import com.ghtk.repository.UserRepository;
import com.ghtk.request.DeleteRequest;
import com.ghtk.request.StaffRegisterRequest;

import com.ghtk.request.StaffUpdateRequest;
import com.ghtk.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ShopRepository shopRepository;

    @Autowired
    private final StaffRepository staffRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    public String addStaff(
            StaffRegisterRequest staffRegisterRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (userRepository.existsByUsername(staffRegisterRequest.getUsername()) == 1) {
            throw new RuntimeException("Username is already taken");
        }
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        var shop = shopRepository.findShopByUsername(username);
        var user = User.builder()
                .username(staffRegisterRequest.getUsername())
                .password(passwordEncoder.encode(staffRegisterRequest.getPassword()))
                .role(staffRegisterRequest.getRole())
                .build();
        userRepository.save(user);

        var staff = Staff.builder()
                .name(staffRegisterRequest.getName())
                .phone(staffRegisterRequest.getPhone())
                .user(user)
                .shop(shop)
                .build();
        staffRepository.save(staff);
        return "Add staff successfully";
    }

    public String updateStaff(
            StaffUpdateRequest staffUpdateRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        if (staffRepository.findShopIdByStaffId(staffUpdateRequest.getId())
                !=
                userRepository.findIdByUsername(username)) {
            throw new RuntimeException("You are not allowed to update this staff");
        }
        if (!passwordEncoder.matches(
                staffUpdateRequest.getPassword(),
                userRepository.findPasswordByUsername(staffUpdateRequest.getUsername()))) {
            throw new RuntimeException("Password is incorrect");
        }
        if (staffUpdateRequest.getNewPassword() != null) {
            if (staffUpdateRequest.getNewPassword().equals(staffUpdateRequest.getPassword())) {
                throw new RuntimeException("New password must be different from old password");
            }
        }
        if (!Objects.equals(staffUpdateRequest.getNewPassword(), staffUpdateRequest.getConfirmPassword())) {
            throw new RuntimeException("Confirm password is incorrect");
        }

        var shop = shopRepository.findShopByUsername(username);
        var user = User.builder()
                .id(staffUpdateRequest.getUser_id())
                .username(staffUpdateRequest.getUsername())
                .password(staffUpdateRequest.getNewPassword() == null ?
                        passwordEncoder.encode(staffUpdateRequest.getPassword())
                        :
                        passwordEncoder.encode(staffUpdateRequest.getNewPassword()))
                .role(staffUpdateRequest.getRole())
                .build();
        userRepository.save(user);

        var staff = Staff.builder()
                .id(staffUpdateRequest.getId())
                .name(staffUpdateRequest.getName())
                .phone(staffUpdateRequest.getPhone())
                .user(user)
                .shop(shop)
                .build();
        staffRepository.save(staff);
        return "Update staff successfully";
    }

    public String deleteStaff(
            DeleteRequest deleteRequest,
            HttpServletRequest request
    ) {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        if (staffRepository.findShopIdByStaffId(deleteRequest.getId())
                !=
                userRepository.findIdByUsername(username)) {
            throw new RuntimeException("You are not allowed to delete this staff");
        }
        var user_id = staffRepository.findUserIdById(deleteRequest.getId());
        staffRepository.deleteById((long) deleteRequest.getId());
        userRepository.deleteById(user_id);
        return "Delete staff successfully";
    }

    public List<Staff> getAllStaff(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        var shop_id = shopRepository.findShopIdByUsername(username);
        List<Staff> list = staffRepository.findAllByShopId(shop_id);
        return list;
    }
}
