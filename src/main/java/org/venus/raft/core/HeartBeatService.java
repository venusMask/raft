package org.venus.raft.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.raft.args.RaftArgument;
import org.venus.raft.args.RaftConfiguration;

import java.util.concurrent.*;

/**
 * Raft的心跳服务
 */
public class HeartBeatService {

    private final static Logger logger = LoggerFactory.getLogger(HeartBeatService.class);

    private final ScheduledExecutorService executorService;

    private final RaftConfiguration configuration;

    private ScheduledFuture<?> scheduledFuture;

    public HeartBeatService(RaftConfiguration configuration) {
        this.configuration = configuration;
        executorService = Executors.newScheduledThreadPool(1);
    }

    /**
     * 开启一个等待选举的计时器, 意味着如果该定时器的时间到了则进行选举
     * 在一个节点刚启动的时候它是follower状态,
     * 1. 此时它应该开启一个等待心跳包的计时器(electionTimer), 如果该定时器结束还没有收到心跳包则需要开始进行选举,
     * 2. 如果收到了心跳包则需要进行重置electionTimer
     */
    public void startCountDownTimerForElection() {
        Long minElectionTimeout = configuration.getMinElectionTimeout();
        Long maxElectionTimeout = configuration.getMaxElectionTimeout();
        // 随机生成一个选举超时时间, 随机生成的目的是为了防止多个节点同时超时开始进行选举
        long electionTimeout = ThreadLocalRandom.current().nextLong(minElectionTimeout, maxElectionTimeout + 1);
        logger.info("新的心跳等待时间: {}ms", electionTimeout);
        scheduledFuture = executorService.schedule(this::handleElectionTimeout, electionTimeout, TimeUnit.MILLISECONDS);
    }

    private void handleElectionTimeout() {
        becomeCandidate();
    }

    public void resetCountDownTimer() {
        logger.info("取消前一个倒计时器，开启新的倒计时器...");
        if(scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        startCountDownTimerForElection();
    }

    private void becomeCandidate() {
        // Logic to transition from follower to candidate
        // This could involve incrementing the term, requesting votes, etc.
        System.out.println("Becoming a candidate...");
    }

    public void shutdown() {
        executorService.shutdown();
    }

}
