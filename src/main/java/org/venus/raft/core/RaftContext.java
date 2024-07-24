package org.venus.raft.core;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.raft.args.RaftConfiguration;
import org.venus.raft.election.server.ElectionServer;
import org.venus.raft.election.client.SendHeartBeatClient;
import org.venus.raft.utils.Tuple2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class RaftContext {

    private static final Logger logger = LoggerFactory.getLogger(RaftContext.class);

    private final RaftConfiguration configuration;

    private final HeartBeatService heartBeatService;

    private final Integer currentNodeID;

    private final NodeIdentification currentNode;

    private final AtomicLong term = new AtomicLong(0L);

    private final ElectionServer grpcService;

    private NodeIdentification leader = null;

    /**
     * Node type. eg: leader, follower, candidate
     */
    private Role role = Role.FOLLOWER;

    /**
     * 集群中实际运行的节点, 此节点可能会动态的进行增加或者删除
     * 线程安全的
     */
    private final List<NodeIdentification> aliveNodes;

    /**
     * 存储所有的心跳线程信息
     */
    private final Map<Integer, ExecutorService> hbThread = new ConcurrentHashMap<>();

    private final Map<Integer, NodeIdentification> clusterMapping;

    public RaftContext(RaftConfiguration configuration) {
        this.configuration = configuration;
        this.currentNodeID = configuration.getNodeID();
        heartBeatService = new HeartBeatService(configuration);
        aliveNodes = new CopyOnWriteArrayList<>(configuration.getClusterNodes());
        clusterMapping = new HashMap<>(aliveNodes.size());
        initClusterMapping(aliveNodes);
        currentNode = clusterMapping.get(currentNodeID);
        this.grpcService = new ElectionServer(this);
    }

    public void startGrpcServer() {
        this.grpcService.startServer();
    }

    private void initClusterMapping(List<NodeIdentification> cluster) {
        for (NodeIdentification nodeIdentification : cluster) {
            clusterMapping.put(nodeIdentification.getNodeID(), nodeIdentification);
        }
    }

    /**
     * 依次向非自身节点询问leader的信息,
     *
     * @param lastAskIndex  上一次询问到的位置
     * @return  如果有存活节点则正常返回, 但是如果返回的是null说明集群中已经没有除了自己之外的可用节点,
     *          此时当前节点可以直接将自己变成leader
     */
    public Tuple2<Integer, NodeIdentification> randomAsk(int lastAskIndex) {
        for (int i = lastAskIndex + 1; i < aliveNodes.size(); i++) {
            if(!Objects.equals(aliveNodes.get(i).getNodeID(), currentNodeID)) {
                return Tuple2.of(i, aliveNodes.get(i));
            }
        }
        return null;
    }

    public void setLeader(Integer nodeID, String host, Integer port) {
        leader = new NodeIdentification(nodeID, host, port);
    }

    public void setLeader(Integer id) {
        leader = clusterMapping.get(id);
    }

    public void setLeader(NodeIdentification leader) {
        this.leader = leader;
    }

    /**
     * Determine if the number of votes exceeds half of the total votes in the cluster.
     *
     * @param votes The number of election votes held by the current node.
     */
    public boolean moreThanHalf(int votes) {
        return votes >= aliveNodes.size() / 2;
    }

    public void switchLeader() {
        this.role = Role.LEADER;
        this.leader = clusterMapping.get(currentNodeID);
    }

    public void switchRole(Role newRole) {
        this.role = newRole;
    }

    public boolean isLeader() {
        return role == Role.LEADER;
    }

    public boolean isLeader(NodeIdentification node) {
        if(leader == null || node == null) {
            return false;
        } else {
            return Objects.equals(node.getNodeID(), leader.getNodeID());
        }
    }

    /**
     * leader节点给所有的存活节点发送心跳信息
     */
    public void sendHeartBeat() {
        for (NodeIdentification node : aliveNodes) {
            if(!isLeader(node)) {
                hbThreadAdd(node);
            }
        }
    }

    private void hbThreadAdd(NodeIdentification node) {
        SendHeartBeatClient client = new SendHeartBeatClient(node, this);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        hbThread.put(node.getNodeID(), executor);
        executor.submit(client);
    }

    /**
     * 当一个节点跟leader失去连接的时候调用该方法
     * 从存活节点中删除,
     * 删除心跳线程
     * @param nodeID
     */
    public void removeClient(Integer nodeID) {
        aliveNodes.remove(clusterMapping.get(nodeID));
        ExecutorService service = hbThread.get(nodeID);
        hbThread.remove(service);
        service.shutdown();
    }

    /**
     * 一个新节点申请加入集群
     * TODO: 该方法存在问题, 当相邻的节点快速进行启动的时候, aliveNodes中可能会存在重复节点, aliveNodes改成map存储
     *
     * @param nodeID    新节点的编号
     */
    public void addNewNode(Integer nodeID) {
        NodeIdentification node = clusterMapping.get(nodeID);
        aliveNodes.add(node);
        hbThreadAdd(node);
    }

}
