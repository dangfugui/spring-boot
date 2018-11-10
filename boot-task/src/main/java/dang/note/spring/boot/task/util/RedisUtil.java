package dang.note.spring.boot.task.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Description:redis处理工具类
 */
//@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // String类型的操作

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValue(String key, Object value, long timeOut) {
        redisTemplate.opsForValue().set(key, value, timeOut, TimeUnit.MILLISECONDS);
    }

    public void setValue(String key, Object value, long timeOut, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }

    public boolean setIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public <T> T getValue(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    // HASH操作

    /**
     * 将HashMap key中的域field的值设为value，如果如果 key 不存在，一个新的哈希表被创建并进行 put 操作，如果域 field
     * 已经存在于哈希表中，旧值将被覆盖.
     *
     * @param key   key
     * @param field field
     * @param value value
     */
    public void put(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 将Map key中的域field的值设为value,当且仅当域field不存在，否则操作无效，
     * 如果Map不存在则新建Map并进行putIfAbsent操作.
     *
     * @param key   key
     * @param field field
     * @param value value
     * @return boolean 设置成功，返回 1 .如果给定域已经存在且没有操作被执行，返回 0.
     */
    public boolean putIfAbsent(String key, String field, String value) {
        return redisTemplate.opsForHash().putIfAbsent(key, field, value);
    }

    /**
     * 同时将多个 field-value (域-值)对设置到Map的 key 中.此命令会覆盖哈希表中已存在的域.如果 key
     * 不存在，一个空Map被创建并执行 put 操作.
     *
     * @param key key
     * @param map map
     */
    public <K, V> void putAll(String key, Map<K, V> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 在事务中pushHash并设置过期时间.
     *
     * @return 插入的数据数量
     */
    public <K, V> void putAll(final String key, Map<K, V> map, long timeout, TimeUnit timeUnit) {
        // spring-data-redis在连接redis cluster时无法使用事务
        try {
            final byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);
            Map<byte[], byte[]> values = new HashMap<>();
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            RedisSerializer<V> serializer = (RedisSerializer<V>) redisTemplate.getValueSerializer();
            for (Map.Entry entry : map.entrySet()) {
                values.put(stringSerializer.serialize(entry.getKey()), serializer.serialize((V) entry.getValue()));
            }
            redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) {
                    connection.hMSet(rawKey, values);
                    connection.expire(rawKey, timeUnit.toSeconds(timeout));
                    return true;
                }
            }, true);
        } catch (Exception ex) {
            log.error("lpushAll error", ex);
        }
    }

    /**
     * 返回哈希表 key 中给定域 field 的值.
     *
     * @param key   key
     * @param field field
     * @return value
     */
    public <T> T get(String key, String field) {
        return (T) redisTemplate.opsForHash().get(key, field);
    }

    public <K, V> Map<K, V> getAll(String key) {
        return (Map<K, V>) redisTemplate.opsForHash().entries(key);
    }

    public boolean hasField(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 设置redis的过期时间
     *
     * @param key     key
     * @param timeout 过期时间 /毫秒
     */
    public void setExpireInMill(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public void setExpire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public void remove(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    // List的相关操作

    /**
     * 向list中左添加元素
     *
     * @param key 键
     * @param t   元素
     * @param <T> 元素的类型
     * @return 返回添加的个数
     */
    public <T> Long lpush(String key, T t) {
        return redisTemplate.opsForList().leftPush(key, t);
    }

    /**
     * 向list右侧添加元素
     *
     * @param key 键
     * @param t   元素
     * @param <T> 元素的类型
     * @return 返回添加的个数
     */
    public <T> Long rpush(String key, T t) {
        return redisTemplate.opsForList().rightPush(key, t);
    }

    /**
     * 从list右侧弹出元素
     *
     * @param key 键
     * @param <T> 元素的类型
     * @return 返回去除的元素
     */
    public <T> T rpop(String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 向list中添加所有的元素
     *
     * @param key 键
     * @param ts  元素
     * @param <T> 元素的类型
     * @return 返回添加的个数
     */
    public <T> Long pushAll(String key, List<T> ts) {
        return redisTemplate.opsForList().leftPushAll(key, ts.toArray());
    }

    /**
     * 在事务中pushList并设置过期时间.
     *
     * @return 插入的数据数量
     */
    public <T> long pushAll(final String key, List<T> ts, long timeout, TimeUnit timeUnit) {
        long result = 0;
        try {
            final byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);
            byte[][] values = new byte[ts.size()][];
            RedisSerializer<T> serializer = (RedisSerializer<T>) redisTemplate.getValueSerializer();
            int i = 0;
            for (T element : ts) {
                values[i++] = serializer.serialize(element);
            }
            result = redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) {
                    long ret = connection.lPush(rawKey, values);
                    connection.expire(rawKey, timeUnit.toSeconds(timeout));
                    return ret;
                }
            }, true);
        } catch (Exception ex) {
            log.error("lpushAll error", ex);
        }
        return result;
    }

    /**
     * 获取list中的所有元素
     *
     * @param key 键
     * @param <T> 元素的类型
     * @return 返回所有元素构成的集合
     */
    public <T> List<T> range(String key) {
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    public <T> Set<T> range(String key, long start, long end) {
        return (Set<T>) redisTemplate.opsForZSet().range(key, start, end);
    }

    // ZSet类型的操作

    /**
     * 增加元素及分值
     *
     * @param key   键
     * @param value 值
     * @param score 分值
     * @param <T>   泛型
     * @return 添加成功，true ,失败 false
     */
    public <T> boolean add(String key, T value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    public <T> Long removeSet(String key, T t) {
        return redisTemplate.opsForZSet().remove(key, t);
    }

    public <T> Set<T> rangeByScore(String key, double min, double max) {
        return (Set<T>) redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    public <T> Set<T> reverseRange(String key, long start, long end) {
        return (Set<T>) redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    public <T> Set<T> reverseRangeByScore(String key, double min, double max) {
        return (Set<T>) redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    public <T> Long sAdd(String key, Set<T> values) {
        return redisTemplate.opsForSet().add(key, values.toArray());
    }

    public <T> Long sRemove(String key, Set<T> values) {
        return redisTemplate.opsForSet().remove(key, values.toArray());
    }

    /**
     * 获取集合中的所有元素.
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Set<T> sMembers(String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断元素是否在集合中.
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Boolean sIsMembers(String key, T value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取集合中的元素数量.
     *
     * @param key
     * @return
     */
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Long increment(String key, int add) {
        return redisTemplate.opsForValue().increment(key, add);
    }


    public interface GetData<T> {
        T get();
    }

    public interface GetListData<T> {
        List<T> get();
    }

    /**
     * 获取redis数据，若未获取到，则调用getData获取，并设置缓存
     *
     * @param key
     * @param getData
     * @param expire
     * @param timeUnit
     * @return
     */
    public <T> T doRedisGet(String key, GetData<T> getData, long expire, TimeUnit timeUnit) {

        T entity = getValue(key);
        if (entity != null) {
            return entity;
        }
        entity = getData.get();
        setValue(key, entity, expire, timeUnit);
        return entity;
    }

    /**
     * 获取redis数据，若未获取到，则调用getData获取，并设置缓存
     *
     * @param key
     * @param getData
     * @param expire
     * @return
     */
    public <T> T doRedisGet(String key, GetData<T> getData, long expire) {
        return doRedisGet(key, getData, expire, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取redis数据，若未获取到，则调用getData获取，并设置缓存
     *
     * @param key
     * @param getData
     * @return
     */
    public <T> T doRedisGet(String key, GetData<T> getData) {
        T entity = getValue(key);
        if (entity != null) {
            return entity;
        }
        entity = getData.get();
        setValue(key, entity);
        return entity;
    }
}