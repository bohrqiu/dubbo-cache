/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.bohr.dubbo.cache.factory;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.config.spring.extension.SpringExtensionFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import me.bohr.dubbo.cache.Cache;
import me.bohr.dubbo.cache.CacheMeta;
import me.bohr.dubbo.cache.impl.RedisCache;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @date 2018-07-23 21:52
 */
public class RedisCacheFactory extends AbstractCacheFactory {
    private final static Logger logger = LoggerFactory.getLogger(RedisCacheFactory.class);

    private RedisTemplate redisTemplate;

    public RedisCacheFactory() {
        try {
            ApplicationContext context = ServiceBean.getSpringContext();
            if (context == null) {
                Field contexts = ReflectionUtils.findField(SpringExtensionFactory.class, "contexts");
                contexts.setAccessible(true);
                Set<ApplicationContext> contextSet = (Set<ApplicationContext>) contexts.get(null);
                if (contextSet.isEmpty()) {
                    logger.warn("non spring application,cache dont work");
                    return;
                }
                context = contextSet.iterator().next();
            }

            if (context == null) {
                logger.warn("non spring application,cache dont work");
                return;
            }
            Map<String, RedisTemplate> beansOfType = context.getBeansOfType(RedisTemplate.class);
            if (beansOfType.isEmpty()) {
                throw new IllegalStateException("RedisTemplate is not found in spring container");
            } else {
                redisTemplate = beansOfType.values().iterator().next();
            }
        } catch (Exception e) {
            logger.warn("dubbo client cache FAILURE", e);
        }
    }

    @Override
    public Cache doGetCache(Invoker<?> invoker, Invocation inv, CacheMeta cacheMeta) {
        if (redisTemplate == null) {
            return null;
        }
        return new RedisCache(redisTemplate, cacheMeta);
    }
}