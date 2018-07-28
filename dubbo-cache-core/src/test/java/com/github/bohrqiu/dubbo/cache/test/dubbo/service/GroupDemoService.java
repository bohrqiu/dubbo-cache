package com.github.bohrqiu.dubbo.cache.test.dubbo.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Request;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-28 17:47
 */
@Service(
        version = "2.5.8",
        protocol = "dubbo",
        registry = "my-registry",
        group = "test"
)
public class GroupDemoService implements DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultDemoService.class);

    public Response sayHello(Request request) {
        logger.info("group:test,in sayHello");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }

    @Override
    public Response sayHello1(Request request) {
        logger.info("group:test,in sayHello1");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }

    @Override
    public Response sayHello2(Request request) {
        logger.info("group:test,in sayHello2");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }
    @Override
    public Response sayHello3(Request request) {
        logger.info("group:test,in sayHello3");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }
    @Override
    public Response sayHello4(Request request) {
        logger.info("group:test,in sayHello4");
        Response response = new Response();
        response.setAge(request.getAge());
        response.setName(request.getName());
        return response;
    }
}
