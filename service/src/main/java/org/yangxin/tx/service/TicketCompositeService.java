package org.yangxin.tx.service;

import org.yangxin.tx.dto.TicketDTO;

import java.util.List;

/**
 * @author yangxin
 * 2021/7/14 下午10:22
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface TicketCompositeService {

    List<TicketDTO> getMyTickets(Long customerId);
}
