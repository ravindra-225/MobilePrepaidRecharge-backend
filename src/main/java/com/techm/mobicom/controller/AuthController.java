package com.techm.mobicom.controller;

import com.techm.mobicom.dto.AdminDTO;
import com.techm.mobicom.dto.LoginDTO;
import com.techm.mobicom.dto.UserDTO;
import com.techm.mobicom.dto.UserLoginDTO;
import com.techm.mobicom.exception.CustomException;
import com.techm.mobicom.service.UserService;
import com.techm.mobicom.config.CustomUserDetailsService;
import com.techm.mobicom.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody AdminDTO adminDTO) {
        try {
            String result = userService.registerAdmin(adminDTO);
            return ResponseEntity.ok(result);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/users/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            String result = userService.registerUser(userDTO);
            return ResponseEntity.ok(result);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/users/validate")
    public ResponseEntity<AuthenticationResponse> validate(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = userService.validateUser(userLoginDTO.getMobileNumber());
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(new AuthenticationResponse(null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty() ||
                loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
                throw new CustomException("Username and password are required", HttpStatus.BAD_REQUEST);
            }
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(),
                    loginDTO.getPassword()
                )
            );
            String token = jwtUtil.generateToken(authentication);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthenticationResponse(null, "Invalid username or password"));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(new AuthenticationResponse(null, e.getMessage()));
        }
    }

    private static class AuthenticationResponse {
        private final String token;
        private final String error;

        public AuthenticationResponse(String token) {
            this(token, null);
        }

        public AuthenticationResponse(String token, String error) {
            this.token = token;
            this.error = error;
        }

        public String getToken() {
            return token;
        }

        public String getError() {
            return error;
        }
    }
}