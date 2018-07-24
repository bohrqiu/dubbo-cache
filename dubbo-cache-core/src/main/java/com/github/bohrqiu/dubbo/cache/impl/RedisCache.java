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
package com.github.bohrqiu.dubbo.cache.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.bohrqiu.dubbo.cache.CacheMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Arrays;

/**
 * @date 2018-07-23 21:52
 */
@Slf4j
public class RedisCache extends AbstractCache {
    private RedisTemplate redisTemplate;
    private long expirationSecs;
    private RedisConnectionFactory redisConnectionFactory;
    private RedisSerializer valueRedisSerializer;
    private byte[] prefix;

    public RedisCache(RedisTemplate redisTemplate, CacheMeta cacheMeta) {
        this.redisTemplate = redisTemplate;
        this.valueRedisSerializer = redisTemplate.getValueSerializer();
        this.expirationSecs = cacheMeta.getDubboCache().expire();
        this.redisConnectionFactory = redisTemplate.getConnectionFactory();
        String cacheName = cacheMeta.getDubboCache().cacheName();
        if (!StringUtils.isBlank(cacheName)) {
            this.prefix = new DefaultRedisCachePrefix().prefix(cacheName);
        } else {
            this.prefix = null;
        }
    }

    @Override
    public void put(Object key, Object value) {
        RedisConnection connection = null;
        try {
            connection = redisConnectionFactory.getConnection();
            byte[] newkey = computeKey(key);
            connection.set(
                    newkey, valueRedisSerializer.serialize(value), Expiration.seconds(expirationSecs), null);
        } catch (Exception ex) {
            log.warn("dubbo access cache FAILURE", ex);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public Object get(Object key) {
        RedisConnection connection = null;
        try {
            connection = redisConnectionFactory.getConnection();
            byte[] newkey = computeKey(key);
            byte[] bytes = connection.get(newkey);
            if (bytes == null) {
                return null;
            }
            return valueRedisSerializer.deserialize(bytes);
        } catch (Exception ex) {
            log.warn("dubbo access cache FAILURE", ex);
            return null;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private byte[] computeKey(Object key) {
        byte[] keyBytes =
                this.convertToBytesIfNecessary(
                        this.redisTemplate.getKeySerializer(), key);
        if (this.prefix != null) {
            byte[] result = Arrays.copyOf(this.prefix, keyBytes.length + prefix.length);
            System.arraycopy(keyBytes, 0, result, this.prefix.length, keyBytes.length);
            return result;
        } else {
            return keyBytes;
        }
    }

    private byte[] convertToBytesIfNecessary(RedisSerializer serializer, Object value) {
        return value instanceof byte[]
                ? (byte[]) (value)
                : (null == serializer ? null : serializer.serialize(value));
    }
}
