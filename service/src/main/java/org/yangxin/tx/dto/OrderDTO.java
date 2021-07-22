package org.yangxin.tx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yangxin
 * 2021/7/14 下午10:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = -2932783318722628208L;

    private Long id;

    private String uuid;

    private Long customerId;

    private String title;

    private Long ticketNum;

    private Integer amount;

    private String status;
}
