package org.yangxin.tx.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yangxin.tx.constant.OrderConstant;
import org.yangxin.tx.constant.PayConstant;
import org.yangxin.tx.dto.OrderDTO;
import org.yangxin.tx.user.dao.CustomerRepository;
import org.yangxin.tx.user.dao.PayInfoRepository;
import org.yangxin.tx.user.domain.Customer;
import org.yangxin.tx.user.domain.PayInfo;

/**
 * @author yangxin
 * 2021/7/20 下午10:26
 */
@SuppressWarnings("AlibabaRemoveCommentedCode")
@Service
@Slf4j
public class CustomerService {

    private final JmsTemplate jmsTemplate;
    private final CustomerRepository customerRepository;
    private final PayInfoRepository payInfoRepository;

    @Autowired
    public CustomerService(JmsTemplate jmsTemplate, CustomerRepository customerRepository, PayInfoRepository payInfoRepository) {
        this.jmsTemplate = jmsTemplate;
        this.customerRepository = customerRepository;
        this.payInfoRepository = payInfoRepository;
    }

    @Transactional(rollbackFor = Throwable.class)
    @JmsListener(destination = OrderConstant.QUEUE_ORDER_PAY, containerFactory = "msgFactory")
    public void handle(OrderDTO orderDTO) {
        log.info("Get new order to pay: [{}]", orderDTO);

        // 先检查payInfo判断重复消息
        PayInfo payInfo = payInfoRepository.findOneByOrderId(orderDTO.getId());
        if (payInfo != null) {
            log.warn("Order already paid, duplicated message.");
            return;
        }

        Customer customer = customerRepository.findOne(orderDTO.getCustomerId());
        if (customer.getDeposit() < orderDTO.getAmount()) {
            log.info("No enough deposit, need amount: [{}]", orderDTO.getAmount());

            orderDTO.setStatus(OrderConstant.STATUS_NOT_ENOUGH_DEPOSIT);
            jmsTemplate.convertAndSend(OrderConstant.QUEUE_TICKET_ERROR, orderDTO);
            return;
        }

        payInfo = new PayInfo();
        payInfo.setOrderId(orderDTO.getId());
        payInfo.setAmount(orderDTO.getAmount());
        payInfo.setStatus(PayConstant.STATUS_PAID);

        payInfoRepository.save(payInfo);
//        customer.setDeposit(customer.getDeposit() - orderDTO.getAmount());
        // 如果用户下了2个订单，这个handle方法不是单线程处理，或者有多个实例，又刚好这2个请求被同时处理
        customerRepository.charge(orderDTO.getCustomerId(), orderDTO.getAmount());

        orderDTO.setStatus(PayConstant.STATUS_PAID);
        jmsTemplate.convertAndSend(OrderConstant.QUEUE_TICKET_MOVE, orderDTO);
    }
}
