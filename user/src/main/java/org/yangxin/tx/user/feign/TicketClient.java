package org.yangxin.tx.user.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.yangxin.tx.dto.TicketDTO;
import org.yangxin.tx.service.TicketCompositeService;

import java.util.List;

/**
 * @author yangxin
 * 2021/7/20 下午10:24
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@FeignClient(value = "ticket", path = "/api/ticket")
public interface TicketClient extends TicketCompositeService {

    @GetMapping("/{customerId}")
    @Override
    List<TicketDTO> getMyTickets(@PathVariable(name = "customerId") Long customerId);
}
