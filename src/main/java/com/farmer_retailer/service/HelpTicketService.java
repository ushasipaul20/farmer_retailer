package com.farmer_retailer.service;

import com.farmer_retailer.dto.HelpTicketRequest;
import com.farmer_retailer.model.HelpTicket;
import com.farmer_retailer.model.User;
import com.farmer_retailer.repository.HelpTicketRepository;
import com.farmer_retailer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HelpTicketService {

    private final HelpTicketRepository helpTicketRepository;
    private final UserRepository userRepository;

    public HelpTicketService(HelpTicketRepository helpTicketRepository,
                             UserRepository userRepository) {
        this.helpTicketRepository = helpTicketRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // CREATE TICKET
    // =========================
    public HelpTicket createTicket(HelpTicketRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        HelpTicket ticket = new HelpTicket();
        ticket.setUser(user);
        ticket.setRole(request.getRole());
        ticket.setSubject(request.getSubject());
        ticket.setMessage(request.getMessage());
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());

        return helpTicketRepository.save(ticket);
    }

    // =========================
    // ADMIN REPLY
    // =========================
    public HelpTicket replyToTicket(Long ticketId, String reply) {

        HelpTicket ticket = helpTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setAdminReply(reply);
        ticket.setStatus("REPLIED");
        ticket.setRepliedAt(LocalDateTime.now());

        return helpTicketRepository.save(ticket);
    }

    // =========================
    // GETTERS
    // =========================
    public List<HelpTicket> getTicketsByUser(Long userId) {
        return helpTicketRepository.findByUser_Id(userId);
    }

    public List<HelpTicket> getAllTickets() {
        return helpTicketRepository.findAll();
    }

    public List<HelpTicket> getOpenTickets() {
        return helpTicketRepository.findByStatus("OPEN");
    }
}
