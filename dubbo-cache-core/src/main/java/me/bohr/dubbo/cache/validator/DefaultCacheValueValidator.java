package me.bohr.dubbo.cache.validator;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import me.bohr.dubbo.cache.CacheValueValidator;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-24 18:57
 */
public class DefaultCacheValueValidator implements CacheValueValidator {
    @Override
    public boolean isValid(URL url, Invocation invocation, Object value) {
        if (value == null) {
            return false;
        }
        return true;
    }
}
