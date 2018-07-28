package com.github.bohrqiu.dubbo.cache.test.dubbo.service;

import com.github.bohrqiu.dubbo.cache.DubboCache;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Request;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Response;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-24 17:45
 */
public interface DemoService {
    @DubboCache(cacheName = "dubbo-cache-test", key = "#request.name")
    Response sayHello(Request request);

    @DubboCache(cacheName = "dubbo-cache-test", key = "#p0.age.toString()")
    Response sayHello1(Request request);

    Response sayHello2(Request request);

}
