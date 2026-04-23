package com.farmer_retailer.controller;

import com.farmer_retailer.model.Feedback;
import com.farmer_retailer.model.Order;
import com.farmer_retailer.repository.FeedbackRepository;
import com.farmer_retailer.repository.OrderRepository;
import com.farmer_retailer.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public FeedbackController(FeedbackRepository feedbackRepository,
                              OrderRepository orderRepository,
                              UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{orderId}")
    public Feedback giveFeedback(@PathVariable Long orderId,
                                 @RequestParam int rating,
                                 @RequestParam(required = false) String comment) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().equals("DELIVERED")) {
            throw new RuntimeException("Feedback can only be given for delivered orders");
        }

        // Check if feedback already exists
        feedbackRepository.findByOrder_Id(orderId).ifPresent(f -> {
            throw new RuntimeException("Feedback already submitted for this order");
        });

        Feedback feedback = new Feedback();
        feedback.setOrder(order);
        feedback.setRetailer(order.getBuyer());
        feedback.setFarmer(order.getSeller());
        feedback.setRating(rating);
        feedback.setComment(comment);

        return feedbackRepository.save(feedback);
    }
}
