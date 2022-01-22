package com.scorpios.secondkill.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
@Scope
@Aspect
@Order(1) //order越小越是最先执行，但更重要的是最先执行的最后结束
public class LockAspect {
    /**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
     */
    private static  Lock lock = new ReentrantLock(true); // 互斥锁 参数默认false，不公平锁

    // Service层切点     用于记录错误日志
    @Pointcut("@annotation(com.scorpios.secondkill.aop.ServiceLock)")
    public void lockAspect() {

    }

    @Around("lockAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        lock.lock();
        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
//            throw new RuntimeException();
        } finally{
            lock.unlock();
        }
        return obj;
    }
}
