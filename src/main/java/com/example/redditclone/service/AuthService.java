package com.example.redditclone.service;

import java.time.Instant;
import java.util.UUID;

import com.example.redditclone.dto.RegisterRequest;
import com.example.redditclone.model.User;
import com.example.redditclone.model.VerificationToken;
import com.example.redditclone.repository.UserRepository;
import com.example.redditclone.repository.VerificationTokenRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    public void signup(RegisterRequest registerRequest) {

        User user = saveUser(registerRequest);

        createVerificationToken(user);

        // TODO: send email with the generated token

    }

    private User saveUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        return user;
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    private void createVerificationToken(User user) {
        String token = generateVerificationToken();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryAt(Instant.now().plus(VerificationToken.DURATION_VALID));
        verificationTokenRepository.save(verificationToken);
    }

}
