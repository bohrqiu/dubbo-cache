# @DubboCache

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
	<version>1.0</version>
</dependency>
```       
### 在服务消费者模块中依赖`dubbo-cache-core`

```xml
<dependency>
	<groupId>com.github.bohrqiu.dubbo</groupId>
	<artifactId>dubbo-cache-core</artifactId>
	<version>1.0</version>
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
	
上面的注解和`@org.springframework.cache.annotation.Cacheable(value = "test", key = "#order.playload")`等价。
	
对于dubbo服务消费者，只需要更新jar包即可，由服务提供者来觉得接口是否需要缓存，和缓存的控制。

**注意:** 参数名读取依赖java8编译参数`-parameters`,上面的例子也可以通过`p0`来引用第一个接口入参。

## key生成

key生成策略和`Cacheable`一致，上面的例子中cache key由三部分组成:cacheName,分隔符(:),and spring el表达式结果.

如果请求参数order中`playload`属性值为dubbo，最终key为：`test:dubbo`

## 控制缓存

`@DubboCache`提供了消费者可优先使用缓存，**缓存的一致性由服务提供方负责**，当服务提供方使用此注解后，所有的服务消费者都会使用此缓存。

控制缓存分为两种情况：

1. 缓存一致性要求不高，可以通过`DubboCache#expire`设置过期时间，默认为5分钟。
2. 缓存一致性要求高，服务提供方通过`redisTemplate`或者`org.springframework.cache.annotation.CacheEvict`控制缓存。

## 如何扩展

### 扩展`CacheKeyValidator`

`CacheKeyValidator`可以实现对特定的key、url缓存或者不缓存。

#### 实现`CacheKeyValidator`
		
```java		
package com.github.bohrqiu.dubbo.cache.validator;
public class TestCacheKeyValidator implements CacheKeyValidator {
		    @Override
	public boolean isValid(URL url, Invocation invocation, Object key) {
		System.out.println("in TestCacheKeyValidator");
		return true;
	}
}
```

#### 配置扩展文件

在classpath下创建`META-INF/dubbo/com.github.bohrqiu.dubbo.cache.CacheKeyValidator`文件,内容为：

	test=com.github.bohrqiu.dubbo.cache.validator.TestCacheValueValidator
	
#### 设置服务url使自定义扩展生效

```xml
<dubbo:application name="dubbo-annotation-consumer">
	<dubbo:parameter key="cacheKeyValidator" value="test"/>
</dubbo:application>

```

### 扩展`CacheKeyValidator`

扩展方式和上面的类似，更多参考dubbo扩展机制。	


### F.A.Q

#### 缓存不生效的场景包括哪些？

##### 服务提供者group不为空时，此组件不生效。

为了使用方便，此组件提供服务接口上的注解，此注解不能感知group，而且到多个group存在时，缓存控制比较麻烦。比如有多个group，缓存key完全由dubbo服务限定名生成，那么key=service:group:version:paramkey,为了清除key会很麻烦。