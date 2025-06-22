package ma.foodplus.ordering.system.partner.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String PARTNERS_CACHE = "partners";
    public static final String PARTNER_CACHE = "partner";
    public static final String PARTNER_GROUPS_CACHE = "partner-groups";
    public static final String PARTNER_STATISTICS_CACHE = "partner-statistics";
    public static final String VIP_PARTNERS_CACHE = "vip-partners";
    public static final String ACTIVE_PARTNERS_CACHE = "active-partners";
    public static final String B2B_PARTNERS_CACHE = "b2b-partners";
    public static final String B2C_PARTNERS_CACHE = "b2c-partners";
    public static final String SUPPLIER_PARTNERS_CACHE = "supplier-partners";
    public static final String PARTNER_SEARCH_CACHE = "partner-search";
    public static final String CREDIT_SUMMARY_CACHE = "credit-summary";
    public static final String PERFORMANCE_METRICS_CACHE = "performance-metrics";

    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        // Create a custom serializer with Java 8 date/time support
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        
        // Default cache configuration
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // 1 hour default TTL
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        // Custom cache configurations for different cache types
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Partner details cache - longer TTL since partner data doesn't change frequently
        cacheConfigurations.put(PARTNER_CACHE, defaultConfig.entryTtl(Duration.ofHours(2)));
        
        // Statistics cache - shorter TTL since it's computed data
        cacheConfigurations.put(PARTNER_STATISTICS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // Search cache - shorter TTL for search results
        cacheConfigurations.put(PARTNER_SEARCH_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        // Credit summary cache - medium TTL
        cacheConfigurations.put(CREDIT_SUMMARY_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(45)));
        
        // Performance metrics cache - shorter TTL
        cacheConfigurations.put(PERFORMANCE_METRICS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(20)));
        
        // List caches - medium TTL
        cacheConfigurations.put(PARTNERS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put(VIP_PARTNERS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put(ACTIVE_PARTNERS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put(B2B_PARTNERS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put(B2C_PARTNERS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put(SUPPLIER_PARTNERS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // Groups cache - longer TTL since groups don't change frequently
        cacheConfigurations.put(PARTNER_GROUPS_CACHE, defaultConfig.entryTtl(Duration.ofHours(4)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
} 