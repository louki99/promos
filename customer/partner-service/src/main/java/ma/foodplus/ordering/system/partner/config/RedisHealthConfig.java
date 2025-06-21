package ma.foodplus.ordering.system.partner.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * Redis Health Check Configuration
 * 
 * <p>Provides Redis connectivity testing and cache performance monitoring.
 * This helps ensure the Redis cache system is working properly.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Slf4j
@Component
public class RedisHealthConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final CacheManager cacheManager;

    public RedisHealthConfig(RedisConnectionFactory redisConnectionFactory, CacheManager cacheManager) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.cacheManager = cacheManager;
        
        // Test Redis connection on startup
        testRedisConnection();
    }

    public void testRedisConnection() {
        try {
            log.info("Testing Redis connection...");
            
            // Test Redis connection
            RedisConnection connection = redisConnectionFactory.getConnection();
            
            // Test basic Redis operations
            String testKey = "health-check:" + System.currentTimeMillis();
            String testValue = "test-value";
            
            // Set a test value
            connection.set(testKey.getBytes(), testValue.getBytes());
            connection.expire(testKey.getBytes(), 60); // Expire in 60 seconds
            
            // Get the test value
            byte[] retrievedValue = connection.get(testKey.getBytes());
            
            // Clean up
            connection.del(testKey.getBytes());
            connection.close();
            
            if (retrievedValue != null && new String(retrievedValue).equals(testValue)) {
                log.info("✅ Redis connection test PASSED - Cache system is operational");
                log.info("Cache Manager: {}", cacheManager.getClass().getSimpleName());
                log.info("Available Caches: {}", cacheManager.getCacheNames());
            } else {
                log.error("❌ Redis connection test FAILED - Data retrieval issue");
            }
            
        } catch (Exception e) {
            log.error("❌ Redis connection test FAILED - Connection error: {}", e.getMessage(), e);
        }
    }

    /**
     * Manual health check method that can be called to test Redis connectivity
     */
    public boolean isRedisHealthy() {
        try {
            RedisConnection connection = redisConnectionFactory.getConnection();
            String testKey = "health-check:" + System.currentTimeMillis();
            String testValue = "test-value";
            
            connection.set(testKey.getBytes(), testValue.getBytes());
            connection.expire(testKey.getBytes(), 60);
            
            byte[] retrievedValue = connection.get(testKey.getBytes());
            connection.del(testKey.getBytes());
            connection.close();
            
            return retrievedValue != null && new String(retrievedValue).equals(testValue);
        } catch (Exception e) {
            log.error("Redis health check failed: {}", e.getMessage());
            return false;
        }
    }
}