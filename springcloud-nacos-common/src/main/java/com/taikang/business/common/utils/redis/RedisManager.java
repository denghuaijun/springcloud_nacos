package com.taikang.business.common.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @ Author     ：libin
 * @ Date       ：Created in 16:01 2018/11/17
 * @ Description：
 * @ Modified By：
 * @Version:
 */
public class RedisManager {

    private static final Logger log = LoggerFactory.getLogger(RedisManager.class);

    private JedisPool jedisPool;

    private JedisCluster jedisCluster;

    private ShardedJedisPool shardedJedisPool;

    private RedisMode redisMode;


    private String prefix;

    public RedisManager(JedisPool jedisPool,String prefix) {
        this.jedisPool = jedisPool;
        this.prefix =prefix;
        redisMode = RedisMode.POOLMODE;
    }

    public RedisManager(JedisCluster jedisCluster,String prefix) {
        this.jedisCluster = jedisCluster;
        this.prefix =prefix;
        redisMode = RedisMode.CLUSTERMODE;
    }

    public RedisManager(ShardedJedisPool shardedJedisPool,String prefix) {
        this.shardedJedisPool = shardedJedisPool;
        this.prefix =prefix;
        redisMode = RedisMode.SHARDEDMODE;
    }

    public RedisSet redisSetWapper() {
        log.debug("use method redisSetWapper");
        RedisSet redisSet = null;
        switch (redisMode) {
            case POOLMODE:
                redisSet = new RedisSet(jedisPool,redisMode,prefix);
                break;
            case CLUSTERMODE:
                redisSet = new RedisSet(jedisCluster,redisMode,prefix);
                break;
            case SHARDEDMODE:
                redisSet = new RedisSet(shardedJedisPool,redisMode,prefix);
                break;
        }
        return redisSet;
    }

    public RedisHash redisHashWapper() {
        log.debug("use method redisHashWapper");
        RedisHash redisHash = null;
        switch (redisMode) {
            case POOLMODE:
                redisHash = new RedisHash(jedisPool,redisMode,prefix);
                break;
            case CLUSTERMODE:
                redisHash = new RedisHash(jedisCluster,redisMode,prefix);
                break;
            case SHARDEDMODE:
                redisHash = new RedisHash(shardedJedisPool,redisMode,prefix);
                break;
        }
        return redisHash;
    }

    public RedisArray redisArrayWapper() {
        log.debug("use method  redisArrayWapper");
        RedisArray redisArray = null;
        switch (redisMode) {
            case POOLMODE:
                redisArray = new RedisArray(jedisPool,redisMode,prefix);
                break;
            case CLUSTERMODE:
                redisArray = new RedisArray(jedisCluster,redisMode,prefix);
                break;
            case SHARDEDMODE:
                redisArray = new RedisArray(shardedJedisPool,redisMode,prefix);
                break;
        }

        return redisArray;
    }

    public RedisSortedset redisSortedsetWapper() {
        log.debug("use method redisSortedsetWapper ");
        RedisSortedset redisSortedset = null;
        switch (redisMode) {
            case POOLMODE:
                redisSortedset = new RedisSortedset(jedisPool,redisMode,prefix);
                break;
            case CLUSTERMODE:
                redisSortedset = new RedisSortedset(jedisCluster,redisMode,prefix);
                break;
            case SHARDEDMODE:
                redisSortedset = new RedisSortedset(shardedJedisPool,redisMode,prefix);
                break;
        }

        return redisSortedset;
    }

    public RedisString redisStringWapper() {
        log.debug("use method redisStringWapper ");
        RedisString redisString = null;
        switch (redisMode) {
            case POOLMODE:
                redisString = new RedisString(jedisPool,redisMode,prefix);
                break;
            case CLUSTERMODE:
                redisString = new RedisString(jedisCluster,redisMode,prefix);
                break;
            case SHARDEDMODE:
                redisString = new RedisString(shardedJedisPool,redisMode,prefix);
                break;
        }
        return redisString;
    }
    public RedisKey redisKeyWapper() {
        log.debug("use method redisStringWapper ");
        RedisKey redisKey = null;
        switch (redisMode) {
            case POOLMODE:
                redisKey = new RedisKey(jedisPool,redisMode,prefix);
                break;
            case CLUSTERMODE:
                redisKey = new RedisKey(jedisCluster,redisMode,prefix);
                break;
            case SHARDEDMODE:
                redisKey = new RedisKey(shardedJedisPool,redisMode,prefix);
                break;
        }
        return redisKey;
    }



    /**
     * 主从或者单例使用
     * @return
     */
    public static Builder Builder() {
        return new Builder();
    }

    public static final class Builder {
        /**
         * 资源池中最大连接数 8
         */
        private int maxTotal = 50;

        /**
         *资源池允许最大空闲的连接数 8
         */
        private int maxIdle = 45;

        /**
         *源池确保最少空闲的连接数 0
         */
        private int minIdle = 30;

        /**
         *当资源池用尽后，调用者是否要等待。只有当为true时，下面的maxWaitMillis才会生效 true
         */
        private boolean blockWhenExhausted = true;

        /**
         * 当资源池连接用尽后，调用者的最大等待时间(单位为毫秒) -1：表示永不超时
         */
        private int maxWaitMillis = 2000;


        /**
         * 向资源池借用连接时是否做连接有效性检测(ping)，无效连接会被移除 false
         */
        private boolean testOnBorrow = false;

        /**
         * 向资源池归还连接时是否做连接有效性检测(ping)，无效连接会被移除 false
         */
        private boolean testOnReturn = false;


        /**
         * 是否开启空闲资源监测 false
         */
        private boolean testWhileIdle = true;

        /**
         * 空闲资源的检测周期(单位为毫秒) -1：不检测
         */
        private int timeBetweenEvictionRunsMillis = 1000 * 60 * 15;

        /**
         * 资源池中资源最小空闲时间(单位为毫秒)，达到此值后空闲资源将被移除 1000 60 30 = 30分钟
         */
        private int minEvictableIdleTimeMillis = 1000 * 60 * 15;

        /**
         * 做空闲资源检测时，每次的采样数 3  -1全部资源检测
         */
        private int numTestsPerEvictionRun = -1;

        private String hostAndPort;

        /**
         * 设置前缀
         */
        private String prefix;

        public Builder maxTotal(int maxIdle) {
            this.maxTotal = maxIdle;
            return this;
        }

        public Builder maxIdle (int maxIdle) {
            this.maxIdle = maxIdle;
            return this;
        }
        public Builder minIdle (int minIdle) {
            this.minIdle = minIdle;
            return this;
        }
        public Builder blockWhenExhausted (boolean blockWhenExhausted) {
            this.blockWhenExhausted = blockWhenExhausted;
            return this;
        }

        public Builder maxWaitMillis (int maxWaitMillis) {
            this.maxWaitMillis = maxWaitMillis;
            return this;
        }

        public Builder testOnBorrow (boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
            return this;
        }
        public Builder testOnReturn (boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
            return this;
        }
        public Builder testWhileIdle (boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
            return this;
        }
        public Builder timeBetweenEvictionRunsMillis (int timeBetweenEvictionRunsMillis) {
            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
            return this;
        }
        public Builder minEvictableIdleTimeMillis (int minEvictableIdleTimeMillis) {
            this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
            return this;
        }
        public Builder numTestsPerEvictionRun (int numTestsPerEvictionRun) {
            this.numTestsPerEvictionRun = numTestsPerEvictionRun;
            return this;
        }

        public Builder hostAndPort (String hostAndPort) {
            this.hostAndPort = hostAndPort;
            return this;
        }

        public Builder prefix(String prefix){
            this.prefix = prefix;
            return this;
        }

        public RedisManager poolBuild() {
            String host = null;
            Integer port = null;
            if (hostAndPort == null) {
                throw new IllegalArgumentException("hostAndPort 配置错误");
            }
            String[] split = hostAndPort.split(",");
            if (split.length > 0) {
                String[] split1 = split[0].split(":");
                host = split1[0];
                port = Integer.parseInt(split1[1]);
            }
            if (host == null && port == null) {
                throw new IllegalArgumentException("HOST和PORT 配置错误");
            }
            JedisPool jedisPool = new JedisPool(jedisPoolConfig(), host,port);
            RedisManager redisManager = new RedisManager(jedisPool,prefix);
            return redisManager;
        }

        public RedisManager clusterBuild() {
            Set<HostAndPort> hostAndPortsSet = new LinkedHashSet<HostAndPort>();
            String[] split = hostAndPort.split(",");
            for (String ip : split) {
                String[] split1 = ip.split(":");
                HostAndPort hostAndPort = new HostAndPort(split1[0], Integer.parseInt(split1[1]));
                hostAndPortsSet.add(hostAndPort);
            }
            log.info("init JedisCluster ...");
            JedisCluster jedisPool = new JedisCluster(hostAndPortsSet, jedisPoolConfig());
            RedisManager redisManager = new RedisManager(jedisPool,prefix);
            return redisManager;
        }

        public RedisManager shardedPoolBuild() {
            List<JedisShardInfo> hostAndPortsSet = new ArrayList<>();
            String[] split = hostAndPort.split(",");
            for (String ip : split) {
                String[] split1 = ip.split(":");
                JedisShardInfo shardInfo = new JedisShardInfo(split1[0],Integer.parseInt(split1[1]));
                hostAndPortsSet.add(shardInfo);
            }
            log.info("init shardedPoolBuild ...");
            ShardedJedisPool shardedJedisPool = new ShardedJedisPool(jedisPoolConfig(), hostAndPortsSet);
            RedisManager redisManager = new RedisManager(shardedJedisPool,prefix);
            return redisManager;
        }

        private JedisPoolConfig jedisPoolConfig() {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(maxTotal);
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMinIdle(minIdle);
            jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
            jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
            jedisPoolConfig.setTestOnBorrow(testOnBorrow);
            jedisPoolConfig.setTestOnReturn(testOnReturn);
            jedisPoolConfig.setTestWhileIdle(testWhileIdle);
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
            return jedisPoolConfig;
        }

    }

}

enum RedisMode {
    CLUSTERMODE, POOLMODE, SHARDEDMODE
}

