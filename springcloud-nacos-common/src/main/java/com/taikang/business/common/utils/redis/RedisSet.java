package com.taikang.business.common.utils.redis;

import redis.clients.jedis.*;

import java.util.List;
import java.util.Set;

/**
 * Created by libin on 2016/12/2.
 */
public class RedisSet {

    private JedisCluster jedisCluster;

    private JedisPool jedisPool;

    private ShardedJedisPool shardedJedisPool;

    private RedisMode redisMode;

    private boolean clusterMode = false;

    private String prefix = "";

    public RedisSet(JedisCluster jedisCluster, RedisMode redisMode, String prefix) {
        this.jedisCluster = jedisCluster;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisSet(ShardedJedisPool shardedJedisPool, RedisMode redisMode, String prefix) {
        this.shardedJedisPool = shardedJedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisSet(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public RedisSet(JedisPool jedisPool, RedisMode redisMode, String prefix) {
        this.jedisPool = jedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public Long add(String key , String... value) {
        Long sadd = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    sadd = jedis.sadd(prefix + key, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                sadd = jedisCluster.sadd(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    sadd = resource.sadd(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return sadd;
    }

    public Long size(String key) {
        Long scard = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    scard = jedis.scard(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                scard = jedisCluster.scard(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    scard = resource.scard(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return scard;
    }


    public boolean valueExist(String key, String value) {
        Boolean sismember = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    sismember = jedis.sismember(prefix + key,value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                sismember = jedisCluster.sismember(prefix + key,value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    sismember = resource.sismember(prefix + key,value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return sismember;
    }


    public Set<String> getAll(String key) {
        Set<String> smembers = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    smembers = jedis.smembers(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                smembers = jedisCluster.smembers(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    smembers = resource.smembers(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return smembers;

    }

    /**
     * 随机移除一个数据并返回
     *
     * @return
     */
    public String randomGetAndRemove(String key) {
        String spop = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    spop = jedis.spop(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                spop = jedisCluster.spop(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    spop = resource.spop(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return spop;
    }

    /**
     * 随机查找
     *
     * @param count count 正数 返回多少个随机数  count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
     * @return
     */
    public List<String> randomGet(String key , int count) {
        List<String> srandmember = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    srandmember = jedis.srandmember(prefix + key,count);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                srandmember = jedisCluster.srandmember(prefix + key,count);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    srandmember = resource.srandmember(prefix + key,count);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return srandmember;
    }


    public Long remove(String key, String... value) {
        Long srem = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    srem = jedis.srem(prefix + key,value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                srem = jedisCluster.srem(prefix + key,value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    srem = resource.srem(prefix + key,value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return srem;
    }

    public ScanResult<String> get(String key, String corsor) {
        ScanResult<String> sscan = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    sscan = jedis.sscan(prefix + key,corsor);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                sscan = jedisCluster.sscan(prefix + key,corsor);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    sscan = resource.sscan(prefix + key,corsor);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return sscan;
    }

}
