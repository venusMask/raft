package org.venus.raft.args;


import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class RaftArgument {

    private final static Logger logger = LoggerFactory.getLogger(RaftArgument.class);

    public void parse(String[] args) {
        logger.info("The parameters received by Application are {}", Arrays.toString(args));
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            throw new RuntimeException(e);
        }
    }

}
