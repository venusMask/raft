package org.venus.raft.election.client;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.venus.raft.core.RaftContext;
import org.venus.raft.election.generator.Election;
import org.venus.raft.service.HeartBeatType;
import org.venus.raft.utils.LogUtil;

/**
 * Follower handles heartbeat packets from leader.
 */
@AllArgsConstructor
public class HeartBeatFollowerObserver
        implements StreamObserver<Election.HeartBeatMessage> {

    private final StreamObserver<Election.HeartBeatMessage> responseObserver;

    private final RaftContext context;

    @Override
    public void onNext(Election.HeartBeatMessage message) {
        int messageType = message.getType();
        System.out.println("message type: " + messageType);
        if(messageType == HeartBeatType.HEARTBEAT.type) {
            context.getHeartBeatService().resetCountDownTimer();
            LogUtil.console(message, "Leader");
            Election.HeartBeatMessage responseMessage = Election.HeartBeatMessage
                    .newBuilder()
                    .setType(HeartBeatType.ALIVE.type)
                    .setTerm(-1)
                    .setMessage("Response from follower")
                    .build();
            responseObserver.onNext(responseMessage);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {
        responseObserver.onCompleted();
    }

}
