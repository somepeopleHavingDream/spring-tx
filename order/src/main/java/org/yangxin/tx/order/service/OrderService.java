package org.yangxin.tx.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yangxin.tx.constant.OrderConstant;
import org.yangxin.tx.constant.TicketConstant;
import org.yangxin.tx.dto.OrderDTO;
import org.yangxin.tx.order.dao.OrderRepository;
import org.yangxin.tx.order.domain.Order;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author yangxin
 * 2021/7/15 下午8:55
 */
@Service
@Slf4j
public class OrderService {

    private final JmsTemplate jmsTemplate;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(JmsTemplate jmsTemplate, OrderRepository orderRepository) {
        this.jmsTemplate = jmsTemplate;
        this.orderRepository = orderRepository;
    }

    @Transactional(rollbackFor = Throwable.class)
    @JmsListener(destination = OrderConstant.QUEUE_ORDER_LOCKED, containerFactory = "msgFactory")
    public void handle(OrderDTO orderDTO) {
        log.info("Get new order to create: [{}]", orderDTO);

        // 通过保存到数据库，来使用uuid处理重复消息
        if (orderRepository.findOneByUuid(orderDTO.getUuid()) != null) {
            log.info("Msg already processed: [{}]", orderDTO);
        } else {
            Order order = Order.newOrder(orderDTO);
            orderRepository.save(order);
            orderDTO.setId(order.getId());
        }

        orderDTO.setStatus(OrderConstant.STATUS_NEW);
        jmsTemplate.convertAndSend(OrderConstant.QUEUE_ORDER_PAY, orderDTO);
    }

    @Transactional(rollbackFor = Throwable.class)
    @JmsListener(destination = OrderConstant.QUEUE_ORDER_FINISH, containerFactory = "msgFactory")
    public void handleFinish(OrderDTO msg) {
        log.info("Get finished order: [{}]", msg);

        Order order = orderRepository.findOne(msg.getId());
        order.setStatus(OrderConstant.STATUS_FINISH);

        orderRepository.save(order);
    }

    @Transactional(rollbackFor = Throwable.class)
    @JmsListener(destination = OrderConstant.QUEUE_ORDER_FAIL, containerFactory = "msgFactory")
    public void handleFailed(OrderDTO orderDTO) {
        if (log.isInfoEnabled()) {
            log.info("Get failed order: [{}]", orderDTO);
        }

        Order order;
        if (orderDTO.getId() == null) {
            // 创建订单，用于失败
            order = Order.newOrder(orderDTO);
            // 锁票失败
            order.setReason(OrderConstant.STATUS_TICKET_LOCK_FAIL);
        } else {
            order = orderRepository.findOne(orderDTO.getId());
            if (Objects.equals(orderDTO.getStatus(), OrderConstant.STATUS_NOT_ENOUGH_DEPOSIT)) {
                order.setReason(OrderConstant.STATUS_NOT_ENOUGH_DEPOSIT);
            }
        }

        order.setStatus(OrderConstant.STATUS_FAIL);
        orderRepository.save(order);
    }

    @Scheduled(fixedDelay = 10000L)
    public void checkInvalidOrder() {
        ZonedDateTime checkTime = ZonedDateTime.now().minusMinutes(1);

        List<Order> orderList = orderRepository.findAllByStatusAndCreatedDateBefore("NEW", checkTime);
        for (Order order : orderList) {
            log.error("Order timeout: [{}]", order);

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setTicketNum(order.getTicketNum());
            orderDTO.setUuid(order.getUuid());
            orderDTO.setAmount(order.getAmount());
            orderDTO.setTitle(order.getTitle());
            orderDTO.setCustomerId(order.getCustomerId());
            orderDTO.setStatus(OrderConstant.STATUS_TIMEOUT);

            jmsTemplate.convertAndSend(TicketConstant.QUEUE_TICKET_ERROR);
        }
    }
}
