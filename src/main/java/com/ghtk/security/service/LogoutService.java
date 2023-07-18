package com.ghtk.security.service;

import com.ghtk.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    @Autowired
    private final RedisTemplate template;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        //delete key access_token_id in redis
        template.delete("access_token_" + userRepository.findIdByUsername(username));
        //delete key refresh_token_id in redis
        template.delete("refresh_token_" + userRepository.findIdByUsername(username));
    }
}
