package org.venus.raft.args;

import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.raft.core.NodeIdentification;
import org.venus.raft.utils.RaftStringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * All program parameters of Raft program
 */
@ToString
public class RaftConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RaftConfiguration.class);

    private RaftConfiguration() {}

    @Getter
    private Long minElectionTimeout = 1500L;

    @Getter
    private Long maxElectionTimeout = 3000L;

    @Getter
    private Long heartBeatGap = 100L;

    @Getter
    private Integer nodeID = 1;

    private String cluster;

    @Getter
    private List<NodeIdentification> clusterNodes;

    //////////////////////////////////////
    //
    //  Set configuration from property
    //
    ////////////////////////////////////
    public void setMinElectionTimeout(Properties properties) {
        this.minElectionTimeout = Long.parseLong(
                properties.getOrDefault("min_election_timeout", 150L).toString()
        );
    }

    public void setCluster(Properties properties) {
        String clusterString = properties.getProperty("cluster");
        if(RaftStringUtil.isNullOrEmpty(clusterString)) {
            throw new NullPointerException("Cluster must not be null !");
        }
        String[] clusters = clusterString.split(",");
        if(clusters.length % 2 == 0) {
            throw new RuntimeException("Cluster nodes must be odd numbers !");
        }
        clusterNodes = new ArrayList<>(clusters.length);
        for (String nodeInfo : clusters) {
            String[] strings = nodeInfo.split(":");
            if(strings.length != 3) {
                throw new RuntimeException("Cluster param error !");
            }
            int nodeId = Integer.parseInt(strings[0]);
            int port = Integer.parseInt(strings[2]);
            clusterNodes.add(new NodeIdentification(nodeId, strings[1], port));
        }
        this.cluster = clusterString;
    }

    public void setMaxElectionTimeout(Properties properties) {
        this.minElectionTimeout = Long.parseLong(
                properties.getOrDefault("max_election_timeout", 300L).toString()
        );
    }

    public void setNodeID(Properties properties) {
        this.nodeID = Integer.parseInt(
                properties.getOrDefault("node_id", 1).toString()
        );
    }

    public static RaftConfiguration parseFromProperty(Properties property) {
        RaftConfiguration raftConfiguration = new RaftConfiguration();
        raftConfiguration.setMaxElectionTimeout(property);
        raftConfiguration.setMinElectionTimeout(property);
         raftConfiguration.setNodeID(property);
        raftConfiguration.setCluster(property);
        logger.info("Raft Configuration: {}", raftConfiguration);
        return raftConfiguration;
    }
}
