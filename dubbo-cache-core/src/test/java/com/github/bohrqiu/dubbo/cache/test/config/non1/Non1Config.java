package com.github.bohrqiu.dubbo.cache.test.config.non1;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 不扩展组件配置
 *
 * @author qiuboboy@qq.com
 * @date 2018-07-28 15:10
 */
@SpringBootApplication
@ComponentScan("com.github.bohrqiu.dubbo.cache.test.dubbo")
@EnableAutoConfiguration(exclude={RedisAutoConfiguration.class,RedisRepositoriesAutoConfiguration.class})
public class Non1Config {
    public static void main(String[] args) {
        SpringApplication.run(Non1Config.class, args);
    }

    @Configuration
    @ImportResource("META-INF/spring/dubbo-non.xml")
    @DubboComponentScan(basePackages = "com.github.bohrqiu.dubbo.cache.test.dubbo")
    public class ConsumerConfiguration {
    }
}
