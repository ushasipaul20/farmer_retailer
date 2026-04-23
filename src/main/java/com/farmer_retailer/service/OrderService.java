//////////package com.farmer_retailer.service;
//////////
//////////import com.farmer_retailer.model.*;
//////////import com.farmer_retailer.repository.*;
//////////import org.springframework.beans.factory.annotation.Autowired;
//////////import org.springframework.stereotype.Service;
//////////
//////////import java.time.LocalDateTime;
//////////import java.util.List;
//////////
//////////@Service
//////////public class OrderService {
//////////
//////////    @Autowired
//////////    private UserRepository userRepository;
//////////
//////////    @Autowired
//////////    private ProductRepository productRepository;
//////////
//////////    @Autowired
//////////    private OrderRepository orderRepository;
//////////
//////////    // =========================
//////////    // PLACE ORDER
//////////    // =========================
//////////    public Order placeOrder(Long buyerUserId, Long productId, int quantity) {
//////////
//////////        // Fetch buyer (Retailer/User)
//////////        User buyer = userRepository.findById(buyerUserId)
//////////                .orElseThrow(() -> new RuntimeException("Buyer user not found"));
//////////
//////////        // Fetch product
//////////        Product product = productRepository.findById(productId)
//////////                .orElseThrow(() -> new RuntimeException("Product not found"));
//////////
//////////        // Check stock
//////////        if (product.getQuantity() < quantity) {
//////////            throw new RuntimeException("Not enough quantity available");
//////////        }
//////////
//////////        // Seller is the user who owns the product (Farmer)
//////////        User seller = product.getFarmer();
//////////
//////////        // Create new order
//////////        Order order = new Order();
//////////        order.setBuyer(buyer);
//////////        order.setSeller(seller);
//////////        order.setProduct(product);
//////////        order.setQuantity(quantity);
//////////        order.setAmount(product.getPrice() * quantity);
//////////        order.setOrderDate(LocalDateTime.now());
//////////        order.setStatus("PENDING");
//////////
//////////        // Decrement product stock
//////////        product.setQuantity(product.getQuantity() - quantity);
//////////        productRepository.save(product);
//////////
//////////        return orderRepository.save(order);
//////////    }
//////////
//////////    // =========================
//////////    // GET ORDERS BY BUYER
//////////    // =========================
//////////    public List<Order> getOrdersByBuyer(Long buyerUserId) {
//////////        return orderRepository.findByBuyer_Id(buyerUserId);
//////////    }
//////////
//////////    // =========================
//////////    // GET ORDERS BY SELLER
//////////    // =========================
//////////    public List<Order> getOrdersBySeller(Long sellerUserId) {
//////////        return orderRepository.findBySeller_Id(sellerUserId);
//////////    }
//////////
//////////    // =========================
//////////    // GET ALL ORDERS
//////////    // =========================
//////////    public List<Order> getAllOrders() {
//////////        return orderRepository.findAll();
//////////    }
//////////}
////////
////////
////////
////////
////////
////////package com.farmer_retailer.service;
////////
////////import com.farmer_retailer.model.*;
////////import com.farmer_retailer.repository.*;
////////import org.springframework.beans.factory.annotation.Autowired;
////////import org.springframework.stereotype.Service;
////////
////////import java.time.LocalDateTime;
////////import java.util.List;
////////
////////@Service
////////public class OrderService {
////////
////////    @Autowired
////////    private UserRepository userRepository;
////////
////////    @Autowired
////////    private ProductRepository productRepository;
////////
////////    @Autowired
////////    private OrderRepository orderRepository;
////////
////////    @Autowired
////////    private NotificationService notificationService;
////////
////////    // =========================
////////    // PLACE ORDER (Retailer → Farmer)
////////    // =========================
////////    public Order placeOrder(Long buyerUserId, Long productId, int quantity) {
////////
////////        // Fetch buyer (Retailer)
////////        User buyer = userRepository.findById(buyerUserId)
////////                .orElseThrow(() -> new RuntimeException("Buyer user not found"));
////////
////////        // Fetch product
////////        Product product = productRepository.findById(productId)
////////                .orElseThrow(() -> new RuntimeException("Product not found"));
////////
////////        // Check stock
////////        if (product.getQuantity() < quantity) {
////////            throw new RuntimeException("Not enough quantity available");
////////        }
////////
////////        // Seller is the farmer who owns the product
////////        User seller = product.getFarmer();
////////
////////        // Create order
////////        Order order = new Order();
////////        order.setBuyer(buyer);
////////        order.setSeller(seller);
////////        order.setProduct(product);
////////        order.setQuantity(quantity);
////////        order.setAmount(product.getPrice() * quantity);
////////        order.setOrderDate(LocalDateTime.now());
////////        order.setStatus("PENDING");
////////
////////        // Reduce stock
////////        product.setQuantity(product.getQuantity() - quantity);
////////        productRepository.save(product);
////////
////////        Order savedOrder = orderRepository.save(order);
////////
////////        // 🔔 NOTIFY FARMER
////////        notificationService.notify(
////////                seller.getId(),
////////                "New order placed for " + product.getName() +
////////                        " (Qty: " + quantity + ")",
////////                "ORDER_PLACED"
////////        );
////////
////////        return savedOrder;
////////    }
////////
////////    // =========================
////////    // UPDATE ORDER STATUS (Farmer → Retailer)
////////    // =========================
////////    public Order updateOrderStatus(Long orderId, String status) {
////////
////////        Order order = orderRepository.findById(orderId)
////////                .orElseThrow(() -> new RuntimeException("Order not found"));
////////
////////        order.setStatus(status);
////////        Order updatedOrder = orderRepository.save(order);
////////
////////        // 🔔 NOTIFY RETAILER
////////        notificationService.notify(
////////                order.getBuyer().getId(),
////////                "Your order #" + order.getId() + " is now " + status,
////////                "ORDER_" + status
////////        );
////////
////////        return updatedOrder;
////////    }
////////
////////    // =========================
////////    // CANCEL ORDER (Retailer → Farmer)
////////    // =========================
////////    public void cancelOrder(Long orderId, Long buyerUserId) {
////////
////////        Order order = orderRepository.findById(orderId)
////////                .orElseThrow(() -> new RuntimeException("Order not found"));
////////
////////        if (!order.getBuyer().getId().equals(buyerUserId)) {
////////            throw new RuntimeException("Unauthorized cancel attempt");
////////        }
////////
////////        order.setStatus("CANCELLED");
////////        orderRepository.save(order);
////////
////////        // Restore stock
////////        Product product = order.getProduct();
////////        product.setQuantity(product.getQuantity() + order.getQuantity());
////////        productRepository.save(product);
////////
////////        // 🔔 NOTIFY FARMER
////////        notificationService.notify(
////////                order.getSeller().getId(),
////////                "Order #" + order.getId() + " was cancelled by retailer",
////////                "ORDER_CANCELLED"
////////        );
////////    }
////////
////////    // =========================
////////    // GET ORDERS BY BUYER
////////    // =========================
////////    public List<Order> getOrdersByBuyer(Long buyerUserId) {
////////        return orderRepository.findByBuyer_Id(buyerUserId);
////////    }
////////
////////    // =========================
////////    // GET ORDERS BY SELLER
////////    // =========================
////////    public List<Order> getOrdersBySeller(Long sellerUserId) {
////////        return orderRepository.findBySeller_Id(sellerUserId);
////////    }
////////
////////    // =========================
////////    // GET ALL ORDERS
////////    // =========================
////////    public List<Order> getAllOrders() {
////////        return orderRepository.findAll();
////////    }
////////}
//////
//////
////////package com.farmer_retailer.service;
////////
////////import com.farmer_retailer.model.*;
////////import com.farmer_retailer.repository.*;
////////import org.springframework.beans.factory.annotation.Autowired;
////////import org.springframework.stereotype.Service;
////////
////////import java.time.LocalDateTime;
////////import java.util.List;
////////
////////@Service
////////public class OrderService {
////////
////////    @Autowired
////////    private UserRepository userRepository;
////////
////////    @Autowired
////////    private ProductRepository productRepository;
////////
////////    @Autowired
////////    private OrderRepository orderRepository;
////////
////////    @Autowired
////////    private NotificationService notificationService;
////////
////////    // =========================
////////    // PLACE ORDER (Retailer → Farmer)
////////    // =========================
////////    public Order placeOrder(Long buyerUserId, Long productId, int quantity) {
////////
////////        // Fetch buyer (Retailer)
////////        User buyer = userRepository.findById(buyerUserId)
////////                .orElseThrow(() -> new RuntimeException("Buyer user not found"));
////////
////////        // Fetch product
////////        Product product = productRepository.findById(productId)
////////                .orElseThrow(() -> new RuntimeException("Product not found"));
////////
////////        // Check stock
////////        if (product.getQuantity() < quantity) {
////////            throw new RuntimeException("Not enough quantity available");
////////        }
////////
////////        // Seller is the farmer who owns the product
////////        User seller = product.getFarmer();
////////
////////        // Create order
////////        Order order = new Order();
////////        order.setBuyer(buyer);
////////        order.setSeller(seller);
////////        order.setProduct(product);
////////        order.setQuantity(quantity);
////////        order.setAmount(product.getPrice() * quantity);
////////        order.setOrderDate(LocalDateTime.now());
////////        order.setStatus("PLACED"); // ✅ initial status is now PLACED
////////
////////        // Reduce stock
////////        product.setQuantity(product.getQuantity() - quantity);
////////        productRepository.save(product);
////////
////////        Order savedOrder = orderRepository.save(order);
////////
////////        // 🔔 NOTIFY FARMER
////////        notificationService.notify(
////////                seller.getId(),
////////                "New order placed for " + product.getName() +
////////                        " (Qty: " + quantity + ")",
////////                "ORDER_PLACED"
////////        );
////////
////////        return savedOrder;
////////    }
////////
////////    // =========================
////////    // UPDATE ORDER STATUS (Farmer → Retailer)
////////    // =========================
////////    public Order updateOrderStatus(Long orderId, String status) {
////////
////////        // Status must be one of your workflow
////////        if (!status.equals("PLACED") &&
////////                !status.equals("PAID") &&
////////                !status.equals("ACCEPTED") &&
////////                !status.equals("PROCESSED") &&
////////                !status.equals("DELIVERED") &&
////////                !status.equals("CANCELLED")) {
////////            throw new RuntimeException("Invalid order status: " + status);
////////        }
////////
////////        Order order = orderRepository.findById(orderId)
////////                .orElseThrow(() -> new RuntimeException("Order not found"));
////////
////////        order.setStatus(status);
////////        Order updatedOrder = orderRepository.save(order);
////////
////////        // 🔔 NOTIFY RETAILER
////////        notificationService.notify(
////////                order.getBuyer().getId(),
////////                "Your order #" + order.getId() + " is now " + status,
////////                "ORDER_" + status
////////        );
////////
////////        return updatedOrder;
////////    }
////////
////////    // =========================
////////    // CANCEL ORDER (Retailer → Farmer)
////////    // =========================
////////    public void cancelOrder(Long orderId, Long buyerUserId) {
////////
////////        Order order = orderRepository.findById(orderId)
////////                .orElseThrow(() -> new RuntimeException("Order not found"));
////////
////////        if (!order.getBuyer().getId().equals(buyerUserId)) {
////////            throw new RuntimeException("Unauthorized cancel attempt");
////////        }
////////
////////        order.setStatus("CANCELLED");
////////        orderRepository.save(order);
////////
////////        // Restore stock
////////        Product product = order.getProduct();
////////        product.setQuantity(product.getQuantity() + order.getQuantity());
////////        productRepository.save(product);
////////
////////        // 🔔 NOTIFY FARMER
////////        notificationService.notify(
////////                order.getSeller().getId(),
////////                "Order #" + order.getId() + " was cancelled by retailer",
////////                "ORDER_CANCELLED"
////////        );
////////    }
////////
////////    // =========================
////////    // GET ORDERS BY BUYER
////////    // =========================
////////    public List<Order> getOrdersByBuyer(Long buyerUserId) {
////////        return orderRepository.findByBuyer_Id(buyerUserId);
////////    }
////////
////////    // =========================
////////    // GET ORDERS BY SELLER
////////    // =========================
////////    public List<Order> getOrdersBySeller(Long sellerUserId) {
////////        return orderRepository.findBySeller_Id(sellerUserId);
////////    }
////////
////////    // =========================
////////    // GET ALL ORDERS
////////    // =========================
////////    public List<Order> getAllOrders() {
////////        return orderRepository.findAll();
////////    }
////////}
//////package com.farmer_retailer.service;
//////
//////import com.farmer_retailer.model.*;
//////import com.farmer_retailer.repository.*;
//////import org.springframework.beans.factory.annotation.Autowired;
//////import org.springframework.stereotype.Service;
//////
//////import java.time.LocalDateTime;
//////import java.util.List;
//////
//////@Service
//////public class OrderService {
//////
//////    @Autowired
//////    private UserRepository userRepository;
//////
//////    @Autowired
//////    private ProductRepository productRepository;
//////
//////    @Autowired
//////    private OrderRepository orderRepository;
//////
//////    @Autowired
//////    private NotificationService notificationService;
//////
//////    @Autowired
//////    private EmailService emailService;
//////
//////    // =========================
//////    // PLACE ORDER (Retailer → Farmer)
//////    // =========================
//////    public Order placeOrder(Long buyerUserId, Long productId, int quantity) {
//////
//////        // Fetch buyer
//////        User buyer = userRepository.findById(buyerUserId)
//////                .orElseThrow(() -> new RuntimeException("Buyer user not found"));
//////
//////        // Fetch product
//////        Product product = productRepository.findById(productId)
//////                .orElseThrow(() -> new RuntimeException("Product not found"));
//////
//////        // Check stock
//////        if (product.getQuantity() < quantity) {
//////            throw new RuntimeException("Not enough quantity available");
//////        }
//////
//////        // Fetch seller (Farmer)
//////        User seller = product.getFarmer();
//////        if (seller == null) {
//////            throw new RuntimeException("Product has no farmer linked");
//////        }
//////
//////        // Create order
//////        Order order = new Order();
//////        order.setBuyer(buyer);
//////        order.setSeller(seller);
//////        order.setProduct(product);
//////        order.setQuantity(quantity);
//////        order.setAmount(product.getPrice() * quantity);
//////        order.setOrderDate(LocalDateTime.now());
//////        order.setStatus("PENDING_PAYMENT");
//////
//////        // Reduce stock
//////        product.setQuantity(product.getQuantity() - quantity);
//////        productRepository.save(product);
//////
//////        Order savedOrder = orderRepository.save(order);
//////
//////        // 🔔 Notify farmer (SAFE)
//////        try {
//////            notificationService.notify(
//////                    seller.getId(),
//////                    "New order placed for " + product.getName() +
//////                            " (Qty: " + quantity + ")",
//////                    "ORDER_PLACED"
//////            );
//////        } catch (Exception e) {
//////            e.printStackTrace(); // notification failure must NOT break order
//////        }
//////
//////        return savedOrder;
//////    }
//////
//////    // =========================
//////    // UPDATE ORDER STATUS (Farmer → Retailer)
//////    // =========================
//////    public Order updateOrderStatus(Long orderId, String status) {
//////
//////        if (!status.equals("PLACED") &&
//////                !status.equals("PAID") &&
//////                !status.equals("ACCEPTED") &&
//////                !status.equals("PROCESSED") &&
//////                !status.equals("DELIVERED") &&
//////                !status.equals("CANCELLED")) {
//////            throw new RuntimeException("Invalid order status: " + status);
//////        }
//////
//////        Order order = orderRepository.findById(orderId)
//////                .orElseThrow(() -> new RuntimeException("Order not found"));
//////
//////        order.setStatus(status);
//////        Order updatedOrder = orderRepository.save(order);
//////
//////        // 🔔 Notify retailer (SAFE)
//////        try {
//////            notificationService.notify(
//////                    order.getBuyer().getId(),
//////                    "Your order #" + order.getId() + " is now " + status,
//////                    "ORDER_" + status
//////            );
//////        } catch (Exception e) {
//////            e.printStackTrace();
//////        }
//////
//////        return updatedOrder;
//////    }
//////
//////    // =========================
//////    // CANCEL ORDER (Retailer → Farmer)
//////    // =========================
//////    public void cancelOrder(Long orderId, Long buyerUserId) {
//////
//////        Order order = orderRepository.findById(orderId)
//////                .orElseThrow(() -> new RuntimeException("Order not found"));
//////
//////        if (!order.getBuyer().getId().equals(buyerUserId)) {
//////            throw new RuntimeException("Unauthorized cancel attempt");
//////        }
//////
//////        order.setStatus("CANCELLED");
//////        orderRepository.save(order);
//////
//////        // Restore stock
//////        Product product = order.getProduct();
//////        product.setQuantity(product.getQuantity() + order.getQuantity());
//////        productRepository.save(product);
//////
//////        // 🔔 Notify farmer (SAFE)
//////        try {
//////            notificationService.notify(
//////                    order.getSeller().getId(),
//////                    "Order #" + order.getId() + " was cancelled by retailer",
//////                    "ORDER_CANCELLED"
//////            );
//////        } catch (Exception e) {
//////            e.printStackTrace();
//////        }
//////    }
//////
//////    // =========================
//////    // GET ORDERS BY BUYER
//////    // =========================
//////    public List<Order> getOrdersByBuyer(Long buyerUserId) {
//////        return orderRepository.findByBuyer_Id(buyerUserId);
//////    }
//////
//////    // =========================
//////    // GET ORDERS BY SELLER
//////    // =========================
//////    public List<Order> getOrdersBySeller(Long sellerUserId) {
//////        return orderRepository.findBySeller_Id(sellerUserId);
//////    }
//////
//////    // =========================
//////    // GET ALL ORDERS
//////    // =========================
//////    public List<Order> getAllOrders() {
//////        return orderRepository.findAll();
//////    }
//////}
////
////
////
////
//////package com.farmer_retailer.service;
//////
//////import com.farmer_retailer.model.*;
//////import com.farmer_retailer.repository.*;
//////import org.springframework.beans.factory.annotation.Autowired;
//////import org.springframework.stereotype.Service;
//////
//////import java.time.LocalDateTime;
//////import java.util.List;
//////
//////@Service
//////public class OrderService {
//////
//////    @Autowired
//////    private UserRepository userRepository;
//////
//////    @Autowired
//////    private ProductRepository productRepository;
//////
//////    @Autowired
//////    private OrderRepository orderRepository;
//////
//////    @Autowired
//////    private NotificationService notificationService;
//////
//////    @Autowired
//////    private EmailService emailService;
//////
//////    // =========================
//////    // PLACE ORDER (Retailer → Farmer)
//////    // =========================
//////    public Order placeOrder(Long buyerUserId, Long productId, int quantity) {
//////
//////        User buyer = userRepository.findById(buyerUserId)
//////                .orElseThrow(() -> new RuntimeException("Buyer not found"));
//////
//////        Product product = productRepository.findById(productId)
//////                .orElseThrow(() -> new RuntimeException("Product not found"));
//////
//////        if (product.getQuantity() < quantity) {
//////            throw new RuntimeException("Insufficient stock");
//////        }
//////
//////        User seller = product.getFarmer();
//////
//////        Order order = new Order();
//////        order.setBuyer(buyer);
//////        order.setSeller(seller);
//////        order.setProduct(product);
//////        order.setQuantity(quantity);
//////        order.setAmount(product.getPrice() * quantity);
//////        order.setOrderDate(LocalDateTime.now());
//////        order.setStatus("PLACED");
//////
//////        product.setQuantity(product.getQuantity() - quantity);
//////        productRepository.save(product);
//////
//////        Order savedOrder = orderRepository.save(order);
//////
//////        // 🔔 Notification
//////        notificationService.notify(
//////                seller.getId(),
//////                "New order placed for " + product.getName(),
//////                "ORDER_PLACED"
//////        );
//////
//////        // 📧 Email to farmer
//////        emailService.sendEmail(
//////                seller.getEmail(),
//////                "New Order Received",
//////                "You received a new order.\n\n" +
//////                        "Product: " + product.getName() +
//////                        "\nQuantity: " + quantity +
//////                        "\nOrder ID: " + savedOrder.getId()
//////        );
//////
//////        return savedOrder;
//////    }
//////
//////    // =========================
//////    // UPDATE ORDER STATUS
//////    // =========================
//////    public Order updateOrderStatus(Long orderId, String status) {
//////
//////        Order order = orderRepository.findById(orderId)
//////                .orElseThrow(() -> new RuntimeException("Order not found"));
//////
//////        order.setStatus(status);
//////        Order updatedOrder = orderRepository.save(order);
//////
//////        // 🔔 Notification
//////        notificationService.notify(
//////                order.getBuyer().getId(),
//////                "Order #" + order.getId() + " is now " + status,
//////                "ORDER_" + status
//////        );
//////
//////        // 📧 Email to retailer
//////        emailService.sendEmail(
//////                order.getBuyer().getEmail(),
//////                "Order Status Updated",
//////                "Your order #" + order.getId() +
//////                        " is now " + status
//////        );
//////
//////        return updatedOrder;
//////    }
//////
//////    // =========================
//////    // CANCEL ORDER
//////    // =========================
//////    public void cancelOrder(Long orderId, Long buyerUserId) {
//////
//////        Order order = orderRepository.findById(orderId)
//////                .orElseThrow(() -> new RuntimeException("Order not found"));
//////
//////        if (!order.getBuyer().getId().equals(buyerUserId)) {
//////            throw new RuntimeException("Unauthorized");
//////        }
//////
//////        order.setStatus("CANCELLED");
//////        orderRepository.save(order);
//////
//////        Product product = order.getProduct();
//////        product.setQuantity(product.getQuantity() + order.getQuantity());
//////        productRepository.save(product);
//////
//////        // 🔔 Notification
//////        notificationService.notify(
//////                order.getSeller().getId(),
//////                "Order #" + order.getId() + " cancelled",
//////                "ORDER_CANCELLED"
//////        );
//////
//////        // 📧 Email to farmer
//////        emailService.sendEmail(
//////                order.getSeller().getEmail(),
//////                "Order Cancelled",
//////                "Order #" + order.getId() + " has been cancelled by retailer."
//////        );
//////    }
//////
//////    public List<Order> getOrdersByBuyer(Long buyerUserId) {
//////        return orderRepository.findByBuyer_Id(buyerUserId);
//////    }
//////
//////    public List<Order> getOrdersBySeller(Long sellerUserId) {
//////        return orderRepository.findBySeller_Id(sellerUserId);
//////    }
//////
//////    public List<Order> getAllOrders() {
//////        return orderRepository.findAll();
//////    }
//////}
////
////
////
////
////
////package com.farmer_retailer.service;
////
////import com.farmer_retailer.model.Order;
////import com.farmer_retailer.model.Product;
////import com.farmer_retailer.model.User;
////import com.farmer_retailer.repository.OrderRepository;
////import com.farmer_retailer.repository.ProductRepository;
////import com.farmer_retailer.repository.UserRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////import java.time.LocalDateTime;
////import java.util.List;
////
////@Service
////public class OrderService {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    @Autowired
////    private ProductRepository productRepository;
////
////    @Autowired
////    private OrderRepository orderRepository;
////
////    @Autowired
////    private NotificationService notificationService;
////
////    @Autowired
////    private EmailService emailService;
////
////    // =========================
////    // PLACE ORDER (Retailer → Farmer)
////    // =========================
////    public Order placeOrder(Long buyerUserId, Long productId, int quantity) {
////        // Fetch buyer
////        User buyer = userRepository.findById(buyerUserId)
////                .orElseThrow(() -> new RuntimeException("Buyer not found"));
////
////        // Fetch product
////        Product product = productRepository.findById(productId)
////                .orElseThrow(() -> new RuntimeException("Product not found"));
////
////        if (product.getQuantity() < quantity) {
////            throw new RuntimeException("Insufficient stock");
////        }
////
////        // Fetch seller (farmer)
////        User seller = product.getFarmer();
////        if (seller == null) {
////            throw new RuntimeException("Seller not found for this product");
////        }
////
////        // Create order
////        Order order = new Order();
////        order.setBuyer(buyer);
////        order.setSeller(seller);
////        order.setProduct(product);
////        order.setQuantity(quantity);
////        order.setAmount(product.getPrice() * quantity);
////        order.setOrderDate(LocalDateTime.now());
////        order.setStatus("PLACED");
////
////        // Update product stock
////        product.setQuantity(product.getQuantity() - quantity);
////        productRepository.save(product);
////
////        // Save order
////        Order savedOrder = orderRepository.save(order);
////
////        // 🔔 Notification to farmer
////        notificationService.notify(
////                seller.getId(),
////                "New order placed for " + product.getName(),
////                "ORDER_PLACED"
////        );
////
////        // 📧 Email to farmer
////        emailService.sendEmail(
////                seller.getEmail(),
////                "New Order Received",
////                "You received a new order.\n\n" +
////                        "Product: " + product.getName() +
////                        "\nQuantity: " + quantity +
////                        "\nOrder ID: " + savedOrder.getId()
////        );
////
////        return savedOrder;
////    }
////
////    // =========================
////    // UPDATE ORDER STATUS
////    // =========================
////    public Order updateOrderStatus(Long orderId, String status) {
////        Order order = orderRepository.findById(orderId)
////                .orElseThrow(() -> new RuntimeException("Order not found"));
////
////        order.setStatus(status);
////        Order updatedOrder = orderRepository.save(order);
////
////        // 🔔 Notification to buyer
////        notificationService.notify(
////                order.getBuyer().getId(),
////                "Order #" + order.getId() + " is now " + status,
////                "ORDER_" + status
////        );
////
////        // 📧 Email to buyer
////        emailService.sendEmail(
////                order.getBuyer().getEmail(),
////                "Order Status Updated",
////                "Your order #" + order.getId() + " is now " + status
////        );
////
////        return updatedOrder;
////    }
////
////    // =========================
////    // CANCEL ORDER (by Buyer)
////    // =========================
////    public void cancelOrder(Long orderId, Long buyerUserId) {
////        Order order = orderRepository.findById(orderId)
////                .orElseThrow(() -> new RuntimeException("Order not found"));
////
////        if (!order.getBuyer().getId().equals(buyerUserId)) {
////            throw new RuntimeException("Unauthorized: You can only cancel your own orders");
////        }
////
////        order.setStatus("CANCELLED");
////        orderRepository.save(order);
////
////        // Restore product quantity
////        Product product = order.getProduct();
////        product.setQuantity(product.getQuantity() + order.getQuantity());
////        productRepository.save(product);
////
////        // 🔔 Notification to seller
////        notificationService.notify(
////                order.getSeller().getId(),
////                "Order #" + order.getId() + " cancelled by retailer",
////                "ORDER_CANCELLED"
////        );
////
////        // 📧 Email to seller
////        emailService.sendEmail(
////                order.getSeller().getEmail(),
////                "Order Cancelled",
////                "Order #" + order.getId() + " has been cancelled by the retailer."
////        );
////    }
////
////    // =========================
////    // GET ORDERS
////    // =========================
////    public List<Order> getOrdersByBuyer(Long buyerUserId) {
////        return orderRepository.findByBuyer_Id(buyerUserId);
////    }
////
////    public List<Order> getOrdersBySeller(Long sellerUserId) {
////        return orderRepository.findBySeller_Id(sellerUserId);
////    }
////
////    public List<Order> getAllOrders() {
////        return orderRepository.findAll();
////    }
////}
//
//
//
//
//package com.farmer_retailer.service;
//
//import com.farmer_retailer.model.Order;
//import com.farmer_retailer.model.Product;
//import com.farmer_retailer.model.User;
//import com.farmer_retailer.repository.OrderRepository;
//import com.farmer_retailer.repository.ProductRepository;
//import com.farmer_retailer.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class OrderService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private EmailService emailService;
//
//    // =========================
//    // PLACE ORDER (Retailer → Farmer)
//    // =========================
//    public Order placeOrder(Long buyerUserId, Long productId, int quantity) {
//
//        User buyer = userRepository.findById(buyerUserId)
//                .orElseThrow(() -> new RuntimeException("Buyer not found"));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        if (product.getQuantity() < quantity) {
//            throw new RuntimeException("Insufficient stock");
//        }
//
//        User seller = product.getFarmer();
//        if (seller == null) {
//            throw new RuntimeException("Farmer not linked to product");
//        }
//
//        Order order = new Order();
//        order.setBuyer(buyer);
//        order.setSeller(seller);
//        order.setProduct(product);
//        order.setQuantity(quantity);
//        order.setAmount(product.getPrice() * quantity);
//        order.setOrderDate(LocalDateTime.now());
//        order.setStatus("PLACED");
//
//        product.setQuantity(product.getQuantity() - quantity);
//        productRepository.save(product);
//
//        Order savedOrder = orderRepository.save(order);
//
//        // 🔔 Notification to farmer
//        notificationService.notify(
//                seller.getId(),
//                "New order placed for " + product.getName(),
//                "ORDER_PLACED"
//        );
//
//        // 📧 Email to farmer
//        System.out.println("📧 EMAIL BLOCK REACHED");
//        emailService.sendEmail(
//
//
//        seller.getEmail(),
//                "🧺 New Order Received",
//                "Hello " + seller.getName() + ",\n\n" +
//                        "You have received a new order.\n\n" +
//                        "🧾 Order ID: " + savedOrder.getId() + "\n" +
//                        "🌾 Product: " + product.getName() + "\n" +
//                        "📦 Quantity: " + quantity + "\n" +
//                        "💰 Amount: ₹" + savedOrder.getAmount() + "\n\n" +
//                        "Please log in to your dashboard to process the order.\n\n" +
//                        "Regards,\nFarmer Retailer System"
//        );
//
//        return savedOrder;
//    }
//
//    // =========================
//    // UPDATE ORDER STATUS (Farmer → Retailer)
//    // =========================
//    public Order updateOrderStatus(Long orderId, String status) {
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        order.setStatus(status);
//        Order updatedOrder = orderRepository.save(order);
//
//        // 🔔 Notification to retailer
//        notificationService.notify(
//                order.getBuyer().getId(),
//                "Your order #" + order.getId() + " is now " + status,
//                "ORDER_" + status
//        );
//
//        // 📧 Email to retailer
//        emailService.sendEmail(
//                order.getBuyer().getEmail(),
//                "📦 Order Status Updated",
//                "Hello " + order.getBuyer().getName() + ",\n\n" +
//                        "Your order status has been updated.\n\n" +
//                        "🧾 Order ID: " + order.getId() + "\n" +
//                        "📌 Status: " + status + "\n\n" +
//                        "Thank you for using our platform.\n\n" +
//                        "Regards,\nFarmer Retailer System"
//        );
//
//        return updatedOrder;
//    }
//
//    // =========================
//    // CANCEL ORDER (Retailer → Farmer)
//    // =========================
//    public void cancelOrder(Long orderId, Long buyerUserId) {
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        if (!order.getBuyer().getId().equals(buyerUserId)) {
//            throw new RuntimeException("Unauthorized cancellation");
//        }
//
//        order.setStatus("CANCELLED");
//        orderRepository.save(order);
//
//        Product product = order.getProduct();
//        product.setQuantity(product.getQuantity() + order.getQuantity());
//        productRepository.save(product);
//
//        // 🔔 Notification to farmer
//        notificationService.notify(
//                order.getSeller().getId(),
//                "Order #" + order.getId() + " was cancelled by retailer",
//                "ORDER_CANCELLED"
//        );
//
//        // 📧 Email to farmer
//        emailService.sendEmail(
//                order.getSeller().getEmail(),
//                "❌ Order Cancelled",
//                "Hello " + order.getSeller().getName() + ",\n\n" +
//                        "Order #" + order.getId() + " has been cancelled by the retailer.\n\n" +
//                        "Regards,\nFarmer Retailer System"
//        );
//    }
//
//    // =========================
//    // GETTERS
//    // =========================
//    public List<Order> getOrdersByBuyer(Long buyerUserId) {
//        return orderRepository.findByBuyer_Id(buyerUserId);
//    }
//
//    public List<Order> getOrdersBySeller(Long sellerUserId) {
//        return orderRepository.findBySeller_Id(sellerUserId);
//    }
//
//    public List<Order> getAllOrders() {
//        return orderRepository.findAll();
//    }
//}

package com.farmer_retailer.service;
import com.farmer_retailer.util.OrderPdfGenerator;

import com.farmer_retailer.model.Order;
import com.farmer_retailer.model.Product;
import com.farmer_retailer.model.User;
import com.farmer_retailer.repository.OrderRepository;
import com.farmer_retailer.repository.ProductRepository;
import com.farmer_retailer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    // =========================
    // PLACE ORDER (Retailer → Farmer)
    // =========================
//    public Order placeOrder(Long buyerUserId, Long productId, int quantity) {
//
//        User buyer = userRepository.findById(buyerUserId)
//                .orElseThrow(() -> new RuntimeException("Buyer not found"));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        if (product.getQuantity() < quantity) {
//            throw new RuntimeException("Insufficient stock");
//        }
//
//        User seller = product.getFarmer();
//        if (seller == null) {
//            throw new RuntimeException("Farmer not linked to product");
//        }
//
//        Order order = new Order();
//        order.setBuyer(buyer);
//        order.setSeller(seller);
//        order.setProduct(product);
//        order.setQuantity(quantity);
//        order.setAmount(product.getPrice() * quantity);
//        order.setOrderDate(LocalDateTime.now());
//        order.setStatus("PENDING_PAYMENT"); // ✅ matches controller
//
//        // Reduce stock
//        product.setQuantity(product.getQuantity() - quantity);
//        productRepository.save(product);
//
//        Order savedOrder = orderRepository.save(order);
//
//        // 🔔 Notification to farmer ONLY
//        notificationService.notify(
//                seller.getId(),
//                "New order placed for " + product.getName() +
//                        " (Qty: " + quantity + ")",
//                "ORDER_PLACED"
//        );
//
//        // ❌ NO EMAIL HERE (email goes after payment)
//
//        return savedOrder;
//    }
    public Order placeOrder(Long buyerUserId, Long productId, int quantity) {

        User buyer = userRepository.findById(buyerUserId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        User seller = product.getFarmer();
        if (seller == null) {
            throw new RuntimeException("Farmer not linked to product");
        }

        Order order = new Order();
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setAmount(product.getPrice() * quantity);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        Order savedOrder = orderRepository.save(order);

        // 🔔 Notification
        notificationService.notify(
                seller.getId(),
                "New order placed for " + product.getName() +
                        " (Qty: " + quantity + ")",
                "ORDER_PLACED"
        );

        // 📄 Generate PDF
        byte[] pdfBytes = OrderPdfGenerator.generateOrderPdf(savedOrder);

        // 📧 Email to farmer with PDF
        emailService.sendEmailWithPdf(
                seller.getEmail(),
                "🧾 New Order Received - Order #" + savedOrder.getId(),
                "Hello " + seller.getName() + ",\n\n" +
                        "You have received a new order.\n\n" +
                        "Please find the attached PDF for complete order details.\n\n" +
                        "Regards,\nFarmer Retailer System",
                pdfBytes
        );

        return savedOrder;
    }


    // =========================
    // UPDATE ORDER STATUS (Farmer → Retailer)
    // =========================
    public Order updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Cannot update a cancelled order");
        }

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        // 🔔 Notify retailer
        notificationService.notify(
                order.getBuyer().getId(),
                "Your order for " + order.getProduct().getName() +
                        " is now " + status,
                "ORDER_" + status
        );

        return updatedOrder;
    }

    // =========================
    // CANCEL ORDER (Retailer)
    // =========================
    public void cancelOrder(Long orderId, Long buyerUserId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getBuyer().getId().equals(buyerUserId)) {
            throw new RuntimeException("Unauthorized cancellation");
        }

        if (!order.getStatus().equals("PENDING_PAYMENT")) {
            throw new RuntimeException("Order cannot be cancelled after payment");
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);

        // Restore stock
        Product product = order.getProduct();
        product.setQuantity(product.getQuantity() + order.getQuantity());
        productRepository.save(product);

        // 🔔 Notify farmer
        notificationService.notify(
                order.getSeller().getId(),
                "Order #" + order.getId() + " was cancelled by retailer",
                "ORDER_CANCELLED"
        );
    }

    // =========================
    // GETTERS
    // =========================
    public List<Order> getOrdersByBuyer(Long buyerUserId) {
        return orderRepository.findByBuyer_Id(buyerUserId);
    }

    public List<Order> getOrdersBySeller(Long sellerUserId) {
        return orderRepository.findBySeller_Id(sellerUserId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
