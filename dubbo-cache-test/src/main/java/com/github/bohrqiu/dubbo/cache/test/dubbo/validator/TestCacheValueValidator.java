package com.github.bohrqiu.dubbo.cache.test.dubbo.validator;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.github.bohrqiu.dubbo.cache.CacheValueValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-24 19:09
 */
public class TestCacheValueValidator implements CacheValueValidator {
    private static final Logger logger = LoggerFactory.getLogger(TestCacheKeyValidator.class);

    @Override
    public boolean isValid(URL url, Invocation invocation, Object value) {
        logger.info("in TestCacheValueValidator");
        return true;
    }
}
