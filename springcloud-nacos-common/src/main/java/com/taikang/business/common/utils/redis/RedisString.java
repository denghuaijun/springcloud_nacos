package com.taikang.business.common.utils.redis;

import redis.clients.jedis.*;

import java.util.Arrays;
import java.util.List;

/**
 * @ Author     ：libin
 * @ Date       ：Created in 11:58 2018/11/19
 * @ Description：
 * @ Modified By：
 * @Version:
 */
public class RedisString {

    private JedisCluster jedisCluster;

    private JedisPool jedisPool;

    private ShardedJedisPool shardedJedisPool;

    private RedisMode redisMode;

    private boolean clusterMode = false;

    private String prefix = "";

    public RedisString(JedisCluster jedisCluster, RedisMode redisMode, String prefix) {
        this.jedisCluster = jedisCluster;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisString(ShardedJedisPool shardedJedisPool, RedisMode redisMode, String prefix) {
        this.shardedJedisPool = shardedJedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisString(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public RedisString(JedisPool jedisPool, RedisMode redisMode, String prefix) {
        this.jedisPool = jedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public Long append(String key, String value) {
        Long append = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    append = jedis.append(prefix + key, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                append = jedisCluster.append(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    append = resource.append(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return append;
    }

    public Long decr(String key) {
        Long decr = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    decr = jedis.decr(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                decr = jedisCluster.decr(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    decr = resource.decr(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return decr;
    }

    public Long decrBy(String key, long value) {
        Long decrBy = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    decrBy = jedis.decrBy(prefix +key, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                decrBy = jedisCluster.decrBy(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    decrBy = resource.decrBy(prefix +key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return decrBy;
    }

    public Long incr(String key) {
        Long incr = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    incr = jedis.incr(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                incr = jedisCluster.incr(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    incr = resource.incr(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return incr;
    }

    public Long incrBy(String key, long value) {
        Long incrBy = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    incrBy = jedis.incrBy(prefix + key, value);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                incrBy = jedisCluster.incrBy(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    incrBy = resource.incrBy(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return incrBy;
    }

    public String get(String key) {
        String get = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    get = jedis.get(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                get = jedisCluster.get(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    get = resource.get(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return get;
    }

    public String getSet(String key, String value) {
        String getSet = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    getSet = jedis.getSet(prefix + key, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                getSet = jedisCluster.getSet(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    getSet = resource.getSet(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return getSet;
    }

    public List<String> mGet(String... key) throws Exception {
        List<String> mget = null;
        String[] keys = prefix(prefix,key);
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    mget = jedis.mget(keys);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                mget = jedisCluster.mget(keys);
                break;
            case SHARDEDMODE:
                throw new Exception("nonsupport shardedJedis mode");
        }
        return mget;
    }

    public String mSet(String... keysvalues) throws Exception {
        String mset = null;
        String[] keysvalue = prefix(prefix , keysvalues);
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    mset = jedis.mset(keysvalue);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                mset = jedisCluster.mset(keysvalue);
                break;
            case SHARDEDMODE:
                throw new Exception("nonsupport shardedJedis mode");
        }
        return mset;
    }

    public Long mSetNX(String... keysvalues) throws Exception {
        Long msetNX = null;
        String[] keysvalue = prefix(prefix , keysvalues);
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    msetNX = jedis.msetnx(keysvalue);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                msetNX = jedisCluster.msetnx(keysvalue);
                break;
            case SHARDEDMODE:
                throw new Exception("nonsupport shardedJedis mode");
        }
        return msetNX;
    }

    public String set(String key, String value) {
        String set = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    set = jedis.set(prefix + key, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                set = jedisCluster.set(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    set = resource.set(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return set;
    }

    /**
     * 同时设置NX\XX  PX\EX
     *
     * @param key
     * @param value
     * @param nxxx  NX ：只在键不存在时，才对键进行设置操作。 SET key value NX 效果等同于 SETNX key value 。 XX ：只在键已经存在时，才对键进行设置操作。
     * @param expx  EX seconds  PX milliseconds
     * @param time
     * @return
     */
    public String set(String key, String value, String nxxx, String expx, long time) {
        String set = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    set = jedis.set(prefix + key, value, nxxx, expx, time);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                set = jedisCluster.set(prefix + key, value, nxxx, expx, time);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    set = resource.set(prefix + key, value, nxxx, expx, time);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return set;
    }


    /**
     * @param key
     * @param value
     * @return 置成功，返回 1 。 设置失败，返回 0 。
     */
    public Long setNX(final String key, final String value) {
        Long setnx = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    setnx = jedis.setnx(prefix + key, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                setnx = jedisCluster.setnx(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    setnx = resource.setnx(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return setnx;
    }

    /**
     * 设置EX值 存在时覆盖
     *
     * @param key
     * @param seconds 秒为单位
     * @param value
     * @return
     */
    public String setEX(String key, int seconds, String value) {
        String sestEx = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    sestEx = jedis.setex(prefix + key, seconds, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                sestEx = jedisCluster.setex(prefix + key, seconds, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    sestEx = resource.setex(prefix + key, seconds, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return sestEx;
    }

    /**
     * 查询value长度
     *
     * @param key
     * @return
     */
    public Long length(String key) {
        Long strlen = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strlen = jedis.strlen(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strlen = jedisCluster.strlen(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strlen = resource.strlen(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strlen;
    }
    private static String[] prefix(String prefix , String... key){
        String[] keys = Arrays.copyOf(key,key.length);
        for (int i = 0 ; i < key.length ; i++){
            keys[i] = prefix + key[i];
        }
        return keys;
    }

}
