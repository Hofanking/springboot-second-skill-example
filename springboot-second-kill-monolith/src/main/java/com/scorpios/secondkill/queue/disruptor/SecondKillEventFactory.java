package com.scorpios.secondkill.queue.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 事件生成工厂（用来初始化预分配事件对象）
 */
public class SecondKillEventFactory implements EventFactory<SecondKillEvent> {

    @Override
    public SecondKillEvent newInstance() {
        return new SecondKillEvent();
    }
}