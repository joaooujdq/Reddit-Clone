package com.example.RedditClone.service;

import com.example.RedditClone.dto.AuthenticationResponse;
import com.example.RedditClone.dto.LoginRequest;
import com.example.RedditClone.dto.RegisterRequest;
import com.example.RedditClone.exceptions.SpringRedditException;
import com.example.RedditClone.model.NotificationEmail;
import com.example.RedditClone.model.User;
import com.example.RedditClone.model.VerificationToken;
import com.example.RedditClone.repository.UserRepository;
import com.example.RedditClone.repository.VerificationTokenRepository;
import com.example.RedditClone.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


import static java.time.Instant.now;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final MailContentBuilder mailContentBuilder;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    public  void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(Instant.now() );
        user.setEnabled(false);

        userRepository.save(user);

        log.info("Usuário registrado com sucesso, enviando email de autenticação.");

        String token = generateVerificationToken(user);

        String message = mailContentBuilder.build(new NotificationEmail("Obrigado por registrar no reddit clone, clique no link abaixo para ativar sua conta: "
                + ACTIVATION_EMAIL + "/" + token);

        mailService.sendMail(user.getEmail(), message);
        log.info("Email de ativação enviado!");

    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Token inválido"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("Usuário não encontrado"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }

    public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return Optional.of(principal);
    }
}
