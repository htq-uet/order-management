package com.ghtk.controller.auth;

import com.ghtk.request.auth.LoginRequest;
import com.ghtk.request.auth.ShopRegisterRequest;
import com.ghtk.response.AuthenticationResponse;
import com.ghtk.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Authentication {

    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody ShopRegisterRequest shopRegisterRequest
    ) throws Exception {
        return ResponseEntity.ok(authenticationService.register(shopRegisterRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest loginRequest
    ) throws AccessDeniedException {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public void refresh(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authenticationService.refresh(request, response);
    }

//    @GetMapping("/logout")
//    public void logout() {
//
//    }
}
