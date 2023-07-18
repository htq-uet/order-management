package com.ghtk.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghtk.model.Shop;
import com.ghtk.model.User;
import com.ghtk.repository.ShopRepository;
import com.ghtk.repository.UserRepository;
import com.ghtk.request.LoginRequest;
import com.ghtk.request.RegisterRequest;
import com.ghtk.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    @Autowired
    private final RedisTemplate redisTemplate;

    public AuthenticationResponse register(RegisterRequest registerRequest) throws Exception{
        if (userRepository.existsByUsername(registerRequest.getUsername()) == 1) {
            throw new Exception("Username is already taken");
        }
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
        var refreshToken = jwtService.generateRefreshToken(user);

        redisTemplate.opsForValue().set("access_token_" + user.getId(), jwtToken);
        redisTemplate.opsForValue().set("refresh_token_" + user.getId(), refreshToken);

        //Test
        System.out.println("Value of redis: \n"
                + user.getId()
                + "\n"
                + redisTemplate.opsForValue().get("access_token_" + user.getId())
                + "\n"
                + redisTemplate.opsForValue().get("refresh_token_" + user.getId())) ;

        return AuthenticationResponse.builder()
                .access_token(jwtToken)
                .refresh_token(refreshToken)
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
        var refreshToken = jwtService.generateRefreshToken(user);

        redisTemplate.opsForValue().set("access_token_" + user.getId(), jwtToken);
        redisTemplate.opsForValue().set("refresh_token_" + user.getId(), refreshToken);

        //Test
        System.out.println("Value of redis: \n"
                + user.getId()
                + "\n"
                + redisTemplate.opsForValue().get("access_token_" + user.getId())
                + "\n"
                + redisTemplate.opsForValue().get("refresh_token_" + user.getId())) ;


        return AuthenticationResponse.builder()
                .access_token(jwtToken)
                .refresh_token(refreshToken)
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
            var userDetails = this.userRepository.findUserByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);
                var authResponse = AuthenticationResponse.builder()
                        .access_token(accessToken)
                        .refresh_token(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
