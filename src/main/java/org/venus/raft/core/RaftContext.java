package org.venus.raft.core;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.raft.args.RaftConfiguration;
import org.venus.raft.heartbeat.RepHeartBeatServer;
import org.venus.raft.heartbeat.SendHeartBeatClient;
import org.venus.raft.utils.Tuple2;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class RaftContext {

    private static final Logger logger = LoggerFactory.getLogger(RaftContext.class);

    private final RaftConfiguration configuration;

    private final HeartBeatService heartBeatService;

    private final Integer currentNodeID;

    private final NodeIdentification currentNode;

    private final AtomicLong term = new AtomicLong(0L);

    private final RepHeartBeatServer grpcService;

    private NodeIdentification leader = null;

    /**
     * Node type. eg: leader, follower, candidate
     */
    private Role role = Role.FOLLOWER;

    /**
     * 集群中实际运行的节点, 此节点可能会动态的进行增加或者删除
     */
    private final List<NodeIdentification> clusterNodes;

    private final Map<Integer, NodeIdentification> clusterMapping;

    public RaftContext(RaftConfiguration configuration) {
        this.configuration = configuration;
        this.currentNodeID = configuration.getNodeID();
        heartBeatService = new HeartBeatService(configuration);
        clusterNodes = configuration.getClusterNodes();
        clusterMapping = new HashMap<>(clusterNodes.size());
        initClusterMapping(clusterNodes);
        currentNode = clusterMapping.get(currentNodeID);
        this.grpcService = new RepHeartBeatServer(this);
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
        for (int i = lastAskIndex + 1; i < clusterNodes.size(); i++) {
            if(!Objects.equals(clusterNodes.get(i).getNodeID(), currentNodeID)) {
                return Tuple2.of(i, clusterNodes.get(i));
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
        return votes >= clusterNodes.size() / 2;
    }

    public void switchLeader() {
        logger.info("将自身节点切换为leader...");
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

    public void sendHeartBeat() {
        ArrayList<SendHeartBeatClient> clients = new ArrayList<>();
        for (NodeIdentification node : clusterNodes) {
            if(!isLeader(node)) {
                String host = node.getHost();
                Integer port = node.getPort();
                try {
                    clients.add(SendHeartBeatClient.newInstance(host, port, this));
                } catch (Exception e) {
                    System.out.println("bbbb");
                }
            }
        }
        for (SendHeartBeatClient client : clients) {
            client.sendHeartBeat();
        }
    }

}
