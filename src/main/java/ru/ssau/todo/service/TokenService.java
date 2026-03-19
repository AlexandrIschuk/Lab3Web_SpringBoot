package ru.ssau.todo.service;

import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import ru.ssau.todo.ExceptionHandler.InvalidTokenException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
@Service
public class TokenService {
    private Long createTime;

    public String generateToken(CustomUserDetails customUserDetails) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", customUserDetails.getId());
        payload.put("roles", customUserDetails.getAuthorities());
        payload.put("iat", System.currentTimeMillis());
        createTime = System.currentTimeMillis();
        payload.put("exp", System.currentTimeMillis() + 1 * 60 * 1000);
        return generateToken(payload);
    }
    public String generateRefreshToken(CustomUserDetails customUserDetails) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", customUserDetails.getId());
        payload.put("iat", System.currentTimeMillis());
        payload.put("exp", System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);

        return generateToken(payload);
    }
    public String generateToken(Map<String, Object> payload) throws NoSuchAlgorithmException, InvalidKeyException {
        String encodedPayload = encodingPayload(payload);
        String encodedSignature = encodingSignature(encodedPayload);
        return encodedPayload + "." + encodedSignature;
    }

    public String encodingSignature(String encodedPayload) throws NoSuchAlgorithmException, InvalidKeyException {
        String secret = System.getenv("JWT_SECRET");
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key =
                new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(key);
        byte[] signatureBytes =
                mac.doFinal(encodedPayload.getBytes());
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(signatureBytes);
    }

    public String encodingPayload(Map<String, Object> payload) {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(payload);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(json.getBytes());

    }

    public @NonNull Map<String, Object> getDecodePayload(String jwt) throws NoSuchAlgorithmException, InvalidKeyException, InvalidTokenException {
        String[] token = jwt.split("\\.", 2);
        String payloadPart = token[0];
        String signature = token[1];
        String encodingSignature;
        encodingSignature = encodingSignature(payloadPart);
        if (!signature.equals(encodingSignature)) {
            throw new InvalidTokenException("Invalid token");
        }
        byte[] decoded =
                Base64.getUrlDecoder().decode(payloadPart);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payload = mapper.readValue(decoded, new TypeReference<Map<String, Object>>() {
        });
        long exp = Long.parseLong(payload.get("exp").toString());
        if (exp < System.currentTimeMillis()) {
            throw new InvalidTokenException("The token's validity period has finally disappeared");
        }
        return payload;
    }


}
