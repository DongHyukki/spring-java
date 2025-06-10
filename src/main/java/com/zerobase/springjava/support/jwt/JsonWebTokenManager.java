package com.zerobase.springjava.support.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zerobase.springjava.support.result.Result;
import com.zerobase.springjava.support.wrapper.Pair;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonWebTokenManager {

    private final String ISSUER = "zero-base";
    private final String TEMP_SECRET = "ding-dong";

    public JsonWebToken createToken(String subject, Instant expireAt) {
        var algorithm = Algorithm.HMAC256(TEMP_SECRET);

        var token = JWT.create()
                .withSubject(subject)
                .withExpiresAt(expireAt)
                .withIssuer(ISSUER)
                .sign(algorithm);

        return JsonWebToken.of(token);
    }

    public JsonWebToken createTokenWithClaim(String subject,
                                             Map<String, String> claims,
                                             Instant expireAt) {

        var algorithm = Algorithm.HMAC256(TEMP_SECRET);
        var jwtBuilder = JWT.create()
                .withSubject(subject)
                .withExpiresAt(expireAt)
                .withIssuer(ISSUER);

        claims.forEach(jwtBuilder::withClaim);

        var token = jwtBuilder.sign(algorithm);

        return JsonWebToken.of(token);
    }

    public Result<DecodedJWT, JWTVerificationException> verifyAndDecodeToken(JsonWebToken jsonWebToken) {
        try {
            var algorithm = Algorithm.HMAC256(TEMP_SECRET);
            var verifier = JWT.require(algorithm).build();

            var decoded = verifier.verify(jsonWebToken.value());

            return Result.success(decoded);
        } catch (JWTVerificationException exception) {
            //todo. 별도의 Exception 으로 매핑
            return Result.failure(exception);
        }
    }

    public Pair<String, String> getIssuerAndSubject(JsonWebToken jsonWebToken) {
        var result = verifyAndDecodeToken(jsonWebToken);
        var decoded = result.getOrThrow();

        return Pair.of(decoded.getIssuer(), decoded.getSubject());
    }

    public Pair<String, Map<String, String>> getSubjectAndClaims(JsonWebToken jsonWebToken) {
        var result = verifyAndDecodeToken(jsonWebToken);
        var decoded = result.getOrThrow();
        var claims = new HashMap<String, String>();

        decoded.getClaims().forEach((key, value) -> {
            claims.put(key, String.valueOf(value));
        });

        return Pair.of(decoded.getSubject(), claims);
    }
}
