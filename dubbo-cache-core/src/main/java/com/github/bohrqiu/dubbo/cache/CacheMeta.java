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
package com.github.bohrqiu.dubbo.cache;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @date 2018-07-23 21:52
 */
@Data
public class CacheMeta {
    private static final Logger logger = LoggerFactory.getLogger(CacheMeta.class);
    private static final String DELIMITER = ":";
    private static final String CACHE_PREFIX_CONTAIN_GROUP = "cachePrefixContainGroup";
    private static final String CACHE_PREFIX_CONTAIN_VERSION = "cachePrefixContainVersion";

    private Method method;
    private Class<?>[] parameterTypes;
    private DubboCache dubboCache;
    private Class targetClass;
    private String group;
    private String version;

    public static CacheMeta build(Invoker<?> invoker, Invocation inv) {
        try {
            String group = invoker.getUrl().getParameter(Constants.GROUP_KEY);
            String version = invoker.getUrl().getParameter(Constants.VERSION_KEY);

            Class interf = invoker.getInterface();
            String methodName = inv.getMethodName();
            Class[] argsClass = inv.getParameterTypes();
            Method method = interf.getDeclaredMethod(methodName, argsClass);
            DubboCache dubboCache = method.getAnnotation(DubboCache.class);
            if (dubboCache == null) {
                return null;
            }
            CacheMeta cacheMeta = new CacheMeta();
            cacheMeta.setMethod(method);
            cacheMeta.setDubboCache(dubboCache);
            cacheMeta.setParameterTypes(argsClass);
            cacheMeta.setTargetClass(interf);
            boolean cachePrefixContainGroup = invoker.getUrl().getParameter(CACHE_PREFIX_CONTAIN_GROUP, Boolean.FALSE);
            if (cachePrefixContainGroup) {
                cacheMeta.setGroup(group);
            }

            boolean cachePrefixContainVersion = invoker.getUrl().getParameter(CACHE_PREFIX_CONTAIN_VERSION, Boolean.FALSE);
            if (cachePrefixContainVersion) {
                cacheMeta.setVersion(version);
            }
            return cacheMeta;
        } catch (Exception e) {
            logger.warn("build cacheMeta FAILURE", e);
            return null;
        }
    }

    /**
     * get cache key prefix
     */
    public String getCachePrefix() {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(dubboCache.cacheName())) {
            sb.append(dubboCache.cacheName());
            sb.append(DELIMITER);
        }
        if (!StringUtils.isEmpty(group)) {
            sb.append(group);
            sb.append(DELIMITER);
        }
        if (!StringUtils.isEmpty(version)) {
            sb.append(version);
            sb.append(DELIMITER);
        }
        return sb.toString();
    }

    public String getMethodFullName() {
        return this.targetClass.getSimpleName() + '#' + method.getName();
    }
}
