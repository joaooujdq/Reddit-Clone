package com.example.RedditClone.security;

import com.example.RedditClone.exceptions.SpringRedditException;
import com.example.RedditClone.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

import static io.jsonwebtoken.Jwts.parser;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init(){
        try {
            keyStore = keyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        }catch(KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
            throw new SpringRedditException("Um erro aconteceu durante o carregamento da keystore");

        }
    }
    public String generateToken(Authentication authentication){
        //declarei User dessa forma para que não houvesse confusão com a entidade USer
        User principal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey(){
        try{
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());

        }catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
            throw new SpringRedditException("Um erro aconteceu no retorno da chave privada pra keystore");
        }
    }

    public boolean validateToken(String jwt){
        parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublickey(){
        try{
            return keyStore.getCertificate("springblog").getPublicKey();
        }catch (KeyStoreException e){
            throw new SpringRedditException("Um erro aconteceu no retorno da chave pública pra keystore")
        }
    }

    public String getUsernameFromJWT(String token){
        Claims claims = parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
