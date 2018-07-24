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
package com.github.bohrqiu.dubbo.cache.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.*;
import com.github.bohrqiu.dubbo.cache.Cache;
import com.github.bohrqiu.dubbo.cache.CacheFactory;
import com.github.bohrqiu.dubbo.cache.CacheMeta;
import com.github.bohrqiu.dubbo.cache.KeyGenerator;
import com.github.bohrqiu.dubbo.cache.el.SpelKeyGenerator;
import com.github.bohrqiu.dubbo.cache.impl.NullCache;

/**
 * @date 2018-07-23 21:52
 */
@Activate(group = {Constants.CONSUMER}, order = Integer.MIN_VALUE + 1)
public class ConsumerCacheFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerCacheFilter.class);

    private CacheFactory cacheFactory;

    private KeyGenerator keyGenerator = new SpelKeyGenerator();


    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        CacheMeta cacheMeta = CacheMeta.build(invoker, invocation);
        if (cacheMeta != null) {
            Cache cache = cacheFactory.getCache(invoker, invocation, cacheMeta);
            if (cache != null && !cache.equals(NullCache.INSTANCE)) {
                Object key = keyGenerator.key(cacheMeta, invocation.getArguments());
                if (cache.getCacheKeyValidator().isValid(invoker.getUrl(), invocation, key)) {
                    Object value = cache.get(key);
                    if (value != null) {
                        logger.info(String.format("@DubboCache hit,service=%s,key=%s,result=%s",
                                cacheMeta.getMethodFullName(), key, value));
                        return new RpcResult(value);
                    }
                    Result result = invoker.invoke(invocation);
                    if (!result.hasException()) {
                        if (cache.getCacheValueValidator().isValid(invoker.getUrl(), invocation, result.getValue())) {
                            cache.put(key, result.getValue());
                        }
                    }
                    return result;
                } else {
                    logger.warn(String.format("key[%s] is not support by %s", key, cache.getClass().getName()));
                }
            }
        }
        return invoker.invoke(invocation);
    }

    public CacheFactory getCacheFactory() {
        return cacheFactory;
    }

    public void setCacheFactory(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }
}
