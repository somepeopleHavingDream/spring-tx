package org.yangxin.tx.user.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.yangxin.tx.dto.OrderDTO;
import org.yangxin.tx.service.OrderCompositeService;

import java.util.List;

/**
 * @author yangxin
 * 2021/7/20 下午10:22
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@FeignClient(value = "order", path = "/api/order")
public interface OrderClient extends OrderCompositeService {

    @GetMapping("/{customerId}")
    @Override
    List<OrderDTO> getMyOrder(@PathVariable(name = "customerId") Long id);
}
