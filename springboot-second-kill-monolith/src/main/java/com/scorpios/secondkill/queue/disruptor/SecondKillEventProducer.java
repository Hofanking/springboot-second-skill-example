package com.scorpios.secondkill.queue.disruptor;

import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;

/**
 * 使用translator方式生产者
 */
public class SecondKillEventProducer {

    private final static EventTranslatorVararg<SecondKillEvent> translator = (seckillEvent, seq, objs) -> {
        seckillEvent.setSeckillId((Long) objs[0]);
        seckillEvent.setUserId((Long) objs[1]);
    };

    private final RingBuffer<SecondKillEvent> ringBuffer;

    public SecondKillEventProducer(RingBuffer<SecondKillEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void secondKill(long seckillId, long userId){
        this.ringBuffer.publishEvent(translator, seckillId, userId);
    }
}
