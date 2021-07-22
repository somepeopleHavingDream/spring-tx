package org.yangxin.tx.user.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author yangxin
 * 2021/7/20 下午10:17
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity(name = "t_pay_info")
@Getter
@Setter
@ToString
public class PayInfo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    private String status;

    private Integer amount;
}
