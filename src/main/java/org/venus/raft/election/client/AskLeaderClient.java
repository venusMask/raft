package org.venus.raft.election.client;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.raft.election.generator.Election;
import org.venus.raft.election.generator.ElectionServiceGrpc;

import java.util.concurrent.TimeUnit;

@Getter
public class AskLeaderClient {

    private static final Logger logger = LoggerFactory.getLogger(AskLeaderClient.class);

    private final ManagedChannel channel;

    private final ElectionServiceGrpc.ElectionServiceBlockingStub blockingStub;

    private final String host;

    private final int port;

    public AskLeaderClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = ElectionServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public int askLeader() {
        int leaderId = -1;
        try {
            Election.LeaderMessage leaderResponse = blockingStub.askLeader(Empty.newBuilder().build());
            leaderId = leaderResponse.getLeaderId();
        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
        return leaderId;
    }

    public static AskLeaderClient newInstance(String host, int port) {
        try {
            return new AskLeaderClient(host, port);
        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
        return null;
    }

}
