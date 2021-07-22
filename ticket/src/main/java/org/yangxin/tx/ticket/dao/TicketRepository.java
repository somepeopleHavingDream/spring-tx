package org.yangxin.tx.ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.yangxin.tx.ticket.domain.Ticket;

import java.util.List;

/**
 * @author yangxin
 * 2021/7/19 下午9:31
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unchecked"})
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByOwner(Long owner);

    Ticket findOneByTicketNum(Long num);

    @Modifying
    @Query("UPDATE t_ticket SET lockUser = ?1 WHERE lockUser is NULL and ticketNum = ?2")
    int lockTicket(Long customerId, Long ticketNum);

    @Modifying
    @Query("UPDATE t_ticket SET lockUser = null WHERE lockUser = ?1 and ticketNum = ?2")
    int unLockTicket(Long customerId, Long ticketNum);

    @Modifying
    @Query("UPDATE t_ticket SET owner = ?1, lockUser = null WHERE lockUser = ?1 and ticketNum = ?2")
    int moveTicket(Long customerId, Long ticketNum);

    @Modifying
    @Query("UPDATE t_ticket SET owner = ?1, lockUser = null WHERE lockUser = ?1 and ticketNum = ?2")
    int unMoveTicket(Long customerId, Long ticketNum);

    @Override
    @Modifying(clearAutomatically = true)
    Ticket save(Ticket o);
}
