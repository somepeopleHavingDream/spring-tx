package org.yangxin.tx.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yangxin.tx.user.domain.PayInfo;

/**
 * @author yangxin
 * 2021/7/20 下午10:21
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface PayInfoRepository extends JpaRepository<PayInfo, Long> {

    PayInfo findOneByOrderId(Long orderId);
}
