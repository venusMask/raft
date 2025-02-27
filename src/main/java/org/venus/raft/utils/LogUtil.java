package org.venus.raft.utils;

import org.slf4j.Logger;
import org.venus.raft.election.generator.Election;

/**
 * @Author venus
 * @Date 2024/7/23
 * @Version 1.0
 */
public class LogUtil {

    public static void console(Election.HeartBeatMessage message, String role) {
        System.out.printf("***** Message From %s Begin *****\n", role);
        System.out.println(message.getMessage());
        System.out.println(message.getTerm());
        System.out.println(message.getTerm());
        System.out.printf("***** Message From %s END ***** \n", role);
    }

    public static void info(Logger logger,
                            Election.HeartBeatMessage message,
                            String role) {
        logger.info("***** Message From {} Begin *****", role);
        logger.info(message.getMessage());
        logger.info(String.valueOf(message.getTerm()));
        logger.info(String.valueOf(message.getType()));
        logger.info("***** Message From {} END *****", role);
    }


}
