package org.venus.raft;

import org.venus.raft.args.RaftArgument;

/**
 * Hello world!
 */
public class Raft {

    public static void main(String[] args) {
        RaftArgument raftArgument = new RaftArgument();
        raftArgument.parse(args);
    }

}
