package com.zorpz.zcoe.shiro.cache;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.SortingParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: wangkui
 * @Date: 2019/4/10 16:34
 * @Description:
 */
public interface JedisClient {
    String set(String key, String value, int indexDb);


    String get(String key, int indexDb);

    byte[] get(byte[] key, int indexDb);

    Long del(String... key);

    Long del(int indexDb, String... keys);

    Long del(int indexDb, byte[]... keys);

    Long append(String key, String str);

    Boolean exists(String key);

    Boolean exists(String key, int indexDb);

    String flushDB();

    Long expire(String key, int seconds, int indexDb);

    Long ttl(String key, int indexDb);

    Long persist(String key);

    Long incr(String key);

    String setex(String key, String value, int seconds);

    String setex(String key, String value, int seconds, int indexDb);

    Long setnx(String key, String value);

    String getSet(String key, String value);

    Long setrange(String key, String str, int offset);

    List<String> mget(String... keys);

    String mset(String... keysValues);

    Long msetnx(String... keysValues);

    String getset(String key, String value);

    String getrange(String key, int startOffset, int endOffset);

    Long incrBy(String key, Long integer);

    Long decr(String key);

    Long decrBy(String key, Long integer);

    Long serlen(String key);

    Long hset(String key, String field, String value);

    Long hsetnx(String key, String field, String value);

    String hmset(String key, Map<String, String> hash, int indexDb);

    String hget(String key, String field);

    List<String> hmget(String key, int indexDb, String... fields);

    Long hincrby(String key, String field, Long value);

    Boolean hexists(String key, String field);

    Long hlen(String key);

    Long hdel(String key, String... field);

    Set<String> hkeys(String key);

    List<String> hvals(String key);

    Map<String, String> hgetall(String key, int indexDb);

    Long lpush(int indexDb, String key, String... strs);

    Long rpush(String key, String... strs);

    Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value);

    String lset(String key, Long index, String value);

    Long lrem(String key, long count, String value);

    String ltrim(String key, long start, long end);

    String lpop(String key);

    String rpop(String key, int indexDb);

    String rpoplpush(String srckey, String dstkey, int indexDb);

    String lindex(String key, long index);

    Long llen(String key);

    List<String> lrange(String key, long start, long end, int indexDb);

    String lset(String key, long index, String value);

    List<String> sort(String key, SortingParams sortingParameters);

    List<String> sort(String key);

    Long sadd(String key, String... members);

    Long srem(String key, String... members);

    String spop(String key);

    Set<String> sdiff(String... keys);

    Long sdiffstore(String dstkey, String... keys);

    Set<String> sinter(String... keys);

    Long sinterstore(String dstkey, String... keys);

    Set<String> sunion(String... keys);

    Long sunionstore(String dstkey, String... keys);

    Long smove(String srckey, String dstkey, String member);

    Long scard(String key);

    Boolean sismember(String key, String member);

    String srandmember(String key);

    Set<String> smembers(String key);

    Long zadd(String key, double score, String member);

    Set<String> zrange(String key, long min, long max);

    Long zcount(String key, double min, double max);

    Long hincrBy(String key, String value, long increment);

    Long zrem(String key, String... members);

    Double zincrby(String key, double score, String member);

    Long zrank(String key, String member);

    Long zrevrank(String key, String member);

    Set<String> zrevrange(String key, long start, long end);

    Set<String> zrangebyscore(String key, String max, String min);

    Set<String> zrangeByScore(String key, double max, double min);

    Long zcount(String key, String min, String max);

    Long zcard(String key);

    Double zscore(String key, String member);

    Long zremrangeByRank(String key, long start, long end);

    Long zremrangeByScore(String key, double start, double end);

    Set<String> keys(String pattern);

    Set<String> keysBySelect(String pattern, int database);

    String type(String key);

    byte[] ObjTOSerialize(Object obj);

    Object unserialize(byte[] bytes);

}
