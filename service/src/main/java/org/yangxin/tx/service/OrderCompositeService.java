package org.yangxin.tx.service;

import org.yangxin.tx.dto.OrderDTO;

import java.util.List;

/**
 * @author yangxin
 * 2021/7/14 下午10:20
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface OrderCompositeService {

    List<OrderDTO> getMyOrder(Long id);
}
