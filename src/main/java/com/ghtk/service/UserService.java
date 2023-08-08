package com.ghtk.service;

import com.ghtk.model.DTO.StaffDTO;
import com.ghtk.model.Staff;
import com.ghtk.model.User;
import com.ghtk.repository.ShopRepository;
import com.ghtk.repository.StaffRepository;
import com.ghtk.repository.UserRepository;
import com.ghtk.request.staff_manage.StaffRegisterRequest;

import com.ghtk.request.staff_manage.StaffUpdateRequest;
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
                .status(staffRegisterRequest.getStatus())
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
        final String username ;

        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        if (staffRepository.findShopIdByStaffId(staffUpdateRequest.getId())
                !=
                shopRepository.findShopIdByUsername(username)) {
            throw new RuntimeException("You are not allowed to update this staff");
        }

        var shop = shopRepository.findShopByUsername(username);

        User user = userRepository.findUserByUsername(staffUpdateRequest.getUsername());
        if(staffUpdateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(staffUpdateRequest.getPassword()));
            userRepository.save(user);
        }


        var staff = Staff.builder()
                .id(staffUpdateRequest.getId())
                .name(staffUpdateRequest.getName())
                .status(staffUpdateRequest.getStatus())
                .phone(staffUpdateRequest.getPhone())
                .user(user)
                .shop(shop)
                .build();
        staffRepository.save(staff);
        return "Update staff successfully";
    }

    public String deleteStaff(
            int staffId,
            HttpServletRequest request
    ) {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        if (staffRepository.findShopIdByStaffId(staffId)
                !=
                shopRepository.findShopIdByUsername(username) ){
            throw new RuntimeException("You are not allowed to delete this staff");
        }

        var staff = staffRepository.findStaffById(staffId);
        staff.setStatus("deactivated");

        staffRepository.save(staff);
        return "Deactivate staff successfully";
    }

    public List<StaffDTO> getAllStaff(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        var shop_id = shopRepository.findShopIdByUsername(username);
        List<StaffDTO> list = staffRepository.findAllByShopId(shop_id);
        return list;
    }
}
