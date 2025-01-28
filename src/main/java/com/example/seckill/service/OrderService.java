package com.example.seckill.service;

import com.example.seckill.entity.Order;

public interface OrderService {

    Order createOrder(int userId, int itemId, int amount, Integer promotionId, String itemStockLogId);

    void createOrderAsync(int userId, int itemId, int amount, Integer promotionId);
}
