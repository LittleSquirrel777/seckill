package com.example.seckill.service.impl;

import com.example.seckill.common.BusinessException;
import com.example.seckill.common.ErrorCode;
import com.example.seckill.component.ObjectValidator;
import com.example.seckill.dao.ItemMapper;
import com.example.seckill.dao.ItemStockMapper;
import com.example.seckill.dao.PromotionMapper;
import com.example.seckill.entity.Item;
import com.example.seckill.entity.ItemStock;
import com.example.seckill.entity.Promotion;
import com.example.seckill.service.ItemService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService, ErrorCode {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemStockMapper itemStockMapper;

    @Autowired
    private PromotionMapper promotionMapper;

    @Autowired
    private ObjectValidator validator;

    @Autowired
    private RedisTemplate redisTemplate;

    // 本地缓存
    private Cache<String, Object> cache;

    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
    }

    public List<Item> findItemsOnPromotion() {
        List<Item> items = itemMapper.selectOnPromotion();
        return items.stream().map(item -> {
            // 查库存
            ItemStock stock = itemStockMapper.selectByItemId(item.getId());
            item.setItemStock(stock);
            // 查活动
            Promotion promotion = promotionMapper.selectByItemId(item.getId());
            if (promotion != null && promotion.getStatus() == 0) {
                item.setPromotion(promotion);
            }
            return item;
        }).collect(Collectors.toList());
    }

    public Item findItemById(int id) {
        // 查商品
        Item item = itemMapper.selectByPrimaryKey(id);

        // 查库存
        ItemStock stock = itemStockMapper.selectByItemId(id);
        item.setItemStock(stock);

        // 查活动
        Promotion promotion = promotionMapper.selectByItemId(id);
        if (promotion != null && promotion.getStatus() == 0) {
            item.setPromotion(promotion);
        }

        return item;
    }

    public Item findItemInCache(int id) {
        if (id <= 0) {
            throw new BusinessException(PARAMETER_ERROR, "参数不合法！");
        }

        Item item = null;
        String key = "item:" + id;

        // guava
        item = (Item) cache.getIfPresent(key);
        if (item != null) {
            return item;
        }

        // redis
        item = (Item) redisTemplate.opsForValue().get(key);
        if (item != null) {
            cache.put(key, item);
            return item;
        }

        // mysql
        item = this.findItemById(id);
        if (item != null) {
            cache.put(key, item);
            redisTemplate.opsForValue().set(key, item, 3, TimeUnit.MINUTES);
            return item;
        }

        return null;
    }

    @Transactional
    public boolean decreaseStock(int itemId, int amount) {
        int rows = itemStockMapper.decreaseStock(itemId, amount);
        return rows > 0;
    }

    @Transactional
    public void increaseSales(int itemId, int amount) {
        itemMapper.increaseSales(itemId, amount);
    }

}
