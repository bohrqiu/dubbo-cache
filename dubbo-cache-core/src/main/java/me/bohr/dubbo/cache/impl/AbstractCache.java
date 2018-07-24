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
package me.bohr.dubbo.cache.impl;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import me.bohr.dubbo.cache.Cache;
import me.bohr.dubbo.cache.CacheKeyValidator;
import me.bohr.dubbo.cache.CacheValueValidator;

/**
 * @date 2018-07-23 21:52
 */
public class AbstractCache implements Cache {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected CacheValueValidator cacheValueValidator;
    protected CacheKeyValidator cacheKeyValidator;

    public AbstractCache() {
        this.cacheValueValidator = ExtensionLoader.getExtensionLoader(CacheValueValidator.class).getAdaptiveExtension();
        this.cacheKeyValidator = ExtensionLoader.getExtensionLoader(CacheKeyValidator.class).getAdaptiveExtension();
    }

    public void put(Object key, Object value) {

    }

    public Object get(Object key) {
        return null;
    }

    public CacheValueValidator getCacheValueValidator() {
        return cacheValueValidator;
    }


    public CacheKeyValidator getCacheKeyValidator() {
        return cacheKeyValidator;
    }

}
