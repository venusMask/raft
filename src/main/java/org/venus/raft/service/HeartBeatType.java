package org.venus.raft.service;

/**
 * @Author venus
 * @Date 2024/7/20
 * @Version 1.0
 */
public enum HeartBeatType {

    SUBSCRIBE(1),

    ALIVE(2),

    HEARTBEAT(2);

    public final int type;

    HeartBeatType(int type) {
        this.type = type;
    }

}
