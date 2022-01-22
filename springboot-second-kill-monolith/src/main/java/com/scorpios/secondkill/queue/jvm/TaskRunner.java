package com.scorpios.secondkill.queue.jvm;


import com.scorpios.secondkill.common.Result;
import com.scorpios.secondkill.common.SecondKillStateEnum;
import com.scorpios.secondkill.entity.SuccessKilled;
import com.scorpios.secondkill.service.SecondKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 消费秒杀队列
 */
@Slf4j
@Component
public class TaskRunner implements ApplicationRunner{

    @Autowired
    private SecondKillService seckillService;

    @Override
    public void run(ApplicationArguments var){
        new Thread(() -> {
            log.info("队列启动成功");
            while(true){
                try {
                    // 进程内队列
                    SuccessKilled kill = SecondKillQueue.getSkillQueue().consume();
                    if(kill != null){
                        Result result = seckillService.startSecondKillByAop(kill.getSeckillId(), kill.getUserId());
//                        Result result = seckillService.startSecondKillByLock(kill.getSeckillId(), kill.getUserId());
                        if(result != null && result.equals(Result.ok(SecondKillStateEnum.SUCCESS))){
                            log.info("TaskRunner,result:{}",result);
                            log.info("TaskRunner从消息队列取出用户，用户:{}{}",kill.getUserId(),"秒杀成功");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
