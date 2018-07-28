# @DubboCache

[![Build Status](https://travis-ci.org/bohrqiu/dubbo-cache.svg?branch=master)](https://travis-ci.org/bohrqiu/dubbo-cache)
[![codecov](https://codecov.io/gh/bohrqiu/dubbo-cache/branch/master/graph/badge.svg)](https://codecov.io/gh/bohrqiu/dubbo-cache)
[![maven](https://img.shields.io/maven-central/v/com.github.bohrqiu.dubbo/dubbo-cache.svg)](https://search.maven.org/#search%7Cga%7C1%7Cdubbo-cache)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

提供dubbo消费者直接使用缓存的能力，当缓存不存在时，再访问远程dubbo服务。

相对于dubbo默认的缓存机制，此项目具有如下优点：

1. 缓存key生成灵活，和spring 声明式缓存一致，采用spring el表达式
2. 可以扩展`CacheKeyValidator`接口，实现键缓存策略
3. 可以扩展`CacheValueValidator`接口，实现值缓存策略
4. 由于采用分布式缓存，服务提供端可以灵活控制缓存

目前仅提供redis实现，依赖`spring-data-redis`.

## 使用`@DubboCache`

### 在服务接口模块中依赖`dubbo-cache-common`
	
```xml
<dependency>
	<groupId>com.github.bohrqiu.dubbo</groupId>
	<artifactId>dubbo-cache-common</artifactId>
	<version>1.1</version>
</dependency>
```
此依赖仅定义了`@DubboCache`注解。
      
### 在服务消费者模块中依赖`dubbo-cache-core`

```xml
<dependency>
	<groupId>com.github.bohrqiu.dubbo</groupId>
	<artifactId>dubbo-cache-core</artifactId>
	<version>1.1</version>
</dependency>
```  

### 在服务接口上添加`@DubboCache`注解

```java
public interface CacheableService {
	@DubboCache(cacheName = "test",key = "#order.playload")
	SingleValueResult<String> echo(SingleValueOrder<String> order);
}
```

如上所示:`cacheName=test`,`key`为第一个参数的playload字段，缓存有效期默认5分钟。
	
上面的注解和`@org.springframework.cache.annotation.Cacheable(value = "test", key = "#order.playload")`生成的key一致。
	
对于dubbo服务消费者，只需要更新jar包即可，由服务提供者来觉得接口是否需要缓存，和缓存的控制。

**注意:** 参数名读取依赖java8编译参数`-parameters`,上面的例子也可以通过`p0`来引用第一个接口入参。

## key生成

key生成策略和`Cacheable`一致，上面的例子中cache key由两部分组成:cacheName,spring el表达式结果，用`:`分隔.

如果请求参数order中`playload`属性值为dubbo，最终key为：`test:dubbo`

如果服务有多个版本或者group，需要对多个版本和group分别设置缓存，可以设置参数：

    cachePrefixContainGroup=true
    cachePrefixContainVersion=true
    
如果两个参数都设置，key由四部分组成：cacheName,group,version,spring el表达式结果,用`:`分隔，建议在provider端设置此配置。

## 控制缓存

`@DubboCache`提供了消费者可优先使用缓存，**缓存的一致性由服务提供方负责**，当服务提供方使用此注解后，所有的服务消费者都会使用此缓存。

控制缓存分为两种情况：

1. 缓存一致性要求不高，可以通过`DubboCache#expire`设置过期时间，默认为5分钟。
2. 缓存一致性要求高，服务提供方通过`redisTemplate`或者`org.springframework.cache.annotation.CacheEvict`控制缓存。

## 如何扩展

### 扩展`CacheKeyValidator`

`CacheKeyValidator`可以实现对特定的key、url缓存或者不缓存。默认策略为：key不为null可以缓存。

#### 实现`CacheKeyValidator`
		
```java	
package com.github.bohrqiu.dubbo.cache.validator;
public class TestCacheKeyValidator implements CacheKeyValidator {
    @Override
	public boolean isValid(URL url, Invocation invocation, CacheMeta cacheMeta, Object elEvaluatedKey) {
		System.out.println("in TestCacheKeyValidator");
		return true;
	}
}
```

#### 配置扩展文件

在classpath下创建`META-INF/dubbo/com.github.bohrqiu.dubbo.cache.CacheKeyValidator`文件,内容为：

	test=com.github.bohrqiu.dubbo.cache.validator.TestCacheValueValidator
	
#### 设置服务url使自定义扩展生效

可以通过在provider配置application时指定(更多配置方式，参考dubbo相关文档).

```xml
<dubbo:application name="dubbo-cache-test">
	<dubbo:parameter key="cacheKeyValidator" value="test"/>
</dubbo:application>
```

### 扩展`CacheKeyValidator`

扩展方式和上面的类似，更多参考dubbo扩展机制。	默认策略为：value不为null可以缓存。
