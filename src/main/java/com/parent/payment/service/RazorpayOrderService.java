package com.parent.payment.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * REST-based wrapper named RazorpayOrderService (returns JSONObject).
 * This matches existing call sites that expect an org.json.JSONObject
 * and a three-argument signature for verifySignature in RazorpaySignatureVerifier.
 */
@Service
public class RazorpayOrderService {

    private final RazorpayRestService razorpayRestService;

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    public RazorpayOrderService(RazorpayRestService razorpayRestService) {
        this.razorpayRestService = razorpayRestService;
    }

    /**
     * Old usage: createOrder(long amount, String currency) - returns JSONObject.
     * We assume amount is already in paise if booking code sends paise.
     */
    public JSONObject createOrder(long amount, String currency) throws Exception {
        int amountPaise = (int) amount; // matches previous behavior; safe for typical amounts
        String receipt = "rcpt_" + System.currentTimeMillis();
        return createOrder(amountPaise, currency, receipt);
    }

    /**
     * Newer full signature: createOrder(int amountInPaise, String currency, String receipt)
     */
    public JSONObject createOrder(int amountInPaise, String currency, String receipt) throws Exception {
        // razorpayRestService returns org.json.JSONObject already
        return razorpayRestService.createOrder(amountInPaise, currency, receipt);
    }

    /**
     * Return key id for frontend usage
     */
    public String getKeyId() {
        return key;
    }

    /**
     * Verify signature wrapper — calls the verifier method that expects three arguments:
     * orderId, paymentId, signature.
     * (The verifier implementation should handle the secret internally or otherwise).
     */
    public boolean verifySignature(String orderId, String paymentId, String signature) throws Exception {
        return com.parent.payment.util.RazorpaySignatureVerifier.verifySignature(orderId, paymentId, signature);
    }
}
