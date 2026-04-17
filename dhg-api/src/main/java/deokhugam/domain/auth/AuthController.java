package deokhugam.domain.auth;

import deokhugam.domain.auth.service.AuthService;
import deokhugam.domain.user.UserService;
import deokhugam.domain.user.dto.LoginDto;
import deokhugam.domain.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(
        @Valid @RequestBody LoginDto req,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // 세션 인증
        authService.login(req.email(), req.password(), request, response);
        UserDto userDto = userService.findByEmail(req.email());
        return ResponseEntity.ok(userDto);
    }
}
