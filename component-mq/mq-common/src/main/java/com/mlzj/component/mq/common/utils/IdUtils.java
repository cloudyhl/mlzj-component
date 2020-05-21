package com.mlzj.component.mq.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author yhl
 * @date 2020/5/20
 */
@SuppressWarnings("all")
public class IdUtils {
    private static final long twepoch = 1409030641843L;
    private static final long workerIdBits = 9L;
    private static final long dataCenterIdBits = 1L;
    private static final long maxWorkerId = 511L;
    private static final long sequenceBits = 12L;
    private static final long workerIdShift = 12L;
    private static final long dataCenterIdShift = 21L;
    private static final long timestampLeftShift = 22L;
    private static final long sequenceMask = 4095L;
    private static long lastTimestamp = -1L;
    private long sequence = 0L;
    private long workerId = -1L;
    private long dataCenterId = 0L;
    private static final Logger logger = LoggerFactory.getLogger(IdUtils.class);

    public IdUtils() {
        String ipAddress;
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException var3) {
            throw new RuntimeException(var3);
        }

        String[] ipArray = ipAddress.split("\\.");
        this.dataCenterId = 1L;
        this.workerId = Long.valueOf(ipArray[3]);
        logger.warn("Init IdService by dataCenterId:{} and workerId:{}, it maybe duplicate.", this.dataCenterId, this.workerId);
    }

    public synchronized long getNextId() {
        if (this.workerId == -1L) {
            throw new IllegalStateException("id service 没有初始化完成");
        } else {
            long timestamp = this.timeGen();
            if (timestamp < lastTimestamp) {
                try {
                    throw new Exception("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
                } catch (Exception var5) {
                    logger.info("Got an exception when generate next id.", var5);
                }
            }

            if (lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & 4095L;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(lastTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            lastTimestamp = timestamp;
            long nextId = timestamp - 1409030641843L << 22 | this.dataCenterId << 21 | this.workerId << 12 | this.sequence;
            return nextId;
        }
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
            ;
        }

        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public synchronized Long nextLongId() {
        return this.getNextId();
    }

    public synchronized String nextStringId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
