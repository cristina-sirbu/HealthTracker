package com.healthtracker.healthtracker.user.api;

import com.healthtracker.healthtracker.common.ApiMessage;
import com.healthtracker.healthtracker.common.FreshnessToken;
import com.healthtracker.healthtracker.user.api.dto.LoginRequest;
import com.healthtracker.healthtracker.user.app.UserService;
import com.healthtracker.healthtracker.user.domain.User;
import com.healthtracker.healthtracker.user.api.dto.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest) {
        User u = userService.register(userRequest.username, userRequest.password);

        String token = FreshnessToken.create(60); // 60 seconds read-your-writes
        return ResponseEntity.ok()
                .header("X-Read-Primary-Until", token)
                .body(new ApiMessage("registered", u.getId(), u.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.username, loginRequest.password)
                .map(u -> ResponseEntity.ok(new ApiMessage("login_ok", u.getId(), u.getUsername())))
                .orElseGet(()->ResponseEntity.status(401).body(new ApiMessage("login_failed", null, loginRequest.username)));
    }
}
