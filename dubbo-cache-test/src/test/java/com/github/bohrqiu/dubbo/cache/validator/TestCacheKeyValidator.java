package com.github.bohrqiu.dubbo.cache.validator;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.github.bohrqiu.dubbo.cache.CacheKeyValidator;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-24 21:46
 */
public class TestCacheKeyValidator implements CacheKeyValidator {
    @Override
    public boolean isValid(URL url, Invocation invocation, Object key) {
        System.out.println("in TestCacheKeyValidator");
        return true;
    }
}
