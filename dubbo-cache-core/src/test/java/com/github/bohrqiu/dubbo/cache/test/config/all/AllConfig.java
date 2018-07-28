package com.github.bohrqiu.dubbo.cache.test.config.all;

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
 * 扩展组件参数配置
 *
 * @author qiuboboy@qq.com
 * @date 2018-07-28 15:10
 */
@SpringBootApplication
@ComponentScan("com.github.bohrqiu.dubbo.cache.test.dubbo")
public class AllConfig {
    public static void main(String[] args) {
        SpringApplication.run(AllConfig.class, args);
    }

    @Configuration
    @ImportResource("META-INF/spring/dubbo-all.xml")
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
