package com.example.redditclone.service;

import java.time.Instant;
import java.util.UUID;

import javax.validation.Valid;

import com.example.redditclone.dto.AuthenticationResponse;
import com.example.redditclone.dto.LoginRequest;
import com.example.redditclone.dto.RefreshTokenRequest;
import com.example.redditclone.dto.RegisterRequest;
import com.example.redditclone.exception.RedditException;
import com.example.redditclone.model.NotificationEmail;
import com.example.redditclone.model.User;
import com.example.redditclone.model.VerificationToken;
import com.example.redditclone.repository.UserRepository;
import com.example.redditclone.repository.VerificationTokenRepository;
import com.example.redditclone.security.JwtProvider;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public void signup(RegisterRequest registerRequest) {

        User user = saveUser(registerRequest);

        String token = generateVerificationToken();
        saveToken(user, token);

        sendVerificationMail(user, token);

        verifyAccount(token);
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

    private void saveToken(User user, String token) {

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryAt(Instant.now().plus(VerificationToken.DURATION_VALID));
        verificationTokenRepository.save(verificationToken);
    }

    private void sendVerificationMail(User user, String token) {
        NotificationEmail notificationEmail = new NotificationEmail();
        notificationEmail.setSubject("Please Activate your Account");
        notificationEmail.setRecipient(user.getEmail());
        notificationEmail.setBody("Thank you for signing up to Spring Reddit, "
                + "please click on the below url to activate your account : "
                + "http://localhost:8080/api/auth/accountVerification/" + token);

        mailService.sendMail(notificationEmail);
    }

    
    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RedditException("Invalid Token"));

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }


    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), 
                    loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = jwtProvider.generateToken(authentication);

        return AuthenticationResponse.builder()
            .authenticationToken(token)
            .username(loginRequest.getUsername())
            .refreshToken(refreshTokenService.generateRefreshToken().getToken())
            .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
        .build();
    }    

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RedditException("No user found with username = " + username));

        return user;
    }

	public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        
        return AuthenticationResponse.builder()
            .authenticationToken(token)
            .username(refreshTokenRequest.getUsername())
            .refreshToken(refreshTokenRequest.getRefreshToken())
            .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
        .build();
	}

    public boolean isLoggedIn() {
        // reference:
        // https://stackoverflow.com/questions/19221979/spring-security-3-isauthenticated-not-working
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

}
