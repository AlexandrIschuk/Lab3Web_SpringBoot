package ru.ssau.todo.service;

import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    public String generateToken(CustomUserDetails customUserDetails) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", customUserDetails.getId());
        payload.put("roles", customUserDetails.getAuthorities());
        payload.put("iat", System.currentTimeMillis());
        payload.put("exp", System.currentTimeMillis() + 15 * 60 * 10000);
        return generateToken(payload);
    }
    public String generateRefreshToken(CustomUserDetails customUserDetails) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", customUserDetails.getId());
        payload.put("iat", System.currentTimeMillis());
        payload.put("exp", System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        return generateRefreshToken(payload);
    }
    public String generateToken(Map<String, Object> payload) throws NoSuchAlgorithmException, InvalidKeyException {
        String encodedPayload = encodingPayload(payload);
        String encodedSignature = encodingSignature(encodedPayload);
        return encodedPayload + "." + encodedSignature;
    }

    public String generateRefreshToken(Map<String, Object> payload) throws NoSuchAlgorithmException, InvalidKeyException {
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


}
