package com.example.seckill.service;

import com.example.seckill.entity.Item;

import java.util.List;

public interface ItemService {

    List<Item> findItemsOnPromotion();

    Item findItemById(int id);

    boolean decreaseStock(int itemId, int amount);

    void increaseSales(int itemId, int amount);

}
