package com.parent.payment.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class RazorpayOrderService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    public JSONObject createOrder(long amountInPaise, String receipt) throws Exception {
        RazorpayClient client = new RazorpayClient(keyId, keySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receipt);
        orderRequest.put("payment_capture", 1);
        Order order = client.orders.create(orderRequest);
        return order.toJson();
    }

    public String getKeyId() { return keyId; }
}
