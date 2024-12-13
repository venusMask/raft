package org.venus.raft.election.generator;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.4.0)",
    comments = "Source: HeartBeat.proto")
public final class ElectionServiceGrpc {

  private ElectionServiceGrpc() {}

  public static final String SERVICE_NAME = "ElectionService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<Election.HeartBeatMessage,
      Election.HeartBeatMessage> METHOD_SEND_HEART_BEAT =
      io.grpc.MethodDescriptor.<Election.HeartBeatMessage, Election.HeartBeatMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "ElectionService", "sendHeartBeat"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              Election.HeartBeatMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              Election.HeartBeatMessage.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      Election.LeaderMessage> METHOD_ASK_LEADER =
      io.grpc.MethodDescriptor.<com.google.protobuf.Empty, Election.LeaderMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "ElectionService", "askLeader"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.google.protobuf.Empty.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              Election.LeaderMessage.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<Election.JoinRequest,
      com.google.protobuf.Empty> METHOD_JOIN_CLUSTER =
      io.grpc.MethodDescriptor.<Election.JoinRequest, com.google.protobuf.Empty>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "ElectionService", "joinCluster"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              Election.JoinRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.google.protobuf.Empty.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ElectionServiceStub newStub(io.grpc.Channel channel) {
    return new ElectionServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ElectionServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ElectionServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ElectionServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ElectionServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ElectionServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<Election.HeartBeatMessage> sendHeartBeat(
        io.grpc.stub.StreamObserver<Election.HeartBeatMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_SEND_HEART_BEAT, responseObserver);
    }

    /**
     */
    public void askLeader(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<Election.LeaderMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ASK_LEADER, responseObserver);
    }

    /**
     */
    public void joinCluster(Election.JoinRequest request,
                            io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_JOIN_CLUSTER, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_SEND_HEART_BEAT,
            asyncBidiStreamingCall(
              new MethodHandlers<
                Election.HeartBeatMessage,
                Election.HeartBeatMessage>(
                  this, METHODID_SEND_HEART_BEAT)))
          .addMethod(
            METHOD_ASK_LEADER,
            asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                Election.LeaderMessage>(
                  this, METHODID_ASK_LEADER)))
          .addMethod(
            METHOD_JOIN_CLUSTER,
            asyncUnaryCall(
              new MethodHandlers<
                Election.JoinRequest,
                com.google.protobuf.Empty>(
                  this, METHODID_JOIN_CLUSTER)))
          .build();
    }
  }

  /**
   */
  public static final class ElectionServiceStub extends io.grpc.stub.AbstractStub<ElectionServiceStub> {
    private ElectionServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<Election.HeartBeatMessage> sendHeartBeat(
        io.grpc.stub.StreamObserver<Election.HeartBeatMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_SEND_HEART_BEAT, getCallOptions()), responseObserver);
    }

    /**
     */
    public void askLeader(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<Election.LeaderMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ASK_LEADER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void joinCluster(Election.JoinRequest request,
                            io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_JOIN_CLUSTER, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ElectionServiceBlockingStub extends io.grpc.stub.AbstractStub<ElectionServiceBlockingStub> {
    private ElectionServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public Election.LeaderMessage askLeader(com.google.protobuf.Empty request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ASK_LEADER, getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty joinCluster(Election.JoinRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_JOIN_CLUSTER, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ElectionServiceFutureStub extends io.grpc.stub.AbstractStub<ElectionServiceFutureStub> {
    private ElectionServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Election.LeaderMessage> askLeader(
        com.google.protobuf.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ASK_LEADER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> joinCluster(
        Election.JoinRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_JOIN_CLUSTER, getCallOptions()), request);
    }
  }

  private static final int METHODID_ASK_LEADER = 0;
  private static final int METHODID_JOIN_CLUSTER = 1;
  private static final int METHODID_SEND_HEART_BEAT = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ElectionServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ElectionServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ASK_LEADER:
          serviceImpl.askLeader((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<Election.LeaderMessage>) responseObserver);
          break;
        case METHODID_JOIN_CLUSTER:
          serviceImpl.joinCluster((Election.JoinRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_HEART_BEAT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sendHeartBeat(
              (io.grpc.stub.StreamObserver<Election.HeartBeatMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class ElectionServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return Election.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ElectionServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ElectionServiceDescriptorSupplier())
              .addMethod(METHOD_SEND_HEART_BEAT)
              .addMethod(METHOD_ASK_LEADER)
              .addMethod(METHOD_JOIN_CLUSTER)
              .build();
        }
      }
    }
    return result;
  }
}
