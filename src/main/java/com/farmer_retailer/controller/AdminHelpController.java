package com.farmer_retailer.controller;

import com.farmer_retailer.dto.HelpReplyRequest;
import com.farmer_retailer.model.HelpTicket;
import com.farmer_retailer.service.HelpTicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/help")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminHelpController {

    private final HelpTicketService helpTicketService;

    public AdminHelpController(HelpTicketService helpTicketService) {
        this.helpTicketService = helpTicketService;
    }

    // VIEW ALL TICKETS
    @GetMapping("/all")
    public List<HelpTicket> getAllTickets() {
        return helpTicketService.getAllTickets();
    }

    // VIEW OPEN TICKETS
    @GetMapping("/open")
    public List<HelpTicket> getOpenTickets() {
        return helpTicketService.getOpenTickets();
    }

    // REPLY TO TICKET
    @PutMapping("/{ticketId}/reply")
    public HelpTicket reply(
            @PathVariable Long ticketId,
            @RequestBody HelpReplyRequest request
    ) {
        return helpTicketService.replyToTicket(ticketId, request.getReply());
    }
}
