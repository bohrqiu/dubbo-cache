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
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import me.bohr.dubbo.cache.Cache;
import me.bohr.dubbo.cache.CacheFactory;
import me.bohr.dubbo.cache.CacheMeta;
import me.bohr.dubbo.cache.el.SpelKeyGenerator;
import me.bohr.dubbo.cache.impl.NullCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @date 2018-07-24 01:48
 */
public abstract class AbstractCacheFactory implements CacheFactory {
    private static final Logger logger = LoggerFactory.getLogger(SpelKeyGenerator.class);

    private static ConcurrentMap<CacheMeta, Cache> concurrentMap = new ConcurrentHashMap();

    public Cache getCache(Invoker<?> invoker, Invocation inv, CacheMeta cacheMeta) {
        Cache cache;
        try {
            cache = concurrentMap.get(cacheMeta);
            if (cache == null) {
                cache = doGetCache(invoker, inv, cacheMeta);
                if (cache == null) {
                    cache = NullCache.INSTANCE;
                }
                concurrentMap.putIfAbsent(cacheMeta, cache);
            }
        } catch (Exception e) {
            logger.warn("create CacheFactory FAILURE", e);
            cache = NullCache.INSTANCE;
        }
        return cache;
    }

    public abstract Cache doGetCache(Invoker<?> invoker, Invocation inv, CacheMeta cacheMeta);
}
