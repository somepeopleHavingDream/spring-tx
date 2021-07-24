package org.yangxin.tx.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.yangxin.tx.user.domain.Customer;

/**
 * @author yangxin
 * 2021/7/20 下午10:18
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "UnusedReturnValue"})
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findOneByUsername(String username);

    @Modifying
    @Query("UPDATE t_customer SET deposit = deposit - ?2 WHERE id = ?1")
    int charge(Long customerId, int amount);
}
