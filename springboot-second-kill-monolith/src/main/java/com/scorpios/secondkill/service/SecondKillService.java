package com.scorpios.secondkill.service;

import com.scorpios.secondkill.common.Result;

public interface SecondKillService {


    /**
     * 删除秒杀售卖商品记录
     * @param skgId  商品id
     */
    void deleteSecondKillGoodsId(long skgId);

    /**
     * 查询秒杀售卖商品
     * @param skgId 商品id
     * @return
     */
    Integer getSecondKillCount(long skgId);

    /**
     * 开始秒杀 - 使用Lock
     * @param skgId  商品id
     * @param userId    用户id
     * @return
     */
    Result startSecondKillByLock(long skgId, long userId);


    /**
     * 开始秒杀 - 使用Aop
     * @param skgId  商品id
     * @param userId    用户id
     * @return
     */
    Result startSecondKillByAop(long skgId, long userId);

    /**
     * 开始秒杀 - 数据库悲观锁
     * @param skgId  商品id
     * @param userId    用户id
     * @return
     */
    Result startSecondKillByUpdate(long skgId, long userId);


    /**
     * 开始秒杀 - 数据库悲观锁
     * @param skgId  商品id
     * @param userId    用户id
     * @return
     */
    Result startSecondKillByUpdateTwo(long skgId, long userId);


    /**
     * 开始秒杀 - 数据库乐观锁
     * @param skgId
     * @param userId
     * @return
     */
    Result startSecondKillByPesLock(long skgId, long userId,int number);
}
