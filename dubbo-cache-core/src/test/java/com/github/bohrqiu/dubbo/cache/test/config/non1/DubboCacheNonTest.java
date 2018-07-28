package com.github.bohrqiu.dubbo.cache.test.config.non1;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Request;
import com.github.bohrqiu.dubbo.cache.test.dubbo.dto.Response;
import com.github.bohrqiu.dubbo.cache.test.dubbo.service.DemoService;
import com.github.bohrqiu.dubbo.cache.test.util.LogOutputRule;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试默认参数配置的情况
 *
 * @author qiuboboy@qq.com
 * @date 2018-07-28 15:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Non1Config.class)
public class DubboCacheNonTest {
    @Rule
    public LogOutputRule capture = new LogOutputRule();

    @Reference(version = "2.5.8", url = "dubbo://127.0.0.1:12345")
    private DemoService demoService;
    @Reference(version = "2.5.8", url = "dubbo://127.0.0.1:12345", group = "test")
    private DemoService groupDemoService;


    /**
     * spring boot 每次跑新的单元测试不会清理EnableAutoConfiguration指定的排除类
     */
    @Test
    @Ignore
    public void testParamIndex() {
        Request request = new Request();
        request.setName("x");
        request.setAge(1);
        Response response = demoService.sayHello1(request);
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        String logsContent = capture.getLogsContent();
        Assertions.assertThat(logsContent).contains("dubbo client cache FAILURE");
        Assertions.assertThat(logsContent).contains("in sayHello1");
        capture.clear();

        response = demoService.sayHello1(request);
        logsContent = capture.getLogsContent();
        Assertions.assertThat(response.getAge()).isEqualTo(request.getAge());
        Assertions.assertThat(response.getName()).isEqualTo(request.getName());
        Assertions.assertThat(logsContent).contains("in sayHello1");
    }

}
