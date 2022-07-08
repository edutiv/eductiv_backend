package com.projectlms.projectlms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.projectlms.projectlms.domain.dto.UserDto;
import com.projectlms.projectlms.response.TokenResponse;
import com.projectlms.projectlms.service.AuthService;
import com.projectlms.projectlms.util.ResponseUtil;
import com.projectlms.projectlms.constant.AppConstant;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthService authService;

    @PostMapping(value = "/user/register")
    public ResponseEntity<?> register(@RequestBody UserDto req) {
        try {
            authService.register(req);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, req, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/user/login")
    // public ResponseEntity<TokenResponse> authenticateUser(@RequestBody LoginDto loginRequest) {
    //     log.info("Incoming password login request.");
    //     TokenResponse jwtResponse = authenticationService.generateToken(loginRequest);
    //     log.info("Successfully authenticated.");
    //     return ResponseEntity.ok(jwtResponse);
    // }
    public ResponseEntity<?> generateToken(@RequestBody UserDto req) {
        TokenResponse token = authService.generateToken(req);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token.getToken());
        
        return ResponseEntity.ok().headers(responseHeaders).body(token);
    }

    // @PutMapping("/users")
    // public ResponseEntity<Object> update(@RequestBody UserDto userDto) {
    //     return authService.updateUser(userDto);
    // }
}
