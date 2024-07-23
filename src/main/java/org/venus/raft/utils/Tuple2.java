package org.venus.raft.utils;

/**
 * @Author venus
 * @Date 2024/7/20
 * @Version 1.0
 */
public class Tuple2<f0, f1> {

    public final f0 f0;

    public final f1 f1;

    public Tuple2(f0 f0, f1 f1) {
        this.f0 = f0;
        this.f1 = f1;
    }

    public static <f0, f1> Tuple2<f0, f1> of(f0 f0, f1 f1) {
        return new Tuple2<>(f0, f1);
    }

}
