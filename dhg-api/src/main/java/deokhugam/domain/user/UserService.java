package deokhugam.domain.user;

import deokhugam.domain.user.dto.UserDto;
import deokhugam.domain.user.exception.UserErrorCode;
import deokhugam.domain.user.exception.UserException;
import deokhugam.user.User;
import deokhugam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto create(String email, String nickname, String password) {
        checkDuplicateEmailAndNickname(email, nickname);
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, nickname, encodedPassword);
        User createdUser = userRepository.save(user);

        return toDto(createdUser);
    }

    private void checkDuplicateEmailAndNickname(String email, String nickname) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        Optional<User> userByNickname = userRepository.findByNickname(nickname);
        if (userByNickname.isPresent()) {
            throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
        }
    }

    private UserDto toDto(User user) {
        return new UserDto(
            user.getId().toString(),
            user.getEmail(),
            user.getNickname(),
            user.getCreated_at()
        );
    }
}
