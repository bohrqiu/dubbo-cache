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


import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import me.bohr.dubbo.cache.Cache;
import me.bohr.dubbo.cache.CacheKeyValidator;
import me.bohr.dubbo.cache.CacheValueValidator;

/**
 * @date 2018-07-22 21:52
 */
public class NullCache implements Cache {
    public static NullCache INSTANCE = new NullCache();

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public CacheValueValidator getCacheValueValidator() {
        return new CacheValueValidator() {
            public boolean isValid(URL url, Invocation invocation, Object value) {
                return false;
            }
        };
    }

    @Override
    public CacheKeyValidator getCacheKeyValidator() {
        return new CacheKeyValidator() {
            public boolean isValid(URL url, Invocation invocation, Object key) {
                return false;
            }
        };
    }
}
