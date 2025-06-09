package ma.foodplus.ordering.system.product.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class CacheMonitoringConfig {

    @Bean
    public CacheResolver cacheResolver(CacheManager cacheManager) {
        SimpleCacheResolver resolver = new SimpleCacheResolver(cacheManager);
        return resolver;
    }

    @Bean
    public CacheMetricsCollector cacheMetricsCollector(MeterRegistry meterRegistry, CacheManager cacheManager, RedisConnectionFactory redisConnectionFactory) {
        return new CacheMetricsCollector(meterRegistry, cacheManager, redisConnectionFactory);
    }
} 