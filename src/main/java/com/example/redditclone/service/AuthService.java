package com.example.redditclone.service;

import java.time.Instant;
import java.util.UUID;

import com.example.redditclone.dto.RegisterRequest;
import com.example.redditclone.exception.RedditException;
import com.example.redditclone.model.NotificationEmail;
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
    private final MailService mailService;

    public void signup(RegisterRequest registerRequest) {

        User user = saveUser(registerRequest);

        String token = generateVerificationToken();
        saveToken(user, token);

        sendVerificationMail(user, token);
    }

    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RedditException("Invalid Token"));

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
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

}
