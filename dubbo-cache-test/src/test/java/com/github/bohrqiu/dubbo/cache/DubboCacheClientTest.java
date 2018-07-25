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

import com.github.bohrqiu.dubbo.cache.consumer.ConsumerConfiguration;
import com.github.bohrqiu.dubbo.cache.dto.Request;
import com.github.bohrqiu.dubbo.cache.dto.Response;
import com.github.bohrqiu.dubbo.cache.provider.ProviderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

/**
 * Dubbo compatibility test on Spring 3.2.x
 *
 * @since 2.5.8
 */
public class DubboCacheClientTest {

    public static void main(String[] args) {


        ConfigurableApplicationContext consumer = startConsumer();

        ConsumerConfiguration consumerConfiguration = consumer.getBean(ConsumerConfiguration.class);

        DemoService demoService = consumerConfiguration.getDemoService();
        Request request = new Request();
        request.setName("x");
        request.setAge(1);
        Response value = demoService.sayHello(request);

        Assert.isTrue(value.getAge() == request.getAge());
        Assert.isTrue(value.getName().equals(request.getName()));
        value = demoService.sayHello(request);
        Assert.isTrue(value.getAge() == request.getAge());
        Assert.isTrue(value.getName().equals(request.getName()));

        value = demoService.sayHello1(request);
        Assert.isTrue(value.getAge() == request.getAge());
        Assert.isTrue(value.getName().equals(request.getName()));

        value = demoService.sayHello1(request);
        Assert.isTrue(value.getAge() == request.getAge());
        Assert.isTrue(value.getName().equals(request.getName()));

        System.out.println(value);

        consumer.close();

    }

    private static ConfigurableApplicationContext startupProvider() {

        ConfigurableApplicationContext context = startupApplicationContext(ProviderConfiguration.class);

        System.out.println("Startup Provider ...");

        return context;
    }

    private static ConfigurableApplicationContext startConsumer() {

        ConfigurableApplicationContext context = startupApplicationContext(ConsumerConfiguration.class);

        System.out.println("Startup Consumer ...");

        return context;

    }

    private static ConfigurableApplicationContext startupApplicationContext(Class<?>... annotatedClasses) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(annotatedClasses);
        context.refresh();
        return context;
    }

}