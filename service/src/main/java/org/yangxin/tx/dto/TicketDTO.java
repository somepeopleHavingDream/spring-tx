package org.yangxin.tx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yangxin
 * 2021/7/14 下午10:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO implements Serializable {

    private static final long serialVersionUID = 1635734855827444126L;

    private Long id;

    private Long ticketNum;

    private String name;

    private Long lockUser;

    private Long owner;
}
