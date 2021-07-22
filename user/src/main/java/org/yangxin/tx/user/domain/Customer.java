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
 * 2021/7/20 下午10:10
 */
@SuppressWarnings({"unused", "JpaDataSourceORMInspection"})
@Entity(name = "t_customer")
@Getter
@Setter
@ToString
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_name")
    private String username;

    private String password;

    private String role;

    private Integer deposit;
}
