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
package me.bohr.dubbo.cache;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @date 2018-07-23 21:52
 */
@Data
public class CacheMeta {
    private static final Logger logger = LoggerFactory.getLogger(CacheMeta.class);

    private Method method;
    private Class<?>[] parameterTypes;
    private DubboCache dubboCache;
    private Class targetClass;

    public static CacheMeta build(Invoker<?> invoker, Invocation inv) {
        try {
            String group = invoker.getUrl().getParameter(Constants.GROUP_KEY);
            if (group != null) {
                return null;
            }
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
            return cacheMeta;
        } catch (Exception e) {
            logger.warn("build cacheMeta FAILURE", e);
            return null;
        }
    }

    public String getMethodFullName() {
        return this.targetClass.getSimpleName() + '#' + method.getName();
    }
}
