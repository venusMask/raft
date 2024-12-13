package org.venus.raft.core;

import org.venus.raft.election.client.AskLeaderClient;
import org.venus.raft.election.client.JoinClusterClient;
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
            System.out.println("当前节点开始接受服务器心跳...");
            // 给leader发送一个包表示自己可以正常开始接受心跳了
            requestHB();
            context.getHeartBeatService().startCountDownTimerForElection();
        }
    }

    private void requestHB() {
        JoinClusterClient client = new JoinClusterClient(context.getCurrentNode());
        client.requestHeartbeat();
    }

    /**
     * 从集群中搜索leader节点, 如果没有发现leader节点则进行开始投票进行选举
     */
    public boolean searchLeaderOrElection() {
        System.out.println("启动成功, 开始寻找leader节点, 当前节点状态: " + context.getRole());
        int lastAskPos = -1;
        while (true) {
            Tuple2<Integer, NodeIdentification> tuple2 = context.randomAsk(lastAskPos);
            // There are no available nodes in the cluster, so upgrade yourself directly to leader.
            if (tuple2 == null) {
                System.out.println("当前配置文件中已经没有可用节点,将自身晋升为leader...");
                context.switchLeader();
                break;
            } else {    // 还有可用节点则去请求leader节点的信息
                lastAskPos = tuple2.f0;
                NodeIdentification prepareToInquire = tuple2.f1;
                String host = prepareToInquire.getHost();
                Integer port = prepareToInquire.getPort();
                System.out.println("当前需要询问的信息: " + prepareToInquire);
                boolean success = askLeader(host, port);
                if(success) {
                    break;
                }
            }
        }
        return true;
    }

    private boolean askLeader(String host, Integer port) {
        AskLeaderClient client = AskLeaderClient.newInstance(host, port);
        if(client != null) {
            int leaderID = client.askLeader();
            try {
                client.shutdown();
            } catch (InterruptedException ignore) {}
            if(leaderID > 0) {
                System.out.println("leader节点的NodeID查询成功, NodeID为: " + leaderID);
                context.setLeader(leaderID);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
