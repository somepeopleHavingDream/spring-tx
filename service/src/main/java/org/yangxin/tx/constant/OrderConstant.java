package org.yangxin.tx.constant;

/**
 * @author yangxin
 * 2021/7/15 下午9:35
 */
public interface OrderConstant {

    /*
        订单状态
     */

    String STATUS_NOT_ENOUGH_DEPOSIT = "NOT_ENOUGH_DEPOSIT";

    String STATUS_NEW = "NEW";

    String STATUS_FINISH = "FINISH";

    String STATUS_TICKET_LOCK_FAIL = "TICKET_LOCK_FAIL";

    String STATUS_FAIL = "FAIL";

    String STATUS_TIMEOUT = "TIMEOUT";

    String STATUS_LOCKED = "TICKET_LOCKED";

    String STATUS_MOVED = "TICKET_MOVED";

    /*
        队列
     */

    String QUEUE_ORDER_LOCKED = "order:locked";

    String QUEUE_ORDER_PAY = "order:pay";

    String QUEUE_ORDER_FINISH = "order:finish";

    String QUEUE_ORDER_FAIL = "order:fail";

    String QUEUE_ORDER_NEW = "order:new";

    String QUEUE_TICKET_MOVE = "order:ticket_move";

    String QUEUE_TICKET_ERROR = "order:ticket_error";
}
