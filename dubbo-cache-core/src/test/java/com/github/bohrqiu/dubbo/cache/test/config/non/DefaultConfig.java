package com.github.bohrqiu.dubbo.cache.test.config.non;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 不扩展组件配置
 *
 * @author qiuboboy@qq.com
 * @date 2018-07-28 15:10
 */
@SpringBootApplication
@ComponentScan("com.github.bohrqiu.dubbo.cache.test.dubbo")
public class DefaultConfig {
    public static void main(String[] args) {
        SpringApplication.run(DefaultConfig.class, args);
    }

    @Configuration
    @ImportResource("META-INF/spring/dubbo-non.xml")
    @DubboComponentScan(basePackages = "com.github.bohrqiu.dubbo.cache.test.dubbo")
    public class ConsumerConfiguration {
        @Bean
        public JedisConnectionFactory redisConnectionFactory() {
            return new JedisConnectionFactory();
        }

        @Bean
        public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
            template.setConnectionFactory(redisConnectionFactory);
            template.setKeySerializer(new StringRedisSerializer());
            return template;
        }
    }
}
