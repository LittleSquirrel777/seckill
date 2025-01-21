package com.example.seckill.service.impl;

import com.example.seckill.common.ErrorCode;
import com.example.seckill.component.ObjectValidator;
import com.example.seckill.dao.ItemMapper;
import com.example.seckill.dao.ItemStockMapper;
import com.example.seckill.dao.PromotionMapper;
import com.example.seckill.entity.Item;
import com.example.seckill.entity.ItemStock;
import com.example.seckill.entity.Promotion;
import com.example.seckill.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
