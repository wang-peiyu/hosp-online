package com.onlinehosp.yygh.order.service;

import com.onlinehosp.yygh.model.order.PaymentInfo;
import com.onlinehosp.yygh.model.order.RefundInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RefundInfoService extends IService<RefundInfo> {
    /**
     * 保存退款记录
     * @param paymentInfo
     */
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}
