package com.taikang.business.common.utils.redis;

import redis.clients.jedis.*;

import java.util.Arrays;
import java.util.Set;

/**
 * @ Author     ：libin
 * @ Date       ：Created in 11:58 2018/11/19
 * @ Description：
 * @ Modified By：
 * @Version:
 */
public class RedisKey {

    private JedisCluster jedisCluster;

    private JedisPool jedisPool;

    private ShardedJedisPool shardedJedisPool;

    private RedisMode redisMode;

    private String prefix = "";

    private Boolean clusterMode = false;

    public RedisKey(JedisCluster jedisCluster, RedisMode redisMode, String prefix) {
        this.jedisCluster = jedisCluster;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisKey(ShardedJedisPool shardedJedisPool, RedisMode redisMode, String prefix) {
        this.shardedJedisPool = shardedJedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisKey(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public RedisKey(JedisPool jedisPool, RedisMode redisMode, String prefix) {
        this.jedisPool = jedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public Long delKey(String... key) {
        Long append = null;
        String[] keys = prefix(prefix, key);
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    append = jedis.del(keys);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                append = jedisCluster.del(keys);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    for (String s : key) {
                        append = resource.del(s);
                    }
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return append;
    }

    public Long exists(String... key) {
        Long append = null;
        String[] keys = prefix(prefix, key);
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    append = jedis.exists(keys);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                append = jedisCluster.exists(key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    for (String s : key) {
                        Boolean reg = resource.exists(s);
                        if (reg) {
                            append = 1L;
                        }
                    }
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }

        return append;
    }

    public Long expire(String key, int seconds) {
        Long expire = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    expire = jedis.expire(prefix + key, seconds);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                expire = jedisCluster.expire(prefix + key, seconds);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    expire = resource.expire(prefix + key, seconds);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return expire;
    }

    public Long expireMilliseconds(String key, long milliseconds) {
        Long expire = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    expire = jedis.pexpire(prefix + key, milliseconds);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                expire = jedisCluster.pexpire(prefix + key, milliseconds);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    expire = resource.pexpire(prefix + key, milliseconds);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return expire;
    }

    public Long expireAt(String key, int unixTime) {
        Long expireAt = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    expireAt = jedis.expireAt(prefix + key, unixTime);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                expireAt = jedisCluster.expireAt(prefix + key, unixTime);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    expireAt = resource.expireAt(prefix + key, unixTime);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return expireAt;
    }

    public Long expireMillisecondsAt(String key, long milliseconds) {
        Long pexpireAt = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    pexpireAt = jedis.pexpireAt(prefix + key, milliseconds);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                pexpireAt = jedisCluster.pexpireAt(prefix + key, milliseconds);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    pexpireAt = resource.pexpireAt(prefix + key, milliseconds);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return pexpireAt;
    }

    public Set<String> keys(String key, int unixTime) throws Exception {
        Set<String> keys = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    keys = jedis.keys(prefix + key);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                throw new Exception("nonsupport cluster mode");
            case SHARDEDMODE:
                throw new Exception("nonsupport shardedJedis mode");
        }
        return keys;
    }

    public Long removeKeyExpire(String key) {
        Long persist = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    persist = jedis.persist(prefix + key);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                persist = jedisCluster.persist(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    persist = resource.persist(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return persist;
    }

    public Long ttl(String key) {
        Long ttl = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    ttl = jedis.ttl(prefix + key);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                ttl = jedisCluster.ttl(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    ttl = resource.ttl(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return ttl;
    }


    public Long pttl(String key) {
        Long pttl = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    pttl = jedis.pttl(prefix + key);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                pttl = jedisCluster.pttl(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    pttl = resource.pttl(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return pttl;
    }

    /**
     * 当 key 和 newkey 相同，或者 key 不存在时，返回一个错误。 当 newkey 已经存在时， RENAME 命令将覆盖旧值。
     *
     * @param key
     * @param newKey
     * @return
     */
    public String renameKey(String key, String newKey) throws Exception {
        String rename = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    rename = jedis.rename(prefix + key, newKey);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                rename = jedisCluster.rename(prefix + key, newKey);
                break;
            case SHARDEDMODE:
                throw new Exception("nonsupport shardedJedis mode");
        }
        return rename;
    }

    /**
     * 当且仅当 newkey 不存在时，将 key 改名为 newkey 。 当 key 不存在时，返回一个错误。
     *
     * @param key
     * @param newKey
     * @return
     */
    public Long renameKeyNX(String key, String newKey) throws Exception {
        Long renamenx = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    renamenx = jedis.renamenx(prefix + key, newKey);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                renamenx = jedisCluster.renamenx(prefix + key, newKey);
                break;
            case SHARDEDMODE:
                throw new Exception("nonsupport shardedJedis mode");
        }
        return renamenx;
    }

    /**
     * 返回数据类型
     *
     * @param key
     * @return
     */
    public String keyType(String key) {
        String type = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    type = jedis.type(prefix + key);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                type = jedisCluster.type(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    type = resource.type(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return type;
    }

    public ScanResult<String> scan(String key, ScanParams scanParams) throws Exception {
        ScanResult<String> scan = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    scan = jedis.scan(prefix + key, scanParams);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                scan = jedisCluster.scan(prefix + key, scanParams);
                break;
            case SHARDEDMODE:
                throw new Exception("nonsupport shardedJedis mode");
        }
        return scan;
    }

    public ScanResult<String> scan(String cursor) throws Exception {
        ScanResult<String> scan = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    scan = jedis.scan(cursor);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                throw new Exception("nonsupport cluster mode");
            case SHARDEDMODE:
                throw new Exception("nonsupport shardedJedis mode");
        }
        return scan;
    }

    private static String[] prefix(String prefix, String... key) {
        String[] keys = Arrays.copyOf(key, key.length);
        for (int i = 0; i < key.length; i++) {
            keys[i] = prefix + key[i];
        }
        return keys;
    }
}
