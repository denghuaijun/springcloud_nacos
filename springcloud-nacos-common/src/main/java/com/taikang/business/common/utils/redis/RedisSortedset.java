package com.taikang.business.common.utils.redis;

import redis.clients.jedis.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by libin on 2016/11/30.
 */

public class RedisSortedset {

    private JedisCluster jedisCluster;

    private JedisPool jedisPool;

    private ShardedJedisPool shardedJedisPool;

    private RedisMode redisMode;

    private boolean clusterMode = false;

    private String prefix = "";

    public RedisSortedset(JedisCluster jedisCluster, RedisMode redisMode, String prefix) {
        this.jedisCluster = jedisCluster;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisSortedset(ShardedJedisPool shardedJedisPool, RedisMode redisMode, String prefix) {
        this.shardedJedisPool = shardedJedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public RedisSortedset(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public RedisSortedset(JedisPool jedisPool, RedisMode redisMode, String prefix) {
        this.jedisPool = jedisPool;
        this.redisMode = redisMode;
        this.prefix = prefix;
    }

    public Long insertAndModifyScore(String key, Map<String, Double> values) {
        Long zadd = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zadd = jedis.zadd(prefix + key, values);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zadd = jedisCluster.zadd(prefix + key, values);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zadd = resource.zadd(prefix + key, values);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zadd;
    }

    public Long size(String key) {
        Long zcard = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zcard = jedis.zcard(prefix + key);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zcard = jedisCluster.zcard(prefix + key);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zcard = resource.zcard(prefix + key);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zcard;
    }


    /**
     * 返回排序分 区间内的个数
     *
     * @param minScore
     * @param maxScore
     * @return
     */
    public Long size(String key, double minScore, double maxScore) {
        Long zcount = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zcount = jedis.zcount(prefix + key, minScore, maxScore);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zcount = jedisCluster.zcount(prefix + key, minScore, maxScore);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zcount = resource.zcount(prefix + key, minScore, maxScore);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zcount;
    }

    /**
     * 加分 不存在的value会insert
     *
     * @return
     */
    public Double sscoreIncrby(String key, String value, double increment) {
        Double zincrby = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zincrby = jedis.zincrby(prefix + key, increment, value);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zincrby = jedisCluster.zincrby(prefix + key, increment, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zincrby = resource.zincrby(prefix + key, increment, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zincrby;
    }


    /**
     * 获取下标区间（分数相同下标相同）数据 排序从小到大
     *
     * @param start
     * @param end
     * @return
     */
    public Set<String> getRangeAsc(String key, int start, int end) {
        Set<String> zrange = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zrange = jedis.zrange(prefix + key, start, end);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zrange = jedisCluster.zrange(prefix + key, start, end);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zrange = resource.zrange(prefix + key, start, end);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zrange;
    }


    public Set<Tuple> getRangeAscWithScore(String key, int start, int end) {
        Set<Tuple> tuples = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    tuples = jedis.zrangeWithScores(prefix + key, start, end);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                tuples = jedisCluster.zrangeWithScores(prefix + key, start, end);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    tuples = resource.zrangeWithScores(prefix + key, start, end);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return tuples;
    }

    /**
     * 获取下标区间（分数相同下标相同）数据 排序从大到小
     *
     * @param start
     * @param end
     * @return
     */
    public Set<String> getRangeDesc(String key, int start, int end) {
        Set<String> zrange = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zrange = jedis.zrevrange(prefix + key, start, end);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zrange = jedisCluster.zrevrange(prefix + key, start, end);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zrange = resource.zrevrange(prefix + key, start, end);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zrange;
    }

    public Set<String> getRangePageDesc(String key, int page, int size) {
        int start = 0;
        int end;
        if (page > 0) {
            start = (page - 1) * size;
        }
        end = start + size - 1;
        Set<String> zrange = getRangeDesc(prefix + key, start, end);
        return zrange;
    }

    public Set<Tuple> getRangeDescWithScore(String key, int start, int end) {
        Set<Tuple> tuples = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    tuples = jedis.zrevrangeWithScores(prefix + key, start, end);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                tuples = jedisCluster.zrevrangeWithScores(prefix + key, start, end);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    tuples = resource.zrevrangeWithScores(prefix + key, start, end);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return tuples;
    }

    public Set<Tuple> getRangeDescPageWithScore(String key, int page, int size) {
        int start = 0;
        int end;
        if (page > 0) {
            start = (page - 1) * size;
        }
        end = start + size - 1;
        Set<Tuple> tuples = getRangeDescWithScore(prefix + key, start, end);
        return tuples;
    }

    /**
     * 分数区间查询数据 排序从小到大   1 < score <= 5 的成员 start=(1 end=5  5 < score < 10 的成员 start=(5 end=(10
     * <p>
     * redisSortedset.getByScoreAsc("-inf", "+inf"); 显示整个有序集
     * redisSortedset.getByScoreAsc("-inf", "5000"); 显示 <=5000 的所有成员
     * redisSortedset.getByScoreAsc("(5000", "400000"); 显示 大于 5000 小于等于 400000 的成员
     *
     * @param start
     * @param end
     */
    public Set<String> getByScoreAsc(String key, String start, String end) {
        Set<String> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrangeByScore(prefix + key, start, end);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrangeByScore(prefix + key, start, end);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrangeByScore(prefix + key, start, end);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }

    public Set<String> getByScoreAsc(String key, double start, double end) {
        Set<String> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrangeByScore(prefix + key, start, end);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrangeByScore(prefix + key, start, end);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrangeByScore(prefix + key, start, end);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }

    /**
     * 带分页的排序 根据分数查询  -inf +inf 根据分数从小到大
     *
     * @param start
     * @param end
     * @param page
     * @param size
     */
    public Set<String> getByScoreAscPage(String key, String start, String end, int page, int size) {
        int startPage = 0;
        if (page > 0) {
            startPage = (page - 1) * size;
        }
        Set<String> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrangeByScore(prefix + key, start, end, startPage, size);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrangeByScore(prefix + key, start, end, startPage, size);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrangeByScore(prefix + key, start, end, startPage, size);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }


    public Set<String> getByScoreAscPage(String key, int start, int end, int page, int size) {
        int startPage = 0;
        if (page > 0) {
            startPage = (page - 1) * size;
        }
        Set<String> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrangeByScore(prefix + key, start, end, startPage, size);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrangeByScore(prefix + key, start, end, startPage, size);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrangeByScore(prefix + key, start, end, startPage, size);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }

    public Set<Tuple> getByScoreAscWithScorePage(String key, String start, String end, int page, int size) {
        int startPage = 0;
        if (page > 0) {
            startPage = (page - 1) * size;
        }
        Set<Tuple> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrangeByScoreWithScores(prefix + key, start, end, startPage, size);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrangeByScoreWithScores(prefix + key, start, end, startPage, size);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrangeByScoreWithScores(prefix + key, start, end, startPage, size);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }

    /**
     * 分数区间查询数据 排序从大到小   1 < score <= 5 的成员 start=(1 end=5  5 < score < 10 的成员 start=(5 end=(10
     *
     * @param start
     * @param end
     */
    public Set<String> getByScoreDesc(String key, String start, String end) {
        Set<String> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrevrangeByScore(prefix + key, start, end);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrevrangeByScore(prefix + key, start, end);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrevrangeByScore(prefix + key, start, end);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }

    public Set<String> getByScoreDesc(String key, double start, double end) {
        Set<String> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrevrangeByScore(prefix + key, start, end);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrevrangeByScore(prefix + key, start, end);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrevrangeByScore(prefix + key, start, end);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }

    /**
     * 带分页的排序 根据分数查询  -inf +inf 根据分数从大到小
     *
     * @param start
     * @param end
     * @param page
     * @param size
     */
    public Set<String> getByScoreDescPage(String key, String start, String end, int page, int size) {
        int startPage = 0;
        if (page > 0) {
            startPage = (page - 1) * size;
        }
        Set<String> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrevrangeByScore(prefix + key, start, end, startPage, size);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrevrangeByScore(prefix + key, start, end, startPage, size);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrevrangeByScore(prefix + key, start, end, startPage, size);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }

    public Set<String> getByScoreDescPage(String key, int start, int end, int page, int size) {
        int startPage = 0;
        if (page > 0) {
            startPage = (page - 1) * size;
        }
        Set<String> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrevrangeByScore(prefix + key, start, end, startPage, size);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrevrangeByScore(prefix + key, start, end, startPage, size);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrevrangeByScore(prefix + key, start, end, startPage, size);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }

    public Set<Tuple> getByScoreDescWithScorePage(String key, String start, String end, int page, int size) {
        int startPage = 0;
        if (page > 0) {
            startPage = (page - 1) * size;
        }
        Set<Tuple> strings = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    strings = jedis.zrevrangeByScoreWithScores(prefix + key, start, end, startPage, size);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                strings = jedisCluster.zrevrangeByScoreWithScores(prefix + key, start, end, startPage, size);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    strings = resource.zrevrangeByScoreWithScores(prefix + key, start, end, startPage, size);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return strings;
    }

    /**
     * 查找排名的游标值 分数从小到大
     *
     * @param value
     * @return
     */
    public Long findTopByValueAsc(String key, String value) {
        Long zrank = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zrank = jedis.zrank(prefix + key, value);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zrank = jedisCluster.zrank(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zrank = resource.zrank(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zrank;
    }

    /**
     * 查找排名的游标值 分数从大到小
     *
     * @param value
     * @return
     */
    public Long findTopByValueDesc(String key, String value) {
        Long zrevrank = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zrevrank = jedis.zrevrank(prefix + key, value);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zrevrank = jedisCluster.zrevrank(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zrevrank = resource.zrevrank(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zrevrank;
    }

    /**
     * 查找分数
     *
     * @param value
     * @return
     */
    public Double findScoreByValue(String key, String value) {
        Double zscore = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zscore = jedis.zscore(prefix + key, value);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zscore = jedisCluster.zscore(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zscore = resource.zscore(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zscore;
    }


    public Long remove(String key, String... value) {
        Long zrem = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zrem = jedis.zrem(prefix + key, value);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zrem = jedisCluster.zrem(prefix + key, value);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zrem = resource.zrem(prefix + key, value);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zrem;
    }


    public void remove(String key, Set<String> values) {
        Iterator<String> iterator = values.iterator();
        while (iterator.hasNext()) {
            Long zrem = null;
            switch (redisMode) {
                case POOLMODE:
                    Jedis jedis = jedisPool.getResource();
                    try {
                        zrem = jedis.zrem(prefix + key, iterator.next());
                    } finally {
                        if (jedis != null) {
                            jedis.close();
                        }
                    }
                    break;
                case CLUSTERMODE:
                    zrem = jedisCluster.zrem(prefix + key, iterator.next());
                    break;
                case SHARDEDMODE:
                    ShardedJedis resource = shardedJedisPool.getResource();
                    try {
                        zrem = resource.zrem(prefix + key, iterator.next());
                    } finally {
                        if (resource != null) {
                            resource.close();
                        }
                    }
                    break;
            }
        }
    }

    public ScanResult<Tuple> zscan(String key, String cursor, String match, Integer count) {
        if (match == null) {
            ScanResult<Tuple> zscan = null;
            switch (redisMode) {
                case POOLMODE:
                    Jedis jedis = jedisPool.getResource();
                    try {
                        zscan = jedis.zscan(prefix + key, cursor);
                    } finally {
                        if (jedis != null) {
                            jedis.close();
                        }
                    }
                    break;
                case CLUSTERMODE:
                    zscan = jedisCluster.zscan(prefix + key, cursor);
                    break;
                case SHARDEDMODE:
                    ShardedJedis resource = shardedJedisPool.getResource();
                    try {
                        zscan = resource.zscan(prefix + key, cursor);
                    } finally {
                        if (resource != null) {
                            resource.close();
                        }
                    }
                    break;
            }
            return zscan;

        }

        ScanParams params = new ScanParams();
        if (match != null) {
            params.match(match);
        }
        if (count != null) {
            params.count(count);

        }

        ScanResult<Tuple> zscan = null;
        switch (redisMode) {
            case POOLMODE:
                Jedis jedis = jedisPool.getResource();
                try {
                    zscan = jedis.zscan(prefix + key, cursor, params);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                break;
            case CLUSTERMODE:
                zscan = jedisCluster.zscan(prefix + key, cursor, params);
                break;
            case SHARDEDMODE:
                ShardedJedis resource = shardedJedisPool.getResource();
                try {
                    zscan = resource.zscan(prefix + key, cursor, params);
                } finally {
                    if (resource != null) {
                        resource.close();
                    }
                }
                break;
        }
        return zscan;

    }
}
