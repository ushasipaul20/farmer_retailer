//
//
//
//
//working
//package com.farmer_retailer.controller;
//
//import com.farmer_retailer.dto.ModifyOrderRequest;
//import com.farmer_retailer.dto.OrderRequest;
//import com.farmer_retailer.model.Order;
//import com.farmer_retailer.model.Product;
//import com.farmer_retailer.model.User;
//import com.farmer_retailer.repository.OrderRepository;
//import com.farmer_retailer.repository.ProductRepository;
//import com.farmer_retailer.repository.UserRepository;
//import com.farmer_retailer.service.EmailService;
//import com.farmer_retailer.service.NotificationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/orders")
//@CrossOrigin(origins = "http://localhost:5173")
//public class OrderController {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private EmailService emailService;
//
//    // ================= PLACE ORDER =================
//    @PostMapping
//    public Order placeOrder(@RequestBody OrderRequest request) {
//
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        User buyer = userRepository.findById(request.getRetailerId())
//                .orElseThrow(() -> new RuntimeException("Retailer not found"));
//
//        User seller = product.getFarmer();
//
//        if (product.getQuantity() < request.getQuantity()) {
//            throw new RuntimeException("Insufficient stock available");
//        }
//
//        product.setQuantity(product.getQuantity() - request.getQuantity());
//        productRepository.save(product);
//
//        Order order = new Order();
//        order.setBuyer(buyer);
//        order.setSeller(seller);
//        order.setProduct(product);
//        order.setQuantity(request.getQuantity());
//        order.setAmount(product.getPrice() * request.getQuantity());
//        order.setOrderDate(LocalDateTime.now());
//        order.setStatus("PENDING_PAYMENT");
//
//        Order savedOrder = orderRepository.save(order);
//
//        notificationService.notify(
//                seller.getId(),
//                "New order placed for " + product.getName() +
//                        " (Qty: " + request.getQuantity() + ")",
//                "ORDER_PLACED"
//        );
//
//        return savedOrder;
//    }
//
//    // ================= MARK ORDER AS PAID =================
//    @PatchMapping("/{orderId}/paid")
//    public Order markOrderPaid(@PathVariable Long orderId) {
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        order.setStatus("PAID");
//        Order updatedOrder = orderRepository.save(order);
//
//        // 🔔 Notify buyer
//        notificationService.notify(
//                order.getBuyer().getId(),
//                "Your order for " + order.getProduct().getName() +
//                        " has been PAID successfully",
//                "ORDER_PAID"
//        );
//
//        // 📧 EMAIL TO FARMER
//        System.out.println("📧 PAYMENT SUCCESS EMAIL BLOCK REACHED");
//
//        emailService.sendEmail(
//                order.getSeller().getEmail(),
//                "💰 Payment Received for Order #" + order.getId(),
//                "Hello " + order.getSeller().getName() + ",\n\n" +
//                        "Payment has been successfully completed for an order.\n\n" +
//                        "🧾 Order ID: " + order.getId() + "\n" +
//                        "🌾 Product: " + order.getProduct().getName() + "\n" +
//                        "📦 Quantity: " + order.getQuantity() + "\n" +
//                        "💰 Amount: ₹" + order.getAmount() + "\n\n" +
//                        "Please log in to process the order.\n\n" +
//                        "Regards,\nFarmer Retailer System"
//        );
//
//        return updatedOrder;
//    }
//
//    // ================= RETAILER ORDERS =================
//    @GetMapping("/retailer/{userId}")
//    public List<Order> getRetailerOrders(@PathVariable Long userId) {
//        return orderRepository.findByBuyer_Id(userId);
//    }
//
//    // ================= FARMER ORDERS =================
//    @GetMapping("/farmer/{userId}")
//    public List<Order> getFarmerOrders(@PathVariable Long userId) {
//        return orderRepository.findBySeller_Id(userId);
//    }
//
//    // ================= UPDATE ORDER STATUS =================
//    @PutMapping("/{orderId}/status")
//    public Order updateOrderStatus(
//            @PathVariable Long orderId,
//            @RequestParam String status
//    ) {
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        if (order.getStatus().equals("CANCELLED")) {
//            throw new RuntimeException("Cannot update a CANCELLED order");
//        }
//
//        order.setStatus(status);
//        Order updatedOrder = orderRepository.save(order);
//
//        notificationService.notify(
//                order.getBuyer().getId(),
//                "Your order for " + order.getProduct().getName() +
//                        " is now " + status,
//                "ORDER_STATUS"
//        );
//
//        return updatedOrder;
//    }
//
//    // ================= MODIFY ORDER =================
//    @PutMapping("/{orderId}/modify")
//    public Order modifyOrder(
//            @PathVariable Long orderId,
//            @RequestBody ModifyOrderRequest request
//    ) {
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        if (!order.getStatus().equals("PENDING_PAYMENT")) {
//            throw new RuntimeException("Order cannot be modified after payment initiation");
//        }
//
//        Product product = order.getProduct();
//        int difference = request.getNewQuantity() - order.getQuantity();
//
//        if (difference > 0 && product.getQuantity() < difference) {
//            throw new RuntimeException("Insufficient stock");
//        }
//
//        product.setQuantity(product.getQuantity() - difference);
//        productRepository.save(product);
//
//        order.setQuantity(request.getNewQuantity());
//        order.setAmount(product.getPrice() * request.getNewQuantity());
//
//        Order updatedOrder = orderRepository.save(order);
//
//        notificationService.notify(
//                order.getSeller().getId(),
//                "Order modified for " + product.getName() +
//                        " (New Qty: " + request.getNewQuantity() + ")",
//                "ORDER_MODIFIED"
//        );
//
//        return updatedOrder;
//    }
//
//    // ================= CANCEL ORDER =================
//    @PutMapping("/{orderId}/cancel")
//    public void cancelOrder(@PathVariable Long orderId) {
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        if (!order.getStatus().equals("PENDING_PAYMENT")) {
//            throw new RuntimeException("Order cannot be cancelled after payment initiation");
//        }
//
//        Product product = order.getProduct();
//        product.setQuantity(product.getQuantity() + order.getQuantity());
//        productRepository.save(product);
//
//        order.setStatus("CANCELLED");
//        orderRepository.save(order);
//
//        notificationService.notify(
//                order.getSeller().getId(),
//                "Order cancelled for " + product.getName(),
//                "ORDER_CANCELLED"
//        );
//    }
//}


package com.farmer_retailer.controller;

import com.farmer_retailer.dto.ModifyOrderRequest;
import com.farmer_retailer.dto.OrderRequest;
import com.farmer_retailer.model.Order;
import com.farmer_retailer.model.OrderPdfGenerator;
import com.farmer_retailer.model.Product;
import com.farmer_retailer.model.User;
import com.farmer_retailer.repository.OrderRepository;
import com.farmer_retailer.repository.ProductRepository;
import com.farmer_retailer.repository.UserRepository;
import com.farmer_retailer.service.EmailService;
import com.farmer_retailer.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    // ================= PLACE ORDER =================
    @PostMapping
    public Order placeOrder(@RequestBody OrderRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User buyer = userRepository.findById(request.getRetailerId())
                .orElseThrow(() -> new RuntimeException("Retailer not found"));

        User seller = product.getFarmer();

        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        Order order = new Order();
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setAmount(product.getPrice() * request.getQuantity());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING_PAYMENT");

        Order savedOrder = orderRepository.save(order);

        notificationService.notify(
                seller.getId(),
                "New order placed for " + product.getName() +
                        " (Qty: " + request.getQuantity() + ")",
                "ORDER_PLACED"
        );

        return savedOrder;
    }

    // ================= MARK ORDER AS PAID =================
    @PatchMapping("/{orderId}/paid")
    public Order markOrderPaid(@PathVariable Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("PAID");
        Order updatedOrder = orderRepository.save(order);

        // 🔔 Notify buyer
        notificationService.notify(
                order.getBuyer().getId(),
                "Your order for " + order.getProduct().getName() +
                        " has been PAID successfully",
                "ORDER_PAID"
        );

        // ================= PDF + EMAIL =================
        System.out.println("📄 Generating order PDF...");

        byte[] pdfBytes = OrderPdfGenerator.generateOrderPdf(order);

        if (pdfBytes != null && pdfBytes.length > 0) {

            emailService.sendEmailWithPdf(
                    order.getSeller().getEmail(),
                    "🧾 Order Invoice - Order #" + order.getId(),
                    "Hello " + order.getSeller().getName() + ",\n\n" +
                            "Payment has been successfully completed.\n" +
                            "Please find the attached invoice for order details.\n\n" +
                            "Order ID: " + order.getId() + "\n" +
                            "Product: " + order.getProduct().getName() + "\n" +
                            "Quantity: " + order.getQuantity() + "\n" +
                            "Amount: ₹" + order.getAmount() + "\n\n" +
                            "Regards,\nFarmer Retailer System",
                    pdfBytes
            );

            System.out.println("✅ Email with PDF sent to farmer");

        } else {
            System.out.println("❌ PDF generation failed, email not sent");
        }

        return updatedOrder;
    }

    // ================= RETAILER ORDERS =================
    @GetMapping("/retailer/{userId}")
    public List<Order> getRetailerOrders(@PathVariable Long userId) {
        return orderRepository.findByBuyer_Id(userId);
    }

    // ================= FARMER ORDERS =================
    @GetMapping("/farmer/{userId}")
    public List<Order> getFarmerOrders(@PathVariable Long userId) {
        return orderRepository.findBySeller_Id(userId);
    }

    // ================= UPDATE ORDER STATUS =================
    @PutMapping("/{orderId}/status")
    public Order updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Cannot update a CANCELLED order");
        }

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        notificationService.notify(
                order.getBuyer().getId(),
                "Your order for " + order.getProduct().getName() +
                        " is now " + status,
                "ORDER_STATUS"
        );

        return updatedOrder;
    }

    // ================= MODIFY ORDER =================
    @PutMapping("/{orderId}/modify")
    public Order modifyOrder(
            @PathVariable Long orderId,
            @RequestBody ModifyOrderRequest request
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().equals("PENDING_PAYMENT")) {
            throw new RuntimeException("Order cannot be modified after payment initiation");
        }

        Product product = order.getProduct();
        int difference = request.getNewQuantity() - order.getQuantity();

        if (difference > 0 && product.getQuantity() < difference) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setQuantity(product.getQuantity() - difference);
        productRepository.save(product);

        order.setQuantity(request.getNewQuantity());
        order.setAmount(product.getPrice() * request.getNewQuantity());

        Order updatedOrder = orderRepository.save(order);

        notificationService.notify(
                order.getSeller().getId(),
                "Order modified for " + product.getName() +
                        " (New Qty: " + request.getNewQuantity() + ")",
                "ORDER_MODIFIED"
        );

        return updatedOrder;
    }

    // ================= CANCEL ORDER =================
    @PutMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().equals("PENDING_PAYMENT")) {
            throw new RuntimeException("Order cannot be cancelled after payment initiation");
        }

        Product product = order.getProduct();
        product.setQuantity(product.getQuantity() + order.getQuantity());
        productRepository.save(product);

        order.setStatus("CANCELLED");
        orderRepository.save(order);

        notificationService.notify(
                order.getSeller().getId(),
                "Order cancelled for " + product.getName(),
                "ORDER_CANCELLED"
        );
    }
}
