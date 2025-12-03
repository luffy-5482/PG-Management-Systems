package com.parent.payment.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Simple REST-based Razorpay client (no SDK).
 * Creates orders using Razorpay REST API using basic auth.
 */
@Service
public class RazorpayRestService {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    private static final String ORDERS_URL = "https://api.razorpay.com/v1/orders";

    /**
     * Creates an order on Razorpay using REST call.
     *
     * @param amountInPaise amount in paise (e.g. 850000 for ₹8,500)
     * @param currency      currency code (INR)
     * @param receipt       receipt string
     * @return JSONObject response from Razorpay
     * @throws Exception on HTTP or parsing error
     */
    public JSONObject createOrder(int amountInPaise, String currency, String receipt) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("amount", amountInPaise);
        payload.put("currency", currency);
        payload.put("receipt", receipt);
        payload.put("payment_capture", 1);

        String auth = razorpayKey + ":" + razorpaySecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(ORDERS_URL);
            post.setHeader("Authorization", "Basic " + encodedAuth);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(payload.toString(), StandardCharsets.UTF_8));

            try (CloseableHttpResponse resp = client.execute(post)) {
                String body = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
                int status = resp.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    return new JSONObject(body);
                } else {
                    throw new RuntimeException("Razorpay order creation failed: status=" + status + " body=" + body);
                }
            }
        }
    }
}
