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

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.bohrqiu.dubbo.cache.CacheMeta;
import com.github.bohrqiu.dubbo.cache.KeyGenerator;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @date 2018-07-23 21:52
 */
public class SpelKeyGenerator implements KeyGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SpelKeyGenerator.class);

    private SpelExpressionParser spelExpressionParser;
    private ParameterNameDiscoverer paramDiscoverer;
    private ConcurrentMap<Method, ExpressionValueHolder> cache;

    public SpelKeyGenerator() {
        SpelParserConfiguration spelParserConfiguration =
                new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, null);
        spelExpressionParser = new SpelExpressionParser(spelParserConfiguration);
        paramDiscoverer = new DefaultParameterNameDiscoverer();
        cache = new ConcurrentHashMap<Method, ExpressionValueHolder>();
    }

    @Override
    public Object key(CacheMeta cacheMeta, Object[] args) {
        try {
            if (StringUtils.isBlank(cacheMeta.getDubboCache().key())) {
                return null;
            }
            ExpressionValueHolder expressionValueHolder = cache.get(cacheMeta.getMethod());
            if (expressionValueHolder == null) {
                Expression expression =
                        spelExpressionParser.parseExpression(cacheMeta.getDubboCache().key());
                String[] parameterNames = paramDiscoverer.getParameterNames(cacheMeta.getMethod());
                expressionValueHolder = new ExpressionValueHolder(expression, parameterNames);
                cache.putIfAbsent(cacheMeta.getMethod(), expressionValueHolder);
            }
            CacheEvaluationContext context =
                    new CacheEvaluationContext(
                            cacheMeta, cacheMeta.getMethod(), args, expressionValueHolder.paramNames);
            return expressionValueHolder.expression.getValue(context);
        } catch (Exception e) {
            logger.warn("spel parse failure", e);
        }
        return null;
    }

    private static class ExpressionValueHolder {
        private Expression expression;
        private String[] paramNames;

        public ExpressionValueHolder(Expression expression, String[] paramNames) {
            this.expression = expression;
            this.paramNames = paramNames;
        }
    }
}
