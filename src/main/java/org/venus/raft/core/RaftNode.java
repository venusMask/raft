package org.venus.raft.core;

import org.venus.raft.heartbeat.AskLeaderClient;
import org.venus.raft.utils.Tuple2;

/**
 * 当前服务器的节点角色
 */
public class RaftNode {

    /**
     * Node context.
     */
    private final RaftContext context;

    public RaftNode(RaftContext context) {
        this.context = context;
    }

    /**
     * 调用该方法表示一个节点真正开始启动了, 它需要进行如下动作:
     * 1: 启动所有的GRPC Server.
     * 2: 寻找leader节点,
     * 1: 如果找到leader节点,则开始同步日志.
     * 2: 如果没有找到leader节点则开始发起选举
     */
    public void startNode() {
        context.startGrpcServer();
        searchLeaderOrElection();
        mayBeSendHeartbeat();
    }

    /**
     * 如果当前节点是leader节点则开始向各个节点发送心跳包,
     * 如果该节点不是leader则开始准备接受心跳
     */
    public void mayBeSendHeartbeat() {
        if(context.isLeader()) {
            context.sendHeartBeat();
        } else {
            context.getHeartBeatService().startCountDownTimerForElection();
        }
    }

    /**
     * 从集群中搜索leader节点, 如果没有发现leader节点则进行开始投票进行选举
     */
    public boolean searchLeaderOrElection() {
        int lastAskPos = -1;
        while (true) {
            Tuple2<Integer, NodeIdentification> tuple2 = context.randomAsk(lastAskPos);
            // There are no available nodes in the cluster, so upgrade yourself directly to leader.
            if (tuple2 == null) {
                context.switchLeader();
                break;
            } else {    // 还有可用节点则去请求leader节点的信息
                lastAskPos = tuple2.f0;
                NodeIdentification prepareToInquire = tuple2.f1;
                String host = prepareToInquire.getHost();
                Integer port = prepareToInquire.getPort();
                AskLeaderClient client = AskLeaderClient.newInstance(host, port);
                if(client != null) {
                    try {
                        int leaderID = client.askLeader();
                        context.setLeader(leaderID);
                    } catch (Exception e) {
                        // 节点无法连接成功, 开始尝试连接下一个节点
                        continue;
                    } finally {
                        try {
                            client.shutdown();
                        } catch (InterruptedException e) {
                            // IGNORE
                        }
                    }
                    break;
                }
            }
        }
        return true;
    }

}
