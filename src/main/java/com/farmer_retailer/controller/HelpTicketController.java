package com.farmer_retailer.controller;

import com.farmer_retailer.dto.HelpTicketRequest;
import com.farmer_retailer.model.HelpTicket;
import com.farmer_retailer.service.HelpTicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/help")
@CrossOrigin(origins = "http://localhost:5173")
public class HelpTicketController {

    private final HelpTicketService helpTicketService;

    public HelpTicketController(HelpTicketService helpTicketService) {
        this.helpTicketService = helpTicketService;
    }

    // CREATE TICKET
    @PostMapping("/create")
    public HelpTicket createTicket(@RequestBody HelpTicketRequest request) {
        return helpTicketService.createTicket(request);
    }

    // VIEW MY TICKETS
    @GetMapping("/user/{userId}")
    public List<HelpTicket> getMyTickets(@PathVariable Long userId) {
        return helpTicketService.getTicketsByUser(userId);
    }
}
