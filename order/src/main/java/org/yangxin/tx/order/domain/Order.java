package org.yangxin.tx.order.domain;

import lombok.*;
import org.yangxin.tx.constant.OrderConstant;
import org.yangxin.tx.dto.OrderDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;

/**
 * @author yangxin
 * 2021/7/15 下午8:48
 */
@Entity(name = "t_customer_order")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private String uuid;

    private Long customerId;

    private String title;

    private Long ticketNum;

    private Integer amount;

    private String status;

    private String reason;

    private ZonedDateTime createdDate;

    public static Order newOrder(OrderDTO orderDTO) {
        return Order.builder()
                .uuid(orderDTO.getUuid())
                .amount(orderDTO.getAmount())
                .title(orderDTO.getTitle())
                .customerId(orderDTO.getCustomerId())
                .ticketNum(orderDTO.getTicketNum())
                .status(OrderConstant.STATUS_NEW)
                .createdDate(ZonedDateTime.now())
                .build();
    }
}
