package com.bwton.agg.common.util.id;

/**
 * @Author:wangjunjie
 * @Describleption:
 * @Date: Created in 10:13  2018/9/4
 * @Modified By:
 */

import java.util.concurrent.ConcurrentHashMap;


/**
 * 获取唯一id，最后生成18位整型
 * <p>
 * 长度为64bit,从高位到低位依次为
 * </p>
 * <p>
 * <pre>
 * 1bit   符号位
 * 41bits 时间偏移量从2016年1月1日零点到现在的毫秒数
 * 10bits 工作进程Id
 * 12bits 同一个毫秒内的自增量
 * </pre>
 *
 * @author Saintcy
 */
public class SnowflakeIdWorker implements IdWorker {
    private static SnowflakeIdWorker myWorker;
    private final long workerId;
    /**
     * 基准时间
     * 2016/1/1
     */
    private long twepoch = 1451577600000L;
    private long sequence = 0L;
    private long workerIdBits = 10L;
    private long maxWorkerId = -1L ^ -1L << workerIdBits;
    private long sequenceBits = 12L;
    private long workerIdShift = sequenceBits;
    private long timestampLeftShift = sequenceBits + workerIdBits;
    private long sequenceMask = -1L ^ -1L << sequenceBits;
    private long lastTimestamp = -1L;
    private boolean is16digit = false;
    /**
     * ========================================================
     * =                      按名字取id                       =
     * ========================================================
     */
    private ConcurrentHashMap<String, SnowflakeIdWorker> idWorkers = new ConcurrentHashMap<String, SnowflakeIdWorker>();

    /**
     * @param workerId
     */
    public SnowflakeIdWorker(final long workerId) {
        this(workerId, false);
    }

    /**
     * @param workerId  每个应用在不同的机器必须不同
     * @param is16digit 是否16位数
     */
    public SnowflakeIdWorker(final long workerId, final boolean is16digit) {
        super();
        //如果要生成16位的
        if (is16digit) {
            this.is16digit = true;
            //机器数最多只能是32台
            workerIdBits = 5L;
            //同一个毫秒内的自增量最多1024
            sequenceBits = 10L;
            maxWorkerId = -1L ^ -1L << workerIdBits;
            workerIdShift = sequenceBits;
            timestampLeftShift = sequenceBits + workerIdBits;
            sequenceMask = -1L ^ -1L << sequenceBits;
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        this.workerId = workerId;
    }

    /**
     * 用于单机版快捷获取id，分布式请手动创建SnowflakeIdWorker对象，并赋予workerId，每个应用在不同的机器必须不同
     *
     * @return
     */
    public static SnowflakeIdWorker getInstance() {
        if (myWorker == null) {
            myWorker = new SnowflakeIdWorker(1);
        }
        return myWorker;
    }

    @Override
    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (sequence + 1) & sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                        this.lastTimestamp - timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.lastTimestamp = timestamp;
        long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << workerIdShift)
                | (this.sequence);
        //System.out.println("timestamp:" + timestamp + ",timestampLeftShift:" + timestampLeftShift + ",nextId:" + nextId + ",workerId:"
        //	+ workerId + ",sequence:" + sequence);
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 本方法与不带参数的nextId不能混用，本方法是建立多个idWorker实例，每个实例之间用名字区分，
     * 单个实例的id保证唯一，不同的实例会生成相同的id，所以不能在同一个业务中使用不同的名称获取id
     *
     * @param name worker名称
     */
    @Override
    public long nextId(String name) {
        //根据workerId生成多个相同workerId的实例
        SnowflakeIdWorker idWorker = idWorkers.get(name);
        if (idWorker == null) {
            //如果还未生成实例，则创建一个，注意这里需要同步实例
            synchronized (this) {
                idWorker = idWorkers.get(name);
                if (idWorker == null) {
                    //新生成一个实例
                    idWorker = new SnowflakeIdWorker(this.workerId, this.is16digit);
                    idWorkers.put(name, idWorker);
                }
            }
        }

        return idWorker.nextId();
    }

    //public static void main(String[] args) {
    //    //final SnowflakeIdWorker worker2 = new SnowflakeIdWorker(10, true);
    //    ///*for (int i = 0; i < 10000; i++) {
    //    //    System.out.println(worker2.nextId());
    //    //}*/
    //    //System.out.println(new Date(116, 0, 1).getTime());
    //    //System.out.println(new Date(1451577600000L).toLocaleString());
    //    ////System.out.println(worker2.nextId());
    //    ////System.out.println(new BigInteger("0111111111111111111111111111111111111111111111111111111111111111", 2).toString(10));
    //    //
    //    //ExecutorService executorService = Executors.newCachedThreadPool();
    //    //for (int i = 0; i < 100; i++) {
    //    //    executorService.submit(new Runnable() {
    //    //        @Override
    //    //        public void run() {
    //    //            System.out.println(worker2.nextId("a"));
    //    //            System.out.println(worker2.nextId("b"));
    //    //        }
    //    //    });
    //    //}
    //    //executorService.shutdown();
    //
    //    int i=0;
    //    SnowflakeIdWorker worker = new SnowflakeIdWorker(1);
    //    while (i++ < 10) {
    //        System.out.println(worker.nextId());
    //    }
    //}
}

