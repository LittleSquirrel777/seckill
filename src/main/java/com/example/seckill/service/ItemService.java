package com.example.seckill.service;

import com.example.seckill.entity.Item;
import com.example.seckill.entity.ItemStockLog;

import java.util.List;

public interface ItemService {

    List<Item> findItemsOnPromotion();

    Item findItemById(int id);

    Item findItemInCache(int id);

    boolean decreaseStock(int itemId, int amount);

    void increaseSales(int itemId, int amount);

    boolean decreaseStockInCache(int itemId, int amount);

    boolean increaseStockInCache(int itemId, int amount);

    ItemStockLog createItemStockLog(int itemId, int amount);

    void updateItemStockLogStatus(String id, int status);

    ItemStockLog findItemStorkLogById(String id);

}
