//package com.farmer_retailer.repository;
//
//import com.farmer_retailer.model.Feedback;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
//
//    Optional<Feedback> findByOrder_Id(Long orderId);
//
//    List<Feedback> findByFarmer_Id(Long farmerId);
//
//    List<Feedback> findByRetailer_Id(Long retailerId);
//}
package com.farmer_retailer.repository;

import com.farmer_retailer.model.Feedback;
import com.farmer_retailer.model.Product;
import com.farmer_retailer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // ✅ Find feedback by order
    Optional<Feedback> findByOrder_Id(Long orderId);

    // ✅ All feedbacks for a farmer
    List<Feedback> findByFarmer_Id(Long farmerId);

    // ✅ All feedbacks submitted by a retailer
    List<Feedback> findByRetailer_Id(Long retailerId);

    // ✅ Feedbacks for a specific farmer and product (product-level feedback)
    @Query("""
        SELECT f
        FROM Feedback f
        WHERE f.farmer = :farmer
        AND f.order.product = :product
        ORDER BY f.createdAt DESC
    """)
    List<Feedback> findByFarmerAndProduct(
            @Param("farmer") User farmer,
            @Param("product") Product product
    );
}
