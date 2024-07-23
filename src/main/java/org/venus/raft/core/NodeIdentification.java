package org.venus.raft.core;

import lombok.Getter;

import java.net.InetSocketAddress;

/**
 * Identity proof for each node in the cluster.
 *
 * @Author venus
 * @Date 2024/7/20
 * @Version 1.0
 */
@Getter
public class NodeIdentification {

    /**
     * 一个节点的唯一标识
     */
    private Integer nodeID;

    private InetSocketAddress nodeAddress;

    private String host;

    private Integer port;

    public NodeIdentification(Integer nodeID, String host, Integer port) {
        this(host, port);
        this.nodeID = nodeID;
    }

    public NodeIdentification(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.nodeAddress = new InetSocketAddress(host, port);
    }

    public void setNodeAddress(String host, Integer port) {
        this.nodeAddress = new InetSocketAddress(host, port);
    }

    public void setHost(String host) {
        this.host = host;
        setNodeAddress(host, port);
    }

    public void setPort(Integer port) {
        this.port = port;
        setNodeAddress(host, port);
    }

    @Override
    public String toString() {
        return "Cluster Node Info: {" +
                "nodeID=" + nodeID +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
