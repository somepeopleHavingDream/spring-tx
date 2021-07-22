package org.yangxin.tx.ticket.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author yangxin
 * 2021/7/19 下午9:29
 */
@Entity(name = "t_ticket")
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue
    private Long id;

    private Long ticketNum;

    private String name;

    private Long lockUser;

    private Long owner;
}
