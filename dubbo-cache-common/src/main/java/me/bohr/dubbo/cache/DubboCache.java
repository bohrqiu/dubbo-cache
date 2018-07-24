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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this cache component provides the ability for dubbo consumers to access the cache directly,
 * when the cache does not exist and then access the remote dubbo service.
 * <p>
 *
 * <code>@DubboCache</code> used on dubbo service interface method,cause of service developers know if they need to use caching and control caching.
 *
 * <h2>1. cache key:<h2/>
 * <p>
 * key is evaluate by spring el.
 * <p>
 * cahce key composed of three parts:cacheName,delimiter(:),and spring el key.
 * for example:
 * <pre>
 *        {@code
 *       @literal @DubboCache(cacheName = "test", key = "#msg.dto")
 *              SingleResult<String> echo(SingleOrder<String> msg);
 *        }
 *        </pre>
 * <p>
 * <p>
 * SingleOrder class has dto field,when dto=dubbo, the final key is test:dubbo
 * <p>
 * and the key evaluate logic is equal to <code>@org.springframework.cache.annotation.Cacheable(value = "test", key = "#msg.dto")<code/>
 * <h2>2. cache control:<h2/>
 * <p>
 * 1. Cache consistency requirements are not high
 * <p>
 * use can use expire
 * <p>
 * 2. Cache consistency requirements are high
 * <p>
 * use cache client(such as RedisTemplate) or spring cache( org.springframework.cache.annotation.CacheEvict) control it .
 *
 * <p>
 *
 * @date 2018-07-23 21:42
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DubboCache {

    /**
     * cache name
     */
    String cacheName() default "";

    /**
     * spring el express, p0 means the first parameter and so on.
     * <p>
     * if use java 8, and compiler parameter set -parameters, can use parameter name
     * <p>
     * <p>
     * for example:
     * <pre>
     *   {@code
     *  @literal @DubboCache(cacheName = "test", key = "#msg.dto")
     *         SingleResult<String> echo(SingleOrder<String> msg);
     *   }
     *   </pre>
     * <p>
     * #msg and #p0  are all means  the parameter msg
     * <p>
     * <b>Note:</b> the final key in cache implements is cacheName:key.
     */
    String key();

    /**
     * cache expire time, unit:second
     */
    int expire() default 5 * 60;
}

