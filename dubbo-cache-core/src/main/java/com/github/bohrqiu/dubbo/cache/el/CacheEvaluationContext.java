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
package com.github.bohrqiu.dubbo.cache.el;

import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @date 2018-07-23 23:43
 */
class CacheEvaluationContext extends StandardEvaluationContext {

    private final Method method;

    private final Object[] arguments;

    private final String[] paramNames;


    CacheEvaluationContext(Object rootObject, Method method, Object[] arguments,
                           String[] paramNames) {
        super(rootObject);
        this.method = method;
        this.arguments = arguments;
        this.paramNames = paramNames;
        LoadArguments();
    }

    private void LoadArguments() {
        int paramCount = this.method.getParameterTypes().length;
        for (int i = 0; i < paramCount; i++) {
            Object value = this.arguments[i];
            setVariable("p" + i, value);
            if (paramNames != null) {
                setVariable(paramNames[i], value);
            }
        }
    }

}
