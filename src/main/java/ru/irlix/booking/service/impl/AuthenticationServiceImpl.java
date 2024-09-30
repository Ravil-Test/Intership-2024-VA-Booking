package ru.irlix.booking.service.impl;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.irlix.booking.dto.user.AuthenticationUserRequest;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.security.jwt.JwtResponse;
import ru.irlix.booking.security.jwt.JwtTokenUtils;
import ru.irlix.booking.service.AuthenticationService;
import ru.irlix.booking.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;

    @Override
    public JwtResponse login(AuthenticationUserRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.login(), authRequest.password()));
        } catch (BadCredentialsException e) {
            throw new JwtException(e.getMessage());
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.login());
        String token = jwtTokenUtils.generateToken(userDetails);

        return new JwtResponse(token);
    }

    @Override
    public UserResponse registration(UserCreateRequest createRequest) {
        return userService.save(createRequest);
    }
}
