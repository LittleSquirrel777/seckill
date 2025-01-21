package com.example.seckill.controller;

import com.example.seckill.common.ResponseModel;
import com.example.seckill.entity.Item;
import com.example.seckill.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/item")
@CrossOrigin(origins = "${example.web.path}", allowedHeaders = "*", allowCredentials = "true")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel getItemList() {
        List<Item> items = itemService.findItemsOnPromotion();
        return new ResponseModel(items);
    }

    @RequestMapping(path = "/detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel getItemDetail(@PathVariable("id") int id) {
        Item item = itemService.findItemById(id);
        return new ResponseModel(item);
    }

}
