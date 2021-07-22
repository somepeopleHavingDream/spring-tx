package org.yangxin.tx.user.web;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yangxin.tx.dto.OrderDTO;
import org.yangxin.tx.dto.TicketDTO;
import org.yangxin.tx.service.OrderCompositeService;
import org.yangxin.tx.service.TicketCompositeService;
import org.yangxin.tx.user.dao.CustomerRepository;
import org.yangxin.tx.user.domain.Customer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangxin
 * 2021/7/21 下午9:54
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerResource {

    private final CustomerRepository customerRepository;

    private final OrderCompositeService orderClient;
    private final TicketCompositeService ticketClient;

    @Autowired
    public CustomerResource(CustomerRepository customerRepository, OrderCompositeService orderClient, TicketCompositeService ticketClient) {
        this.customerRepository = customerRepository;
        this.orderClient = orderClient;
        this.ticketClient = ticketClient;
    }

    @PostConstruct
    public void init() {
        if (customerRepository.count() > 0) {
            return;
        }

        Customer customer = new Customer();
        customer.setUsername("mooc");
        customer.setPassword("111111");
        customer.setRole("User");
        customer.setDeposit(1000);
        customerRepository.save(customer);
    }

    @PostMapping("")
    public Customer create(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping("")
    @HystrixCommand
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @GetMapping("/my")
    @HystrixCommand
    public Map<String, Object> getMyInfo() {
        Customer customer = customerRepository.findOneByUsername("mooc");

        List<OrderDTO> orderDTOList = orderClient.getMyOrder(customer.getId());
        List<TicketDTO> ticketDTOList = ticketClient.getMyTickets(customer.getId());

        Map<String, Object> result = new HashMap<>(3);
        result.put("customer", customer);
        result.put("orders", orderDTOList);
        result.put("tickets", ticketDTOList);

        return result;
    }
}
