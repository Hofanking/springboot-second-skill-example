package com.scorpios.secondkill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scorpios.secondkill.aop.ServiceLock;
import com.scorpios.secondkill.common.Result;
import com.scorpios.secondkill.common.SecondKillStateEnum;
import com.scorpios.secondkill.entity.Payment;
import com.scorpios.secondkill.entity.SecondKill;
import com.scorpios.secondkill.entity.SuccessKilled;
import com.scorpios.secondkill.exception.ScorpiosException;
import com.scorpios.secondkill.mapper.PaymentMapper;
import com.scorpios.secondkill.mapper.SecondKillMapper;
import com.scorpios.secondkill.mapper.SuccessKilledMapper;
import com.scorpios.secondkill.service.SecondKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service("secondKillService")
public class SecondKillServiceImpl implements SecondKillService {

    Lock lock = new ReentrantLock(true);

    @Autowired
    SuccessKilledMapper successKilledMapper;

    @Autowired
    SecondKillMapper secondKillMapper;

    @Autowired
    PaymentMapper paymentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSecondKillGoodsId(long skgId) {
        successKilledMapper.deleteById(skgId);
        SecondKill secondKill = new SecondKill();
        secondKill.setSeckillId(skgId);
        secondKill.setNumber(1000);
        secondKillMapper.updateById(secondKill);
    }

    @Override
    public Integer getSecondKillCount(long skgId) {
        QueryWrapper<SuccessKilled> wrapper = new QueryWrapper<>();
        wrapper.eq("seckill_id",skgId);
        Integer count = successKilledMapper.selectCount(wrapper);
        return count;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result startSecondKillByLock(long skgId, long userId) {
        /**
         * 1.脏读：事务A读取了事务B更新的数据，然后B回滚操作，那么A读取到的数据是脏数据
         * 2.不可重复读：事务 A 多次读取同一数据，事务 B 在事务A多次读取的过程中，对数据作了更新并提交，导致事务A多次读取同一数据时，结果 不一致。
         * 3.幻读：系统管理员A将数据库中所有学生的成绩从具体分数改为ABCDE等级，但是系统管理员B就在这个时候插入了一条具体分数的记录，当系统管理员A改结束后发现还有一条记录没有改过来，就好像发生了幻觉一样，这就叫幻读。
         *
         * 事务隔离级别	                脏读	    不可重复读	幻读
         * 读未提交（read-uncommitted）	是	        是	     是
         * 不可重复读（read-committed）	否	        是	     是
         * 可重复读（repeatable-read）	否	        否	     是
         * 串行化（serializable）	        否	        否	     否
         *
         * 1)这里、不清楚为啥、总是会被超卖101、难道锁不起作用、lock是同一个对象
         * 2)分析一下，事务未提交之前，锁已经释放(事务提交是在整个方法执行完)，导致另一个事物读取到了这个事物未提交的数据，也就是传说中的脏读，
         *      但数据库默认的事务隔离级别为 可重复读(repeatable-read)，也就不可能出现脏读
         * 3)给自己留个坑思考：为什么分布式锁(zk和redis)没有问题？(事实是有问题的，由于redis释放锁需要远程通信，不那么明显而已)
         */
//        lock.lock();
        try {
            // 校验库存
            SecondKill secondKill = secondKillMapper.selectById(skgId);
            Integer number = secondKill.getNumber();
            if (number > 0) {
                //扣库存
                secondKill.setNumber(number - 1);
                secondKillMapper.updateById(secondKill);
                //创建订单
                SuccessKilled killed = new SuccessKilled();
                killed.setSeckillId(skgId);
                killed.setUserId(userId);
                killed.setState((short) 0);
                killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
                successKilledMapper.insert(killed);

                //支付
                Payment payment = new Payment();
                payment.setSeckillId(skgId);
                payment.setSeckillId(skgId);
                payment.setUserId(userId);
                payment.setMoney(40);
                payment.setState((short) 1);
                payment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                paymentMapper.insert(payment);
            } else {
                return Result.error(SecondKillStateEnum.END);
            }
        } catch (Exception e) {
//            throw new ScorpiosException("异常了个乖乖");
        } finally {
//            lock.unlock();
        }
        return Result.ok(SecondKillStateEnum.SUCCESS);
    }


    @Override
    @ServiceLock // 使用Aop进行加锁
    @Transactional(rollbackFor = Exception.class)
    public Result startSecondKillByAop(long skgId, long userId) {

        try {
            // 校验库存
            SecondKill secondKill = secondKillMapper.selectById(skgId);
            Integer number = secondKill.getNumber();
            if (number > 0) {
                //扣库存
                secondKill.setNumber(number - 1);
                secondKillMapper.updateById(secondKill);
                //创建订单
                SuccessKilled killed = new SuccessKilled();
                killed.setSeckillId(skgId);
                killed.setUserId(userId);
                killed.setState((short) 0);
                killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
                successKilledMapper.insert(killed);

                //支付
                Payment payment = new Payment();
                payment.setSeckillId(skgId);
                payment.setSeckillId(skgId);
                payment.setUserId(userId);
                payment.setMoney(40);
                payment.setState((short) 1);
                payment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                paymentMapper.insert(payment);
            } else {
                return Result.error(SecondKillStateEnum.END);
            }
        } catch (Exception e) {
//            throw new ScorpiosException("异常了个乖乖");
        }
        return Result.ok(SecondKillStateEnum.SUCCESS);
    }


    /**
     * 使用for update一定要加上这个事务，当事务处理完后，for update才会将行级锁解除
     * 如果请求数和秒杀商品数量一致，会出现少卖
     * @param skgId  商品id
     * @param userId    用户id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result startSecondKillByUpdate(long skgId, long userId) {
        try {
            // 校验库存-悲观锁
            SecondKill secondKill = secondKillMapper.querySecondKillForUpdate(skgId);
            Integer number = secondKill.getNumber();
            if (number > 0) {
                //扣库存
                secondKill.setNumber(number - 1);
                secondKillMapper.updateById(secondKill);
                //创建订单
                SuccessKilled killed = new SuccessKilled();
                killed.setSeckillId(skgId);
                killed.setUserId(userId);
                killed.setState((short) 0);
                killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
                successKilledMapper.insert(killed);

                //支付
                Payment payment = new Payment();
                payment.setSeckillId(skgId);
                payment.setSeckillId(skgId);
                payment.setUserId(userId);
                payment.setMoney(40);
                payment.setState((short) 1);
                payment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                paymentMapper.insert(payment);
            } else {
                return Result.error(SecondKillStateEnum.END);
            }
        } catch (Exception e) {
            throw new ScorpiosException("异常了个乖乖");
        } finally {
        }
        return Result.ok(SecondKillStateEnum.SUCCESS);
    }


    /**
     * UPDATE锁表
     * @param skgId  商品id
     * @param userId    用户id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result startSecondKillByUpdateTwo(long skgId, long userId) {
        try {

            // 不校验，直接扣库存更新
            int result = secondKillMapper.updateSecondKillById(skgId);
            if (result > 0) {
                //创建订单
                SuccessKilled killed = new SuccessKilled();
                killed.setSeckillId(skgId);
                killed.setUserId(userId);
                killed.setState((short) 0);
                killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
                successKilledMapper.insert(killed);

                //支付
                Payment payment = new Payment();
                payment.setSeckillId(skgId);
                payment.setSeckillId(skgId);
                payment.setUserId(userId);
                payment.setMoney(40);
                payment.setState((short) 1);
                payment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                paymentMapper.insert(payment);
            } else {
                return Result.error(SecondKillStateEnum.END);
            }
        } catch (Exception e) {
            throw new ScorpiosException("异常了个乖乖");
        } finally {
        }
        return Result.ok(SecondKillStateEnum.SUCCESS);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result startSecondKillByPesLock(long skgId, long userId, int number) {

        /**
         * 乐观锁，不进行库存数量的校验，直接
         * 这里使用的乐观锁、可以自定义抢购数量、如果配置的抢购人数比较少、比如120:100(人数:商品) 会出现少买的情况
         * 用户同时进入会出现更新失败的情况，出现少卖
         */
        try {
            SecondKill kill = secondKillMapper.selectById(skgId);
            // 剩余的数量应该要大于等于秒杀的数量
            if(kill.getNumber() >= number) {
                int result = secondKillMapper.updateSecondKillByVersion(number,skgId,kill.getVersion());
                if (result > 0) {
                    //创建订单
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(skgId);
                    killed.setUserId(userId);
                    killed.setState((short) 0);
                    killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    successKilledMapper.insert(killed);

                    //支付
                    Payment payment = new Payment();
                    payment.setSeckillId(skgId);
                    payment.setSeckillId(skgId);
                    payment.setUserId(userId);
                    payment.setMoney(40);
                    payment.setState((short) 1);
                    payment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    paymentMapper.insert(payment);
                } else {
                    return Result.error(SecondKillStateEnum.END);
                }
            }
        } catch (Exception e) {
            throw new ScorpiosException("异常了个乖乖");
        } finally {
        }
        return Result.ok(SecondKillStateEnum.SUCCESS);
    }
}
