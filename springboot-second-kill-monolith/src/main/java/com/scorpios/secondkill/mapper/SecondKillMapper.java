package com.scorpios.secondkill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scorpios.secondkill.entity.SecondKill;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondKillMapper extends BaseMapper<SecondKill> {

    /**
     * 将此行数据进行加锁，当整个方法将事务提交后，才会解锁
     * @param skgId
     * @return
     */
    @Select(value = "SELECT * FROM seckill WHERE seckill_id=#{skgId} FOR UPDATE")
    SecondKill querySecondKillForUpdate(@Param("skgId") Long skgId);

    @Update(value = "UPDATE seckill SET number=number-1 WHERE seckill_id=#{skgId} AND number > 0")
    int updateSecondKillById(@Param("skgId") long skgId);

    @Update(value = "UPDATE seckill  SET number=number-#{number},version=version+1 WHERE seckill_id=#{skgId} AND version = #{version}")
    int updateSecondKillByVersion(@Param("number") int number, @Param("skgId") long skgId, @Param("version")int version);
}
