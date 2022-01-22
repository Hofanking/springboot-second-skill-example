package com.scorpios.secondkill.queue.disruptor;


import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ThreadFactory;


public class DisruptorUtil {

    static Disruptor<SecondKillEvent> disruptor;

    static{
        SecondKillEventFactory factory = new SecondKillEventFactory();
        int ringBufferSize = 1024;
        ThreadFactory threadFactory = runnable -> new Thread(runnable);
        disruptor = new Disruptor<>(factory, ringBufferSize, threadFactory);
        disruptor.handleEventsWith(new SecondKillEventConsumer());
        disruptor.start();
    }

    public static void producer(SecondKillEvent kill){
        RingBuffer<SecondKillEvent> ringBuffer = disruptor.getRingBuffer();
        SecondKillEventProducer producer = new SecondKillEventProducer(ringBuffer);
        producer.secondKill(kill.getSeckillId(),kill.getUserId());
    }
}
