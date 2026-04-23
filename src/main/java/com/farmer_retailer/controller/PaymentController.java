package com.farmer_retailer.controller;

import com.farmer_retailer.dto.CreatePaymentRequest;
import com.farmer_retailer.dto.VerifyPaymentRequest;
import com.farmer_retailer.model.Order;
import com.farmer_retailer.repository.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    // ✅ Status constants
    private static final String STATUS_PENDING_PAYMENT = "PENDING_PAYMENT";
    private static final String STATUS_PAYMENT_INITIATED = "PAYMENT_INITIATED";
    private static final String STATUS_PAID = "PAID";

    public PaymentController(RazorpayClient razorpayClient,
                             OrderRepository orderRepository) {
        this.razorpayClient = razorpayClient;
        this.orderRepository = orderRepository;
    }

    // 1️⃣ Create Razorpay Order
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest request)
            throws RazorpayException {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Guard conditions
        if (STATUS_PAID.equals(order.getStatus())) {
            return ResponseEntity.badRequest()
                    .body("Order already paid");
        }

        if (!STATUS_PENDING_PAYMENT.equals(order.getStatus())) {
            return ResponseEntity.badRequest()
                    .body("Invalid order state for payment");
        }

        // Create Razorpay order
        JSONObject options = new JSONObject();
        options.put("amount", order.getAmount().longValue() * 100); // ₹ → paise
        options.put("currency", "INR");
        options.put("receipt", "order_" + order.getId());

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);

        // Update order status
        order.setStatus(STATUS_PAYMENT_INITIATED);
        orderRepository.save(order);

        // Clean response for frontend
        JSONObject response = new JSONObject();
        response.put("razorpayOrderId", Optional.ofNullable(razorpayOrder.get("id")));
        response.put("amount", Optional.ofNullable(razorpayOrder.get("amount")));
        response.put("currency", Optional.ofNullable(razorpayOrder.get("currency")));

        return ResponseEntity.ok(response.toMap());
    }

    // 2️⃣ Verify Razorpay Payment
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody VerifyPaymentRequest request) throws Exception {

        // Verify signature
        String payload = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
        boolean isValid = Utils.verifySignature(payload, request.getRazorpaySignature(), razorpaySecret);

        if (!isValid) {
            return ResponseEntity.badRequest()
                    .body("Payment verification failed");
        }

        // Update order to PAID
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Ensure only PAYMENT_INITIATED orders can be marked PAID
        if (!STATUS_PAYMENT_INITIATED.equals(order.getStatus())) {
            return ResponseEntity.badRequest()
                    .body("Invalid order state for payment verification");
        }

        order.setStatus(STATUS_PAID);
        orderRepository.save(order);

        return ResponseEntity.ok("Payment verified & order marked as PAID");
    }
}
