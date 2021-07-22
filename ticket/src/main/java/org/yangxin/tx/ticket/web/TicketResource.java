package org.yangxin.tx.ticket.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yangxin.tx.dto.OrderDTO;
import org.yangxin.tx.dto.TicketDTO;
import org.yangxin.tx.service.TicketCompositeService;
import org.yangxin.tx.ticket.dao.TicketRepository;
import org.yangxin.tx.ticket.domain.Ticket;
import org.yangxin.tx.ticket.service.TicketService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangxin
 * 2021/7/20 下午9:34
 */
@SuppressWarnings("AlibabaServiceOrDaoClassShouldEndWithImpl")
@RestController
@RequestMapping("/api/ticket")
public class TicketResource implements TicketCompositeService {

    private final TicketRepository ticketRepository;
    private final TicketService ticketService;

    @Autowired
    public TicketResource(TicketRepository ticketRepository, TicketService ticketService) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
    }

    @PostConstruct
    public void init() {
        if (ticketRepository.count() > 0) {
            return;
        }

        Ticket ticket = new Ticket();
        ticket.setName("No.1");
        ticket.setTicketNum(100L);

        ticketRepository.save(ticket);
    }

    @PostMapping("")
    public OrderDTO create(@RequestBody OrderDTO orderDTO) {
        return orderDTO;
    }

    @GetMapping("/{customerId}")
    @Override
    public List<TicketDTO> getMyTickets(@PathVariable(name = "customerId") Long customerId) {
        List<Ticket> ticketList = ticketRepository.findAllByOwner(customerId);
        return ticketList.stream()
                .map(ticket -> TicketDTO.builder()
                        .ticketNum(ticket.getTicketNum())
                        .id(ticket.getId())
                        .lockUser(ticket.getLockUser())
                        .name(ticket.getName())
                        .owner(ticket.getOwner())
                        .build())
                .collect(Collectors.toList());
    }

    @PostMapping("/lock")
    public Ticket lock(@RequestBody OrderDTO orderDTO) {
        return ticketService.lockTicket(orderDTO);
    }

    @PostMapping("/lock2")
    public Integer lock2(@RequestBody OrderDTO orderDTO) {
        return ticketService.lockTicket2(orderDTO);
    }

    @GetMapping("")
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }
}
