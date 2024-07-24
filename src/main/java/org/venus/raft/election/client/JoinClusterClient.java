package org.venus.raft.election.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.raft.core.NodeIdentification;
import org.venus.raft.election.generator.Election;
import org.venus.raft.election.generator.ElectionServiceGrpc;

import java.util.concurrent.TimeUnit;

/**
 * 向leader发送一个packet表示自己可以开始接受心跳了
 *
 * @Author venus
 * @Date 2024/7/22
 * @Version 1.0
 */
@Getter
public class JoinClusterClient {

    private static final Logger logger = LoggerFactory.getLogger(JoinClusterClient.class);

    private final ManagedChannel channel;

    private final ElectionServiceGrpc.ElectionServiceBlockingStub blockingStub;

    private final NodeIdentification node;

    public JoinClusterClient(NodeIdentification node) {
        this.node = node;
        this.channel = ManagedChannelBuilder.forAddress(node.getHost(), node.getPort()).usePlaintext().build();
        this.blockingStub = ElectionServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void requestHeartbeat() {
        Election.JoinRequest request = Election.JoinRequest
                .newBuilder()
                .setNodeId(node.getNodeID())
                .build();
        blockingStub.joinCluster(request);
        // TODO Deal with exception.
    }

}
