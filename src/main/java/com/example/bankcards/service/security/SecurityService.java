package com.example.bankcards.service.security;
import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.response.TokenData;
import com.example.bankcards.exception.CheckPasswordException;
import com.example.bankcards.model.Role;
import com.example.bankcards.model.User;
import com.example.bankcards.model.jwt.RefreshToken;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenService jwtTokenService;

    private final JwtRefreshService jwtRefreshTokenService;

    public TokenData processPasswordToken(LoginRequest request){
        User user = userService.findByPhoneNumber(request.phoneNumber());
        if(!passwordEncoder.matches(request.password(),user.getPassword())){
            log.error("Exception trying to check password for email: {}", user.getPassword());
            throw new CheckPasswordException("Введен неверный пароль");
        }
        return createTokenData(user);
    }

    public TokenData processRefreshToken(String refreshTokenValue){
        RefreshToken refreshToken = jwtRefreshTokenService.getByValue(refreshTokenValue);
        User user = userService.findById(refreshToken.getUserId());
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
