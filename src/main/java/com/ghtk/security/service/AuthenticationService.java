package com.ghtk.security.service;

import com.ghtk.model.Shop;
import com.ghtk.model.User;
import com.ghtk.repository.ShopRepository;
import com.ghtk.repository.UserRepository;
import com.ghtk.request.LoginRequest;
import com.ghtk.request.RegisterRequest;
import com.ghtk.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ShopRepository shopRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) throws Exception{
        var user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        var shop = Shop.builder()
                .name(registerRequest.getName())
                .address(registerRequest.getAddress())
                .phone(registerRequest.getPhone())
                .user(user)
                .build();
        userRepository.save(user);
        shopRepository.save(shop);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .success(true)
                .message("Register successfully")
                .data("token: " + jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findUserByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .success(true)
                .message("Login successfully")
                .data("token: " + jwtToken)
                .build();
    }
}
