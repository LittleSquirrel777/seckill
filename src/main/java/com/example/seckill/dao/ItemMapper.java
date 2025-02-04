package com.example.seckill.dao;

import com.example.seckill.entity.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    int insert(Item record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    Item selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    List<Item> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    int updateByPrimaryKey(Item record);

    /**
     * 增加销量
     *
     * @param id
     * @param amount
     * @return
     */
    int increaseSales(Integer id, Integer amount);

    /**
     * 查询正在进行秒杀活动的商品
     *
     * @return
     */
    List<Item> selectOnPromotion();
}