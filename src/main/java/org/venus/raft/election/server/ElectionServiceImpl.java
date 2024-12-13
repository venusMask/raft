package org.venus.raft.election.server;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.venus.raft.core.RaftContext;
import org.venus.raft.election.generator.Election;
import org.venus.raft.election.generator.ElectionServiceGrpc;
import org.venus.raft.election.client.HeartBeatFollowerObserver;

public class ElectionServiceImpl
    extends ElectionServiceGrpc.ElectionServiceImplBase {

    private final RaftContext context;

    public ElectionServiceImpl(RaftContext context) {
        this.context = context;
    }

    @Override
    public StreamObserver<Election.HeartBeatMessage>
    sendHeartBeat(StreamObserver<Election.HeartBeatMessage> responseObserver) {
        return new HeartBeatFollowerObserver(responseObserver, context);
    }

    @Override
    public void askLeader(Empty request,
                          StreamObserver<Election.LeaderMessage> responseObserver) {
        Election.LeaderMessage response = Election.LeaderMessage
                .newBuilder()
                .setLeaderId(context.getLeader().getNodeID())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void joinCluster(Election.JoinRequest request,
                            StreamObserver<Empty> responseObserver) {
        int nodeId = request.getNodeId();
        context.addNewNode(nodeId);
        Empty response = Empty.getDefaultInstance();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
