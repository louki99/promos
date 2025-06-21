package ma.foodplus.ordering.system.partner.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String PARTNERS_CACHE = "partners";
    public static final String PARTNER_CACHE = "partner";
    public static final String PARTNER_GROUPS_CACHE = "partner-groups";
    public static final String PARTNER_STATISTICS_CACHE = "partner-statistics";
    public static final String VIP_PARTNERS_CACHE = "vip-partners";
    public static final String ACTIVE_PARTNERS_CACHE = "active-partners";

    @Bean
    @Primary
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(List.of(
            PARTNERS_CACHE,
            PARTNER_CACHE,
            PARTNER_GROUPS_CACHE,
            PARTNER_STATISTICS_CACHE,
            VIP_PARTNERS_CACHE,
            ACTIVE_PARTNERS_CACHE
        ));
        return cacheManager;
    }
} 