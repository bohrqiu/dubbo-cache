package com.github.bohrqiu.dubbo.cache.test.dubbo.validator;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.github.bohrqiu.dubbo.cache.CacheKeyValidator;
import com.github.bohrqiu.dubbo.cache.CacheMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-24 21:46
 */
public class TestCacheKeyValidator implements CacheKeyValidator {
    private static final Logger logger = LoggerFactory.getLogger(TestCacheKeyValidator.class);

    @Override
    public boolean isValid(URL url, Invocation invocation, CacheMeta cacheMeta, Object elEvaluatedKey) {
        logger.info("in TestCacheKeyValidator");
        logger.info("url:{}", url.toString());
        return elEvaluatedKey != null;
    }
}
