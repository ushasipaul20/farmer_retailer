package com.farmer_retailer.repository;

import com.farmer_retailer.model.HelpTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpTicketRepository extends JpaRepository<HelpTicket, Long> {

    List<HelpTicket> findByUser_Id(Long userId);

    List<HelpTicket> findByStatus(String status);
}
