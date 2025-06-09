package ma.foodplus.ordering.system.product.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
public class CacheMetricsCollector {

    private final MeterRegistry meterRegistry;
    private final CacheManager cacheManager;
    private final RedisConnectionFactory redisConnectionFactory;

    public CacheMetricsCollector(MeterRegistry meterRegistry, CacheManager cacheManager, RedisConnectionFactory redisConnectionFactory) {
        this.meterRegistry = meterRegistry;
        this.cacheManager = cacheManager;
        this.redisConnectionFactory = redisConnectionFactory;
        registerCacheMetrics();
    }

    private void registerCacheMetrics() {
        if (cacheManager instanceof RedisCacheManager) {
            RedisCacheManager redisCacheManager = (RedisCacheManager) cacheManager;
            redisCacheManager.getCacheNames().forEach(cacheName -> {
                RedisCache cache = (RedisCache) redisCacheManager.getCache(cacheName);
                if (cache != null) {
                    // Register cache metrics
                    meterRegistry.gauge("cache.size", 
                        Collections.singletonList(Tag.of("cache", cacheName)),
                        getCacheSize());
                }
            });
        }
    }

    @Scheduled(fixedRate = 60, timeUnit = TimeUnit.SECONDS)
    public void collectCacheMetrics() {
        if (cacheManager instanceof RedisCacheManager) {
            RedisCacheManager redisCacheManager = (RedisCacheManager) cacheManager;
            redisCacheManager.getCacheNames().forEach(cacheName -> {
                RedisCache cache = (RedisCache) redisCacheManager.getCache(cacheName);
                if (cache != null) {
                    // Update cache metrics
                    meterRegistry.gauge("cache.size", 
                        Collections.singletonList(Tag.of("cache", cacheName)),
                        getCacheSize());
                }
            });
        }
    }

    private Number getCacheSize() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            return connection.dbSize();
        }
    }
} 