package me.bohr.dubbo.cache.validator;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import me.bohr.dubbo.cache.CacheKeyValidator;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-24 19:07
 */
public class DefaultCacheKeyValidator implements CacheKeyValidator {
    @Override
    public boolean isValid(URL url, Invocation invocation, Object key) {
        return key != null;
    }
}
