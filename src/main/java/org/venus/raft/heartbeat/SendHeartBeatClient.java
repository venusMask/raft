package org.venus.raft.heartbeat;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import org.venus.raft.core.RaftContext;
import org.venus.raft.generator.HeartBeat;
import org.venus.raft.generator.HeartBeatServiceGrpc;
import org.venus.raft.service.HeartBeatType;

import java.util.concurrent.TimeUnit;

/**
 * The client that sends heartbeats to the specified client(Host, Port).
 *
 * @Author venus
 * @Date 2024/7/22
 * @Version 1.0
 */
@Getter
public class SendHeartBeatClient {

    private final ManagedChannel channel;

    private final String host;

    private final Integer port;

    private final HeartBeatServiceGrpc.HeartBeatServiceStub asyncStub;

    private final RaftContext context;

    private SendHeartBeatClient(String host, Integer port, RaftContext context) {
        this.host = host;
        this.port = port;
        this.context = context;
        // Channels are secure by default (via SSL/TLS).
        // For the example we disable TLS to avoid needing certificates.
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.asyncStub = HeartBeatServiceGrpc.newStub(channel);
    }

    public static SendHeartBeatClient newInstance(String host, Integer port, RaftContext context) {
        return new SendHeartBeatClient(host, port, context);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void sendHeartBeat() {
        try {
            HBClientObserver observerObj = new HBClientObserver(context, channel);
            StreamObserver<HeartBeat.HeartBeatMessage> observer = null;
            try {
                observer = asyncStub.sendHeartBeat(observerObj);
            } catch (Exception e) {
                System.out.println("hhhh");
            }
            // StreamObserver<HeartBeat.HeartBeatMessage> requestObserver = asyncStub.sendHeartBeat(observer);
            // Send heartbeat packet
            while (true) {
                HeartBeat.HeartBeatMessage message = HeartBeat.HeartBeatMessage
                        .newBuilder()
                        .setType(HeartBeatType.HEARTBEAT.type)
                        .setTerm(context.getTerm().get())
                        .setMessage("Message from leader")
                        .build();
                System.out.println("Prepare to send message...");
                try {
                    observer.onNext(message);
                } catch (Exception e) {
                    System.out.println("Throw exception");
                }
                try {
                    Thread.sleep(context.getConfiguration().getHeartBeatGap());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            System.out.println("Host: " + host + ", port: " + port + ". 无法连接成功!!!");
        }
    }

}
