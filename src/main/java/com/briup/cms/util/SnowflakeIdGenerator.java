package com.briup.cms.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SnowflakeIdGenerator {
    // 机器ID所占的位数
    private static final long WORKER_ID_BITS = 5L;
    // 数据中心ID所占的位数
    private static final long DATACENTER_ID_BITS = 5L;
    // 支持的最大机器ID，结果是31
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // 支持的最大数据中心ID，结果是31
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    // 序列在ID中占的位数
    private static final long SEQUENCE_BITS = 12L;
    // 机器ID向左移12位
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    // 数据中心ID向左移17位
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间戳向左移22位
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    // 生成序列的掩码，这里为4095
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // 机器ID（0~31）
    private static long workerId;
    // 数据中心ID（0~31）
    private static long datacenterId;
    // 毫秒内序列（0~4095）
    private static long sequence = 0L;
    // 上次生成ID的时间戳
    private static long lastTimestamp = -1L;

    // ==============================Constructors=====================================
    public SnowflakeIdGenerator(@Value("${snowflake.workerId}") long workerId,
                                @Value("${snowflake.datacenterId}") long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        SnowflakeIdGenerator.workerId = workerId;
        SnowflakeIdGenerator.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================
    public static synchronized long generateId() {
        long timestamp = System.currentTimeMillis();

        // 如果当前时间小于上一次生成ID的时间戳，说明系统时钟回退过
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock is moving backwards. Rejecting requests until %d.", lastTimestamp));
        }

        // 如果是同一时间生成的，则序列加1
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 序列溢出，等待下一个毫秒
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 如果是不同时间生成的，则序列重置为0
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 移位并通过或运算组合生成ID
        return ((timestamp << TIMESTAMP_SHIFT)) | (datacenterId << DATACENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;
    }

    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
