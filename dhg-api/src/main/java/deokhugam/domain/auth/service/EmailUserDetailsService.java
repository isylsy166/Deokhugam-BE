package deokhugam.domain.auth.service;

import deokhugam.domain.user.exception.UserErrorCode;
import deokhugam.domain.user.exception.UserException;
import deokhugam.domain.auth.security.AuthenticatedUser;
import deokhugam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(AuthenticatedUser::from)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
    }
}
