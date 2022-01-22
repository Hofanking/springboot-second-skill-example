package com.scorpios.secondkill.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@ToString
@Data
@TableName("success_killed")
public class SuccessKilled implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId("seckill_id")
    private Long seckillId;
    private Long userId;
    private Short state;
    private Timestamp createTime;

}

