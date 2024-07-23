package org.venus.raft.heartbeat;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.venus.raft.core.RaftContext;
import org.venus.raft.generator.HeartBeat;

/**
 * The logic for processing server return messages.
 *
 * @Author venus
 * @Date 2024/7/22
 * @Version 1.0
 */
public class HBClientObserver
        implements StreamObserver<HeartBeat.HeartBeatMessage> {

    private final RaftContext context;
    private final ManagedChannel channel;

    public HBClientObserver(RaftContext context, ManagedChannel channel) {
        this.context = context;
        this.channel = channel;
    }

    @Override
    public void onNext(HeartBeat.HeartBeatMessage message) {
        // TODO follower节点发送过来的信息需要校验其余节点是否还依然存活
        System.out.println("***** Message From Follower/Candidate Begin *****");
        System.out.println(message.getMessage());
        System.out.println(message.getTerm());
        System.out.println(message.getTerm());
        System.out.println("***** Message From Follower/Candidate END *****");
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println("HeartBeat Client Close...");
        channel.shutdown();
    }

}
