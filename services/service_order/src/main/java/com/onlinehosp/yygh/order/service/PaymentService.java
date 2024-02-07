package com.onlinehosp.yygh.order.service;

import com.onlinehosp.yygh.model.order.OrderInfo;
import com.onlinehosp.yygh.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {
    void savePaymentInfo(OrderInfo order, Integer status);

    void paySuccess(String outTradeNo, Map<String, String> resultMap);

    PaymentInfo getPaymentInfo(Long orderId,Integer paymentType);
}
