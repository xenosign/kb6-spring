package org.example.kb6spring.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("이 문자열은 ADMIN 권한에서만 확인이 가능합니다!");
    }

    @GetMapping("/member")
    public ResponseEntity<String> member() {
        return ResponseEntity.ok("이 문자열은 MEMBER 권한 이상에서만 확인이 가능합니다!");
    }

    @GetMapping("/normal")
    public ResponseEntity<String> normal() {
        return ResponseEntity.ok("이 문자열은 로그인만 하면 확인이 가능합니다!");
    }
}
