package org.example.kb6spring.controller.user;

import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kb6spring.domain.user.User;
import org.example.kb6spring.repository.user.UserRepository;
import org.example.kb6spring.security.service.CustomUserDetailsService;
import org.example.kb6spring.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/login-success")
    public String loginSuccess(Model model, Principal principal, Authentication authentication) {
        log.info("=========> User Principal : {}", principal);
        log.info("=========> User Auth : {}", authentication);

        model.addAttribute("user", principal.getName());
        model.addAttribute("auth", authentication.getAuthorities());

        return "user/login-success";
    }

    @GetMapping("/login-failure")
    public String loginFailed() {
        return "user/login-failure";
    }

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/register")
    public String register(User user) {
        userService.save(user);
        return "redirect:/user/login";
    }
}
