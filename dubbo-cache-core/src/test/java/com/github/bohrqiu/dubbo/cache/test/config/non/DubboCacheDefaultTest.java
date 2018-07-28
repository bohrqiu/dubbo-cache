package com.github.bohrqiu.dubbo.cache.test.config.non;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Request;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Response;
import com.github.bohrqiu.dubbo.cache.test.dubbo.service.DemoService;
import com.github.bohrqiu.dubbo.cache.test.util.LogOutputRule;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试默认参数配置的情况
 *
 * @author qiuboboy@qq.com
 * @date 2018-07-28 15:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = DefaultConfig.class)
public class DubboCacheDefaultTest {
    @Rule
    public LogOutputRule capture = new LogOutputRule();

    @Reference(version = "2.5.8", url = "dubbo://127.0.0.1:12345")
    private DemoService demoService;
    @Reference(version = "2.5.8", url = "dubbo://127.0.0.1:12345", group = "test")
    private DemoService groupDemoService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Before
    public void setUp() throws Exception {
        redisTemplate.execute(new RedisCallback() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return null;
            }
        });
    }

    @Test
    public void testParamName() {
        Request request = new Request();
        request.setName("x");
        request.setAge(1);
        Response response = demoService.sayHello(request);
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        String logsContent = capture.getLogsContent();
        //use jdk6,parameter name not support.
        Assertions.assertThat(logsContent).contains("spel parse failure");
        Assertions.assertThat(logsContent).contains("key[null] is not support");
        Assertions.assertThat(logsContent).contains("in sayHello");
        capture.clear();

        response = demoService.sayHello(request);
        logsContent = capture.getLogsContent();
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        Assertions.assertThat(logsContent).contains("spel parse failure");
        Assertions.assertThat(logsContent).contains("in sayHello");
    }

    @Test
    public void testParamIndex() {
        Request request = new Request();
        request.setName("x");
        request.setAge(1);
        Response response = demoService.sayHello1(request);
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        String logsContent = capture.getLogsContent();
        Assertions.assertThat(logsContent).contains("in sayHello1");
        capture.clear();

        response = demoService.sayHello1(request);
        logsContent = capture.getLogsContent();
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        Assertions.assertThat(logsContent).contains("@DubboCache hit");
        Assertions.assertThat(logsContent).contains("cachePrefix=dubbo-cache-test:");
        Assertions.assertThat(logsContent).contains("elEvaluatedKey=1");
    }

    @Test
    public void testParamIndex_noCacheName() {
        Request request = new Request();
        request.setName("x");
        request.setAge(1);
        Response response = demoService.sayHello3(request);
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        String logsContent = capture.getLogsContent();
        Assertions.assertThat(logsContent).contains("in sayHello3");
        capture.clear();

        response = demoService.sayHello3(request);
        logsContent = capture.getLogsContent();
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        Assertions.assertThat(logsContent).contains("@DubboCache hit");
        Assertions.assertThat(logsContent).contains("cachePrefix=,");
        Assertions.assertThat(logsContent).contains("elEvaluatedKey=1");
    }
    @Test
    public void testParamIndex_noCacheKey() {
        Request request = new Request();
        request.setName("x");
        request.setAge(1);
        Response response = demoService.sayHello4(request);
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        String logsContent = capture.getLogsContent();
        Assertions.assertThat(logsContent).contains("key[null] is not support by");
        Assertions.assertThat(logsContent).contains("in sayHello4");
        capture.clear();

        response = demoService.sayHello4(request);
        logsContent = capture.getLogsContent();
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        Assertions.assertThat(logsContent).contains("key[null] is not support by");
        Assertions.assertThat(logsContent).contains("in sayHello4");
    }

    @Test
    public void testNonCache() {
        Request request = new Request();
        request.setName("x");
        request.setAge(1);
        Response response = demoService.sayHello2(request);
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        String logsContent = capture.getLogsContent();
        Assertions.assertThat(logsContent).contains("in sayHello2");
    }

    @Test
    public void testGroupServiceParamIndex() {
        Request request = new Request();
        request.setName("x");
        request.setAge(1);
        Response response = groupDemoService.sayHello1(request);
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        String logsContent = capture.getLogsContent();
        Assertions.assertThat(logsContent).contains("group:test,in sayHello1");
        capture.clear();

        response = groupDemoService.sayHello1(request);
        logsContent = capture.getLogsContent();
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        Assertions.assertThat(logsContent).contains("@DubboCache hit");
        Assertions.assertThat(logsContent).contains("cachePrefix=dubbo-cache-test:");
        Assertions.assertThat(logsContent).contains("elEvaluatedKey=1");
    }


}
