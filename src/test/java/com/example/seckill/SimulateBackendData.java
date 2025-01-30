package com.example.seckill;

import com.example.seckill.dao.ItemMapper;
import com.example.seckill.dao.ItemStockMapper;
import com.example.seckill.dao.PromotionMapper;
import com.example.seckill.entity.Item;
import com.example.seckill.entity.ItemStock;
import com.example.seckill.entity.Promotion;
import com.example.seckill.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest
public class SimulateBackendData {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void cacheItemStock() {
        List<Item> list = itemService.findItemsOnPromotion();
        for (Item item : list) {
            int stock = item.getItemStock().getStock();
            redisTemplate.opsForValue().set("item:stock:" + item.getId(), stock);
        }
    }

    @Test
    public void initPromotionGate() {
        List<Item> list = itemService.findItemsOnPromotion();
        for (Item item : list) {
            int stock = item.getItemStock().getStock();
            Promotion promotion = item.getPromotion();
            redisTemplate.opsForValue().set("promotion:gate:" + promotion.getId(), stock * 5);
        }
    }

}
