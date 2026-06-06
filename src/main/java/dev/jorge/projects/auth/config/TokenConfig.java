package dev.jorge.projects.auth.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.jorge.projects.auth.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
public class TokenConfig {

    @Value("${JWT_SECRET:secret}")
    private String secret;

    public String generateSessionToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("userId", user.getId())
                .withClaim("roles", user.getRoles().stream().map(Enum::name).toList())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plus(Duration.ofDays(7)))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateSessionToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT decoded = JWT.require(algorithm).build().verify(token);

            if (decoded.getClaim("userId").isMissing()) {
                return Optional.empty();
            }

            List<String> roles = decoded.getClaim("roles").asList(String.class);

            return Optional.of(JWTUserData.builder()
                    .userId(UUID.fromString(decoded.getClaim("userId").asString()))
                    .email(decoded.getSubject())
                    .roles(roles != null ? roles : Collections.emptyList())
                    .build());

        } catch (JWTVerificationException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }


}