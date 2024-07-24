package org.venus.raft.election.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import org.venus.raft.core.NodeIdentification;
import org.venus.raft.core.RaftContext;
import org.venus.raft.election.generator.Election;
import org.venus.raft.election.generator.ElectionServiceGrpc;
import org.venus.raft.election.server.HeartBeatLeaderObserver;
import org.venus.raft.service.HeartBeatType;

import java.util.concurrent.TimeUnit;

/**
 * The leader that sends heartbeats to the specified follower.
 */
@Getter
public class SendHeartBeatClient
        implements Runnable {

    private final ManagedChannel channel;

    private final NodeIdentification node;

    private final ElectionServiceGrpc.ElectionServiceStub asyncStub;

    private final RaftContext context;

    public SendHeartBeatClient(NodeIdentification node, RaftContext context) {
        this.node = node;
        this.context = context;
        this.channel = ManagedChannelBuilder.forAddress(node.getHost(), node.getPort()).usePlaintext().build();
        this.asyncStub = ElectionServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void sendHeartBeat() {
        // Deal with response from follower.
        HeartBeatLeaderObserver observerObj = new HeartBeatLeaderObserver(context, channel, node);
        StreamObserver<Election.HeartBeatMessage> observer = asyncStub.sendHeartBeat(observerObj);
        while (true) {
            Election.HeartBeatMessage message = Election.HeartBeatMessage
                    .newBuilder()
                    .setType(HeartBeatType.HEARTBEAT.type)
                    .setTerm(context.getTerm().get())
                    .setMessage("Message from leader")
                    .build();
            observer.onNext(message);
            try {
                Thread.sleep(context.getConfiguration().getHeartBeatGap());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        sendHeartBeat();
    }

}
