package org.venus.raft.election.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.raft.core.RaftContext;
import org.venus.raft.service.GrpcService;

import java.io.IOException;

/**
 * GRPC Server
 */
public class ElectionServer extends GrpcService {

    private static final Logger logger = LoggerFactory.getLogger(ElectionServer.class);
    private Server server;
    private final RaftContext context;

    public ElectionServer(RaftContext context) {
        this.context = context;
    }

    private void start() throws IOException {
        int port = context.getCurrentNode().getPort();
        server = ServerBuilder.forPort(port)
                .addService(new ElectionServiceImpl(context))
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        System.out.println("启动成功...");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            ElectionServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * TODO:
     * @throws InterruptedException
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    @Override
    public void startServer() {
        System.out.println("准备启动Server节点: 当前节点信息: " + context.getCurrentNode().toString());
        try {
            start();
            // TODO: 此处需要在某个地方进行停止, 但是此API现在还没有特别熟悉
            // blockUntilShutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
