package com.taikang.business.common.utils.redis;

import redis.clients.jedis.*;

import java.util.Map;

/**
 * Created by libin on 2016/11/23.
 */
public class RedisHash {

    private JedisCluster jedisCluster;

    private JedisPool jedisPool;

    private ShardedJedisPool shardedJedisPool;

    private RedisMode redisMode;

    private boolean clusterMode = false;

    private String prefix = "";

    public RedisHash(JedisCluster jedisCluster, RedisMode redisMode, String prefix) {
        this.jedisCluster = jedisCluster;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisHash(ShardedJedisPool shardedJedisPool, RedisMode redisMode, String prefix) {
        this.shardedJedisPool = shardedJedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisHash(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public RedisHash(JedisPool jedisPool, RedisMode redisMode, String prefix) {
        this.jedisPool = jedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public Long put(String key, String field, String value) {
        Long hset = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    hset = jedis.hset(prefix + key, field, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                hset = jedisCluster.hset(prefix + key, field, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    hset = resource.hset(prefix + key, field, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return hset;
    }

    public boolean fieldExists(String key, String field) {
        boolean hexists = false;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    hexists = jedis.hexists(prefix + key, field);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                hexists = jedisCluster.hexists(prefix + key, field);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    hexists = resource.hexists(prefix + key, field);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return hexists;
    }

    public void incr(String key, String field) {
        incrBy(prefix + key,field, 1);
    }

    public Long incrBy(String key, String field, long incr) {
        Long hincrBy = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    hincrBy = jedis.hincrBy(prefix + key, field, incr);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                hincrBy = jedisCluster.hincrBy(prefix + key, field, incr);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    hincrBy = resource.hincrBy(prefix + key, field, incr);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return hincrBy;
    }

    public Boolean putNX(String key, String field, String value) {
        Long hsetnx = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    hsetnx = jedis.hsetnx(prefix + key, field, value);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                hsetnx = jedisCluster.hsetnx(prefix + key, field, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    hsetnx = resource.hsetnx(prefix + key, field, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        if (hsetnx.equals(1)) {
            return true;
        }
        return false;
    }

    public String get(String key, String field) {
        String hget = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    hget = jedis.hget(prefix + key, field);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                hget = jedisCluster.hget(prefix + key, field);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    hget = resource.hget(prefix + key, field);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return hget;
    }

    public Long remove(String key, String... field) {
        Long hdel = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    hdel = jedis.hdel(prefix + key, field);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                hdel = jedisCluster.hdel(prefix + key, field);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    hdel = resource.hdel(prefix + key, field);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return hdel;

    }

    public long length(String key) {
        Long hlen = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    hlen = jedis.hlen(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                hlen = jedisCluster.hlen(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    hlen = resource.hlen(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        if (hlen == null) {
            hlen = 0L;
        }
        return hlen;
    }

    public Map<String, String> hGetAll(String key) {
        Map<String, String> hmap = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    hmap = jedis.hgetAll(prefix + key);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                hmap = jedisCluster.hgetAll(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    hmap = resource.hgetAll(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return hmap;
    }

}
