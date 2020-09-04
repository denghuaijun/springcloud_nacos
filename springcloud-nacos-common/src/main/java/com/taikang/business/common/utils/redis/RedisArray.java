package com.taikang.business.common.utils.redis;

import redis.clients.jedis.*;

import java.util.List;

/**
 * Created by libin on 2016/11/30.
 */
public class RedisArray {

    private JedisCluster jedisCluster;

    private JedisPool jedisPool;

    private ShardedJedisPool shardedJedisPool;

    private RedisMode redisMode;

    private boolean clusterMode = false;

    private String prefix = "";

    public RedisArray(JedisCluster jedisCluster, RedisMode redisMode, String prefix) {
        this.jedisCluster = jedisCluster;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisArray(ShardedJedisPool shardedJedisPool, RedisMode redisMode, String prefix) {
        this.shardedJedisPool = shardedJedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisArray(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public RedisArray(JedisPool jedisPool, RedisMode redisMode, String prefix) {
        this.jedisPool = jedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public Long size(String key) {
        Long llen = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    llen = jedis.llen(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                llen = jedisCluster.llen(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    llen = resource.llen(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return llen;
    }

    /**
     * array 头部添加数据
     *
     * @param values
     * @return
     */
    public Long ladd(String key , String... values) {
        Long lpush = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    lpush = jedis.lpush(prefix + key,values);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                lpush = jedisCluster.lpush(prefix + key,values);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    lpush = resource.lpush(prefix + key,values);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return lpush;

    }

    public Long add(String key, String... value) {
        Long rpush = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    rpush = jedis.rpush(prefix + key,value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                rpush = jedisCluster.rpush(prefix + key,value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    rpush = resource.rpush(prefix + key,value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return rpush;

    }

    /**
     * 指定数据前或后添加数据
     *
     * @param oldValue
     * @param where
     * @param newValue
     * @return -1：没找到指定元素 0：列表为空
     */
    public Long insert(String key, String oldValue, Client.LIST_POSITION where, String newValue) {
        Long linsert = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    linsert = jedis.linsert(prefix + key, where, oldValue, newValue);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                linsert = jedisCluster.linsert(prefix + key, where, oldValue, newValue);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    linsert = resource.linsert(prefix + key, where, oldValue, newValue);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return linsert;
    }


    /**
     * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * count = 0 : 移除表中所有与 VALUE 相等的值。
     *
     * @param count
     * @param value
     * @return
     */
    public Long remove(String key, int count, String value) {
        Long lrem = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    lrem = jedis.lrem(prefix + key, count, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                lrem = jedisCluster.lrem(prefix + key, count, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    lrem = resource.lrem(prefix + key, count, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return lrem;
    }

    /**
     * 移除并返回列表最后一个元素
     *
     * @return
     */
    public String removeLast(String key) {
        String rpop = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    rpop = jedis.rpop(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                rpop = jedisCluster.rpop(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    rpop = resource.rpop(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return rpop;
    }

    /**
     * 移除并返回列表第一个元素
     *
     * @return
     */
    public String removeFirst(String key) {
        String lpop = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    lpop = jedis.lpop(prefix + key);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                lpop = jedisCluster.lpop(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    lpop = resource.lpop(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return lpop;
    }

    public String get(String key, int index) {
        String lindex = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    lindex = jedis.lindex(prefix + key, index);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                lindex = jedisCluster.lindex(prefix + key, index);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    lindex = resource.lindex(prefix + key, index);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return lindex;
    }

    /**
     * 遍历所有的数据是 0 -1     （-1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。）
     *
     * @param start 0 第一个游标数
     * @param end   -1 最后一个游标
     * @return
     */
    public List<String> get(String key, int start, int end) {
        List<String> lrange = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    lrange = jedis.lrange(prefix + key, start, end);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                lrange = jedisCluster.lrange(prefix + key, start, end);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    lrange = resource.lrange(prefix + key, start, end);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return lrange;
    }

    /**
     * 分页array
     *
     * @param page
     * @param size
     * @return
     */
    public List<String> getByPage(String key, int page, int size) {
        int start = (page - 1) * size;
        int end = start + size - 1;
        return this.get(prefix + key,start, end);
    }


    public int set(String key, int index, String value) {
        String lset = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    lset = jedis.lset(prefix + key, index, value);
                }finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                lset = jedisCluster.lset(prefix + key, index, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    lset = resource.lset(prefix + key, index, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return 0;

    }
}
