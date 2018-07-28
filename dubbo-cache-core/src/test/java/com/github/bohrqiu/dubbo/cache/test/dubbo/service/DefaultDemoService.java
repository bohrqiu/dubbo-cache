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
package com.github.bohrqiu.dubbo.cache.test.dubbo.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Request;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link DemoService} implementation
 *
 * @since 2.5.8
 */
@Service(
        version = "2.5.8",
        protocol = "dubbo",
        registry = "my-registry"
)
public class DefaultDemoService implements DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultDemoService.class);

    public Response sayHello(Request request) {
        logger.info("in sayHello");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }

    @Override
    public Response sayHello1(Request request) {
        logger.info("in sayHello1");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }

    @Override
    public Response sayHello2(Request request) {
        logger.info("in sayHello2");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }
    @Override
    public Response sayHello3(Request request) {
        logger.info("in sayHello3");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }
    @Override
    public Response sayHello4(Request request) {
        logger.info("in sayHello4");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }
}
