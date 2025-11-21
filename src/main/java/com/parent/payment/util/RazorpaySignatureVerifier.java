package com.parent.payment.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class RazorpaySignatureVerifier {

    public static boolean verifySignature(String payload, String razorpaySignature, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKey);

            byte[] hash = sha256_HMAC.doFinal(payload.getBytes());
            String generatedSignature = Base64.getEncoder().encodeToString(hash);

            return generatedSignature.equals(razorpaySignature);
        } catch (Exception e) {
            return false;
        }
    }
}
