package org.venus.raft.heartbeat;

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
public class RepHeartBeatServer extends GrpcService {

    private static final Logger logger = LoggerFactory.getLogger(RepHeartBeatServer.class);
    private Server server;
    private final RaftContext context;

    public RepHeartBeatServer(RaftContext context) {
        this.context = context;
    }

    private void start() throws IOException {
        int port = context.getCurrentNode().getPort();
        server = ServerBuilder.forPort(port)
                .addService(new HBServerImpl(context))
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            RepHeartBeatServer.this.stop();
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
        logger.info("启动节点Server...");
        try {
            start();
            // blockUntilShutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
