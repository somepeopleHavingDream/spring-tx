package org.yangxin.tx.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yangxin.tx.order.domain.Order;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author yangxin
 * 2021/7/15 下午8:52
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerId(Long customerId);

    List<Order> findAllByStatusAndCreatedDateBefore(String status, ZonedDateTime checkTime);

    Order findOneByUuid(String uuid);
}
