package com.scorpios.secondkill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@ToString
@Data
@TableName("seckill")
public class SecondKill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "seckill_id", type = IdType.AUTO)
    private Long seckillId;
    private String name;
    private Integer number;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createTime;
    @Version
    private Integer version;

}

