package com.epam.gymsecurity.component;

import com.epam.gymsecurity.domain.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtIssuer {
    private final RSAKey rsaJwk;
    private final JWKSet jwkSet;
    private final JWSSigner signer;
    private final long ttlSeconds = 900;

    public JwtIssuer(RSAKey rsaJwk, JWKSet jwkSet, JWSSigner signer) {
        this.rsaJwk = rsaJwk;
        this.jwkSet = jwkSet;
        this.signer = signer;
    }

    public JwtIssuer() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair kp = gen.generateKeyPair();

        rsaJwk = new RSAKey.Builder((RSAPublicKey) kp.getPublic())
                .privateKey(kp.getPrivate())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(UUID.randomUUID().toString())
                .build();

        jwkSet = new JWKSet(rsaJwk.toPublicJWK());
        signer = new RSASSASigner(rsaJwk.toPrivateKey());
    }

    public String issue(User u) throws JOSEException {
        Instant now = Instant.now();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(u.getUsername())
                .claim("userId", u.getId())
                .claim("role", u.getRole())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(ttlSeconds)))
                .build();

        SignedJWT jwt = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJwk.getKeyID()).build(),
                claims
        );
        jwt.sign(signer);
        return jwt.serialize();
    }

    public JWKSet jwkSet() {
        return jwkSet;
    }
}
