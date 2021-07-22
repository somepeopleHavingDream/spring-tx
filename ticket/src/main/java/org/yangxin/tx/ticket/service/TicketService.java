package org.yangxin.tx.ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yangxin.tx.constant.OrderConstant;
import org.yangxin.tx.dto.OrderDTO;
import org.yangxin.tx.ticket.dao.TicketRepository;
import org.yangxin.tx.ticket.domain.Ticket;

/**
 * @author yangxin
 * 2021/7/19 下午9:58
 */
@Service
@Slf4j
public class TicketService {

    private final JmsTemplate jmsTemplate;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(JmsTemplate jmsTemplate, TicketRepository ticketRepository) {
        this.jmsTemplate = jmsTemplate;
        this.ticketRepository = ticketRepository;
    }

    @Transactional(rollbackFor = Throwable.class)
    @JmsListener(destination = OrderConstant.QUEUE_ORDER_NEW, containerFactory = "msgFactory")
    public void handleTicketLock(OrderDTO orderDTO) {
        log.info("Get new order for ticket lock: [{}]", orderDTO);

        int lockCount = ticketRepository.lockTicket(orderDTO.getCustomerId(), orderDTO.getTicketNum());
        if (lockCount == 0) {
            orderDTO.setStatus(OrderConstant.STATUS_TICKET_LOCK_FAIL);
            jmsTemplate.convertAndSend(OrderConstant.QUEUE_ORDER_FAIL, orderDTO);
        } else {
            orderDTO.setStatus(OrderConstant.STATUS_LOCKED);
            jmsTemplate.convertAndSend(OrderConstant.QUEUE_ORDER_LOCKED, orderDTO);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @JmsListener(destination = OrderConstant.QUEUE_TICKET_MOVE, containerFactory = "msgFactory")
    public void handleTicketMove(OrderDTO orderDTO) {
        log.info("Get new order for ticket move: [{}]", orderDTO);

        int moveCount = ticketRepository.moveTicket(orderDTO.getCustomerId(), orderDTO.getTicketNum());
        if (moveCount == 0) {
            log.info("Ticket already transferred.");
        }

        orderDTO.setStatus(OrderConstant.STATUS_MOVED);
        jmsTemplate.convertAndSend(OrderConstant.QUEUE_ORDER_FINISH, orderDTO);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Ticket lockTicket(OrderDTO orderDTO) {
        Ticket ticket = ticketRepository.findOneByTicketNum(orderDTO.getTicketNum());
        if (ticket == null) {
            return null;
        }

        ticket.setLockUser(orderDTO.getCustomerId());
        ticket = ticketRepository.save(ticket);

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        return ticket;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Integer lockTicket2(OrderDTO orderDTO) {
        int updateCount = ticketRepository.lockTicket(orderDTO.getCustomerId(), orderDTO.getTicketNum());
        log.info("Updated ticket count: [{}]", updateCount);

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        return updateCount;
    }
}
