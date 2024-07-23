package org.venus.raft.service;

/**
 * @Author venus
 * @Date 2024/7/22
 * @Version 1.0
 */
public abstract class GrpcService {

    /**
     * 启动一个GRPC的Server服务
     */
    public abstract void startServer();

}
