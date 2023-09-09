package com.spring.chatbot;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class SignatureGenerator {

    public String generateSignature(String message, String secretKey) {
        String encodeBase64String = "";

        try {
            byte[] secretKeyBytes = secretKey.getBytes("UTF-8");

            SecretKeySpec signingKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

            return encodeBase64String;

        } catch (Exception e) {
            System.out.println(e);
        }

        return encodeBase64String;
    }
}