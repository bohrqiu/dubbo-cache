package com.github.bohrqiu.dubbo.cache;

import com.github.bohrqiu.dubbo.cache.dto.Request;
import com.github.bohrqiu.dubbo.cache.dto.Response;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-24 17:45
 */
public interface DemoService {
    @DubboCache(cacheName = "dubbo-cache-test", key = "#request.name")
    Response sayHello(Request request);

    @DubboCache(cacheName = "dubbo-cache-test", key = "#p0.age")
    Response sayHello1(Request request);

}