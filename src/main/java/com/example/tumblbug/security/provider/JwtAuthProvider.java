package com.example.tumblbug.security.provider;

import com.example.tumblbug.entity.User;
import com.example.tumblbug.repository.UserRepository;
import com.example.tumblbug.security.UserDetailsImpl;
import com.example.tumblbug.security.jwt.JwtDecoder;
import com.example.tumblbug.security.jwt.JwtPreProcessingToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider implements AuthenticationProvider {

    private final JwtDecoder jwtDecoder;

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String token = (String) authentication.getPrincipal();
        String email = jwtDecoder.decodeUsername(token);

        // TODO: API 사용시마다 매번 User DB 조회 필요
        //  -> 해결을 위해서는 UserDetailsImpl 에 User 객체를 저장하지 않도록 수정
        //  ex) UserDetailsImpl 에 userId, username, role 만 저장
        //    -> JWT 에 userId, username, role 정보를 암호화/복호화하여 사용
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 이메일입니다."));
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtPreProcessingToken.class.isAssignableFrom(authentication);
    }
}
