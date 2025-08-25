package com.tcode.moviebase.Controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    @GetMapping("/register")
    public ResponseEntity<String> register() {
        return ResponseEntity.ok().body("http://localhost:8080/realms/myrealm/login-actions/registration?execution=5efa8914-2fbe-4288-9fe8-62c89bcd0ad2&client_id=account-console&tab_id=5OShNeVbUvE&client_data=eyJydSI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbS9hY2NvdW50IiwicnQiOiJjb2RlIiwic3QiOiJkY2EzNTlkOC0zZTk0LTRlNTctODE0MC1hYmVjNDNmMzFjMDkifQ");
    }


}
