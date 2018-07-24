package me.bohr.dubbo.cache.validator;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import me.bohr.dubbo.cache.CacheValueValidator;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-24 19:09
 */
public class TestCacheValueValidator implements CacheValueValidator {
    @Override
    public boolean isValid(URL url, Invocation invocation, Object value) {
        System.out.println("in TestCacheValueValidator");
        return true;
    }
}
