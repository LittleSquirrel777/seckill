package com.example.seckill.rocketmq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.example.seckill.service.ItemService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "seckill",
        consumerGroup = "seckill_sales", selectorExpression = "increase_sales")
public class IncreaseSalesConsumer implements RocketMQListener<String> {

    private Logger logger = LoggerFactory.getLogger(DecreaseStockConsumer.class);

    @Autowired
    private ItemService itemService;

    @Override
    public void onMessage(String message) {
        JSONObject param = JSONObject.parseObject(message);
        int itemId = (int) param.get("itemId");
        int amount = (int) param.get("amount");

        try {
            itemService.increaseSales(itemId, amount);
            logger.debug("更新销量完成 [" + itemId + "]");
        } catch (Exception e) {
            logger.error("更新销量失败", e);
        }
    }

}
