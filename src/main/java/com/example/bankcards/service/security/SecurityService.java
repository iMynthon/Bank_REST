package com.example.bankcards.service.security;
import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.response.TokenData;
import com.example.bankcards.exception.CheckPasswordException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.model.User;
import com.example.bankcards.model.jwt.RefreshToken;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.bankcards.util.StringUtilsMessage.ENTITY_NOT_FOUND;
import static com.example.bankcards.util.StringUtilsMessage.PASSWORD_INCORRECT;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenService jwtTokenService;

    private final JwtRefreshService jwtRefreshTokenService;

    public TokenData processPasswordToken(LoginRequest request){
        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(()-> new EntityNotFoundException(ENTITY_NOT_FOUND));
        if(!passwordEncoder.matches(request.password(),user.getPassword())){
            log.error("Exception trying to check password for email: {}", user.getPassword());
            throw new CheckPasswordException(PASSWORD_INCORRECT);
        }
        return createTokenData(user);
    }

    public TokenData processRefreshToken(String refreshTokenValue){
        RefreshToken refreshToken = jwtRefreshTokenService.getByValue(refreshTokenValue);
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(()-> new EntityNotFoundException(ENTITY_NOT_FOUND));
        return createTokenData(user);
    }

    private TokenData createTokenData(User user) {
        String token = jwtTokenService.generatedToken(
                user.getId(),
                user.getPhoneNumber(),
                user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        RefreshToken refreshToken = jwtRefreshTokenService.save(user.getId());
        return new TokenData(token,refreshToken.getValue());
    }
}
