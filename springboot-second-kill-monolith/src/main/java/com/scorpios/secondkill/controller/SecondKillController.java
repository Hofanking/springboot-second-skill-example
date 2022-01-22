package com.scorpios.secondkill.controller;

import com.scorpios.secondkill.common.Result;
import com.scorpios.secondkill.entity.SuccessKilled;
import com.scorpios.secondkill.queue.disruptor.DisruptorUtil;
import com.scorpios.secondkill.queue.disruptor.SecondKillEvent;
import com.scorpios.secondkill.queue.jvm.SecondKillQueue;
import com.scorpios.secondkill.service.SecondKillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RestController
@Api(tags ="秒杀示例")
@RequestMapping("/second/kill")
public class SecondKillController {

    Lock lock = new ReentrantLock(true);

//    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
//
//    // 创建线程池  调整队列数 拒绝服务
//    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
//            new LinkedBlockingQueue<>(1000));


    @Autowired
    SecondKillService secondKillService;


    @ApiOperation(value="秒杀实现方式——Lock加锁")
    @PostMapping("/start/lock")
    public Result startLock(long skgId){
        lock.lock();
        try {
            log.info("开始秒杀方式一...");
            final long userId = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;
            Result result = secondKillService.startSecondKillByLock(skgId, userId);
            if(result != null){
                log.info("用户:{}--{}", userId, result.get("msg"));
            }else{
                log.info("用户:{}--{}", userId, "哎呦喂，人也太多了，请稍后！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return Result.ok();
    }


    @ApiOperation(value="秒杀实现方式二——Aop加锁")
    @PostMapping("/start/aop")
    public Result startAop(long skgId){
        try {
            log.info("开始秒杀方式二...");
            final long userId = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;
            Result result = secondKillService.startSecondKillByAop(skgId, userId);
            if(result != null){
                log.info("用户:{}--{}", userId, result.get("msg"));
            }else{
                log.info("用户:{}--{}", userId, "哎呦喂，人也太多了，请稍后！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }


    @ApiOperation(value="秒杀实现方式三——悲观锁")
    @PostMapping("/start/pes/lock/one")
    public Result startPesLockOne(long skgId){
        try {
            log.info("开始秒杀方式三...");
            final long userId = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;
            Result result = secondKillService.startSecondKillByUpdate(skgId, userId);
            if(result != null){
                log.info("用户:{}--{}", userId, result.get("msg"));
            }else{
                log.info("用户:{}--{}", userId, "哎呦喂，人也太多了，请稍后！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }


    @ApiOperation(value="秒杀实现方式四——悲观锁")
    @PostMapping("/start/pes/lock/two")
    public Result startPesLockTwo(long skgId){
        try {
            log.info("开始秒杀方式四...");
            final long userId = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;
            Result result = secondKillService.startSecondKillByUpdateTwo(skgId, userId);
            if(result != null){
                log.info("用户:{}--{}", userId, result.get("msg"));
            }else{
                log.info("用户:{}--{}", userId, "哎呦喂，人也太多了，请稍后！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value="秒杀实现方式五——乐观锁")
    @PostMapping("/start/opt/lock")
    public Result startOptLock(long skgId){
        try {
            log.info("开始秒杀方式五...");
            final long userId = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;
            Result result = secondKillService.startSecondKillByPesLock(skgId, userId,1);
            if(result != null){
                log.info("用户:{}--{}", userId, result.get("msg"));
            }else{
                log.info("用户:{}--{}", userId, "哎呦喂，人也太多了，请稍后！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value="秒杀实现方式六——消息队列")
    @PostMapping("/start/queue")
    public Result startQueue(long skgId){
        try {
            log.info("开始秒杀方式六...");
            final long userId = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;
            SuccessKilled kill = new SuccessKilled();
            kill.setSeckillId(skgId);
            kill.setUserId(userId);
            Boolean flag = SecondKillQueue.getSkillQueue().produce(kill);
            // 虽然进入了队列，但是不一定能秒杀成功 进队出队有时间间隙
            if(flag){
                log.info("用户:{}{}",kill.getUserId(),"秒杀成功");
            }else{
                log.info("用户:{}{}",userId,"秒杀失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value="秒杀实现方式七——Disruptor队列")
    @PostMapping("/start/disruptor")
    public Result startDisruptor(long skgId){
        try {
            log.info("开始秒杀方式七...");
            final long userId = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;
            SecondKillEvent kill = new SecondKillEvent();
            kill.setSeckillId(skgId);
            kill.setUserId(userId);
            DisruptorUtil.producer(kill);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

}
