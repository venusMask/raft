package org.venus.raft.error;

import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author venus
 * @Date 2024/7/24
 * @Version 1.0
 */
public class ExceptionUtil {

    public static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    private static void mark(StackTraceElement[] traceElements) {
        for (StackTraceElement element: traceElements) {
            logger.error(element.toString());
        }
    }

    public static void dealWith(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        mark(stackTrace);
        if(throwable instanceof StatusRuntimeException) {
            StatusRuntimeException statusException = (StatusRuntimeException) throwable;
            if (statusException.getStatus().getCode() == io.grpc.Status.Code.UNAVAILABLE) {
                logger.error("Connection refused: {}", statusException.getStatus().getDescription());
                assert statusException.getStatus().getCause() != null;
                logger.error("{}", statusException.getStatus().getCause().toString());
            } else {
                logger.error("GRPC error: {}", statusException.getStatus().getDescription());
                assert statusException.getStatus().getCause() != null;
                logger.error("{}", statusException.getStatus().getCause().toString());            }
        }
    }


}
