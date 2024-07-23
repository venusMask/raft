package org.venus.raft.heartbeat;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import org.venus.raft.generator.HeartBeat;
import org.venus.raft.generator.HeartBeatServiceGrpc;

import java.util.concurrent.TimeUnit;

/**
 * @Author venus
 * @Date 2024/7/22
 * @Version 1.0
 */
@Getter
public class AskLeaderClient {

    private final ManagedChannel channel;

    private final HeartBeatServiceGrpc.HeartBeatServiceBlockingStub blockingStub;

    private final String host;

    private final int port;

    public AskLeaderClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        this.blockingStub = HeartBeatServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public int askLeader() {
        HeartBeat.LeaderResponse leaderResponse = blockingStub.askLeader(Empty.newBuilder().build());
        return leaderResponse.getLeaderId();
    }

    public static AskLeaderClient newInstance(String host, int port) {
        try {
            AskLeaderClient client = new AskLeaderClient(host, port);
            return client;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
