package org.venus.raft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.raft.args.RaftArgument;
import org.venus.raft.args.RaftConfiguration;
import org.venus.raft.core.RaftContext;
import org.venus.raft.core.RaftNode;

/**
 * Hello Raft!
 */
public class Raft {

    private final static Logger logger = LoggerFactory.getLogger(Raft.class);

    public static void main(String[] args) {
        RaftArgument raftArgument = new RaftArgument();
        raftArgument.parse(args);
        RaftConfiguration raftConfiguration = raftArgument.parseConfig();
        RaftContext context = new RaftContext(raftConfiguration);
        RaftNode raftNode = new RaftNode(context);
        raftNode.startNode();
    }

}
