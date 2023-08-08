package com.ghtk.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghtk.model.Shop;
import com.ghtk.model.User;
import com.ghtk.repository.ShopRepository;
import com.ghtk.repository.StaffRepository;
import com.ghtk.repository.UserRepository;
import com.ghtk.request.auth.LoginRequest;
import com.ghtk.request.auth.ShopRegisterRequest;
import com.ghtk.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

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

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final RedisTemplate redisTemplate;

    public AuthenticationResponse register(ShopRegisterRequest shopRegisterRequest) throws Exception{
        if (userRepository.existsByUsername(shopRegisterRequest.getUsername()) == 1) {
            throw new Exception("Username is already taken");
        }
        var user = User.builder()
                .username(shopRegisterRequest.getUsername())
                .password(passwordEncoder.encode(shopRegisterRequest.getPassword()))
                .role(shopRegisterRequest.getRole())
                .build();
        var shop = Shop.builder()
                .name(shopRegisterRequest.getName())
                .address(shopRegisterRequest.getAddress())
                .phone(shopRegisterRequest.getPhone())
                .user(user)
                .build();
        userRepository.save(user);
        shopRepository.save(shop);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        redisTemplate.opsForValue().set("access_token_" + user.getId(), jwtToken);
        redisTemplate.opsForValue().set("refresh_token_" + user.getId(), refreshToken);


        return AuthenticationResponse.builder()
                .access_token(jwtToken)
                .refresh_token(refreshToken)
                .role(user.getRole())
                .build();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) throws AccessDeniedException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findUserByUsername(loginRequest.getUsername());
        if (Objects.equals(staffRepository.getStatusByUsername(loginRequest.getUsername()), "deactivated")) {
            throw new AccessDeniedException("Account is deactivated");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        redisTemplate.opsForValue().set("access_token_" + user.getId(), jwtToken);
        redisTemplate.opsForValue().set("refresh_token_" + user.getId(), refreshToken);

        System.out.println(jwtToken);

        return AuthenticationResponse.builder()
                .access_token(jwtToken)
                .refresh_token(refreshToken)
                .role(user.getRole())
                .build();
    }

    public void refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authorizationHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var userDetails = this.userRepository.findUserByUsername(username);
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);
                var authResponse = AuthenticationResponse.builder()
                        .access_token(accessToken)
                        .refresh_token(refreshToken)
                        .role(userDetails.getRole())
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
