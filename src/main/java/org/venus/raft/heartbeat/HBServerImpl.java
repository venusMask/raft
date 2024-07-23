package org.venus.raft.heartbeat;

import io.grpc.stub.StreamObserver;
import org.venus.raft.core.RaftContext;
import org.venus.raft.generator.HeartBeat;
import org.venus.raft.generator.HeartBeatServiceGrpc;

/**
 * @Author venus
 * @Date 2024/7/23
 * @Version 1.0
 */
public class HBServerImpl
        extends HeartBeatServiceGrpc.HeartBeatServiceImplBase {

    private final RaftContext context;

    public HBServerImpl(RaftContext context) {
        this.context = context;
    }

    @Override
    public StreamObserver<HeartBeat.HeartBeatMessage>
    sendHeartBeat(StreamObserver<HeartBeat.HeartBeatMessage> responseObserver) {
        return new HBServerObserver(responseObserver, context);
    }

}
