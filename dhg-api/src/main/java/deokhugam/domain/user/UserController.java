package deokhugam.domain.user;

import deokhugam.domain.user.dto.CreateUserDto;
import deokhugam.domain.user.dto.LoginDto;
import deokhugam.domain.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserDto req) {
        UserDto response = userService.create(req.email(), req.nickname(), req.password());
        return ResponseEntity.ok(response);
    }
}
