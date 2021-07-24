package org.yangxin.tx.order.web;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.yangxin.tx.constant.OrderConstant;
import org.yangxin.tx.dto.OrderDTO;
import org.yangxin.tx.order.dao.OrderRepository;
import org.yangxin.tx.order.domain.Order;
import org.yangxin.tx.service.OrderCompositeService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangxin
 * 2021/7/15 下午9:52
 */
@SuppressWarnings("AlibabaServiceOrDaoClassShouldEndWithImpl")
@RestController
@RequestMapping("/api/order")
public class OrderResource implements OrderCompositeService {

    private final OrderRepository orderRepository;
    private final JmsTemplate jmsTemplate;

    private final TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

    @Autowired
    public OrderResource(OrderRepository orderRepository, JmsTemplate jmsTemplate) {
        this.orderRepository = orderRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional(rollbackFor = Throwable.class)
    @PostMapping
    public void create(@RequestBody OrderDTO orderDTO) {
        orderDTO.setUuid(uuidGenerator.generate().toString());
        jmsTemplate.convertAndSend(OrderConstant.QUEUE_ORDER_NEW, orderDTO);
    }

    @Override
    @GetMapping("/{customerId}")
    public List<OrderDTO> getMyOrder(@PathVariable Long customerId) {
        List<Order> orderList = orderRepository.findAllByCustomerId(customerId);

        return orderList.stream()
                .map(order -> OrderDTO.builder()
                        .id(order.getId())
                        .status(order.getStatus())
                        .ticketNum(order.getTicketNum())
                        .amount(order.getAmount())
                        .customerId(order.getCustomerId())
                        .title(order.getTitle())
                        .uuid(order.getUuid())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping()
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
}
