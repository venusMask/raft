package org.venus.raft.election.server;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.venus.raft.core.NodeIdentification;
import org.venus.raft.core.RaftContext;
import org.venus.raft.election.generator.Election;
import org.venus.raft.error.ExceptionUtil;
import org.venus.raft.utils.LogUtil;

/**
 * Leader processes heartbeat packet information from Follower.
 */
public class HeartBeatLeaderObserver
        implements StreamObserver<Election.HeartBeatMessage> {

    private final RaftContext context;
    private final ManagedChannel channel;
    private final NodeIdentification node;

    public HeartBeatLeaderObserver(RaftContext context,
                                   ManagedChannel channel,
                                   NodeIdentification node) {
        this.context = context;
        this.channel = channel;
        this.node = node;
    }

    @Override
    public void onNext(Election.HeartBeatMessage message) {
        // TODO follower节点发送过来的信息需要校验其余节点是否还依然存活
        LogUtil.console(message, "Follower");
    }

    @Override
    public void onError(Throwable throwable) {
        context.removeClient(node.getNodeID());
        ExceptionUtil.dealWith(throwable);
        shutdown();
    }

    @Override
    public void onCompleted() {
        shutdown();
    }

    private void shutdown() {
        channel.shutdown();
    }

}
