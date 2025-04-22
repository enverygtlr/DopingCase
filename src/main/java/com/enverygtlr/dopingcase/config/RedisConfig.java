package com.enverygtlr.dopingcase.config;

import com.enverygtlr.dopingcase.domain.response.StudentReportResponse;
import com.enverygtlr.dopingcase.domain.response.TestResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.List;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${cache.config.defaultTtl:60}")
    private int defaultTtl;

    @Value("${cache.config.testTtl:30}")
    private int testTtl;

    @Value("${cache.config.reportTtl:120}")
    private int reportTtl;

    private final ObjectMapper objectMapper;

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(defaultTtl))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> {
            var testConf = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(testTtl))
                    .disableCachingNullValues()
                    .serializeValuesWith(
                            RedisSerializationContext.SerializationPair
                                    .fromSerializer(new Jackson2JsonRedisSerializer<>(TestResponse.class))
                    );

            var reportConf = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(reportTtl))
                    .disableCachingNullValues()
                    .serializeValuesWith(
                            RedisSerializationContext.SerializationPair
                                    .fromSerializer(new Jackson2JsonRedisSerializer<>(StudentReportResponse.class))
                    );


            builder
                    .withCacheConfiguration(CacheNames.TEST_CACHE, testConf)
                    .withCacheConfiguration(CacheNames.REPORT_CACHE, reportConf);
        };
    }
}