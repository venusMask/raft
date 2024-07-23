package org.venus.raft.heartbeat;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.venus.raft.core.RaftContext;
import org.venus.raft.generator.HeartBeat;
import org.venus.raft.service.HeartBeatType;

/**
 * follower处理来自leader的心跳包
 *
 * @Author venus
 * @Date 2024/7/23
 * @Version 1.0
 */
@AllArgsConstructor
public class HBServerObserver
        implements StreamObserver<HeartBeat.HeartBeatMessage> {

    private final StreamObserver<HeartBeat.HeartBeatMessage> responseObserver;

    private final RaftContext context;

    @Override
    public void onNext(HeartBeat.HeartBeatMessage message) {
        int messageType = message.getType();
        if(messageType == HeartBeatType.HEARTBEAT.type) {
            context.getHeartBeatService().resetCountDownTimer();
            LogUtil.console(message, "Leader");
            HeartBeat.HeartBeatMessage responseMessage = HeartBeat.HeartBeatMessage
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
