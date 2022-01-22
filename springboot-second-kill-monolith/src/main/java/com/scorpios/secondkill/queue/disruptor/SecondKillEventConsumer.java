package com.scorpios.secondkill.queue.disruptor;


import com.lmax.disruptor.EventHandler;
import com.scorpios.secondkill.common.Result;
import com.scorpios.secondkill.common.SecondKillStateEnum;
import com.scorpios.secondkill.service.SecondKillService;
import com.scorpios.secondkill.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者(秒杀处理器)
 */
@Slf4j
public class SecondKillEventConsumer implements EventHandler<SecondKillEvent> {


    private SecondKillService secondKillService = (SecondKillService) SpringUtil.getBean("secondKillService");

    @Override
    public void onEvent(SecondKillEvent seckillEvent, long seq, boolean bool) {
        Result result = secondKillService.startSecondKillByAop(seckillEvent.getSeckillId(), seckillEvent.getUserId());
        if(result.equals(Result.ok(SecondKillStateEnum.SUCCESS))){
            log.info("用户:{}{}",seckillEvent.getUserId(),"秒杀成功");
        }
    }
}
