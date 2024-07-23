package org.venus.raft.args;


import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class RaftArgument {

    private final static Logger logger = LoggerFactory.getLogger(RaftArgument.class);

    @Option(usage = "配置文件路径", name = "-config")
    public String configFilePath = "/Users/dzh/software/java/projects/raft/config/config.properties";

    public void parse(String[] args) {
        logger.info("The parameters received by Application are {}", Arrays.toString(args));
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            throw new RuntimeException(e);
        }
    }

    public RaftConfiguration parseConfig() {
        Properties properties = new Properties();
        logger.info("Starting to parse the configuration file, the configuration file path is: {}", configFilePath);
        try (FileInputStream fileInputStream = new FileInputStream(configFilePath)) {
            properties.load(fileInputStream);
            return RaftConfiguration.parseFromProperty(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
