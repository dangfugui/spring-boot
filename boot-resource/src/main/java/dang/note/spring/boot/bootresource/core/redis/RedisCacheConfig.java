package dang.note.spring.boot.bootresource.core.redis;


import java.lang.reflect.Method;
import java.time.Duration;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableCaching // 开启缓存支持
public class RedisCacheConfig extends CachingConfigurerSupport {


    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;


    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }


    // 缓存管理器
    @Bean
    public CacheManager cacheManager() {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        RedisConfig.jackson2JsonRedisSerializer()))
                .disableCachingNullValues();
        RedisCacheManager cacheManager = RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(config)
                .build();
        return cacheManager;

    }


    /**
     * 缓存报错只打log 不抛异常 让请求继续处理
     */
    @Slf4j
    private static class RelaxedCacheErrorHandler implements CacheErrorHandler {
        @Override
        public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
            log.error("Error getting from cache.", exception);
        }

        @Override
        public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
            log.error("Error put to cache.", e);
        }

        @Override
        public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
            log.error("Error evict to cache.", e);
        }

        @Override
        public void handleCacheClearError(RuntimeException e, Cache cache) {
            log.error("Error clear cache.", e);
        }
    }


    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new RelaxedCacheErrorHandler();
    }

}
