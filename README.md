# TeoanLog

一个简单的SpringBoot项目日志收集工具，可根据引入的Spring Data依赖类型将日志写入到es，或者是mongoDB中，同时也提供了相关拓展接口，可自定义拓展实现自己的日志写入逻辑。

## SpringBoot项目集成

### TeoanLog依赖

pom文件中引入TeoanLog的starter依赖。

```xml

<dependency>
    <groupId>io.github.teoan</groupId>
    <artifactId>teoan-log-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

根据需要引入对应es，或者mongoDB的相关依赖。注意：若两个依赖都存在则都会对应写入。若不想都写入则可以在配置文件中将其关闭。

### es的依赖

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

### mongoDB依赖

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

## 使用

### TeoanLog注解

在Controller层使用@TeoanLog注解，注解参数包括日志来源，日志级别，日志操作名称，日志操作描述,可根据自己的需要传入,如下：

```java

/**
 * @author Teoan
 * @since 2023/9/12 22:39
 */
@RestController
@Slf4j
public class TeoanLogController {


    @PostMapping("/testTeoanLog")
    @TeoanLog(operName = "测试", severity = LogSeverity.INFO, description = "测试日志打印")
    public TeoanLogDTO testTeoanLog(@RequestBody TeoanLogDTO teoanLogDTO) {
        return teoanLogDTO;
    }


    @PostMapping("/testTeoanException")
    @TeoanLog(operSource = "teoanLog", operName = "测试异常", severity = LogSeverity.INFO, description = "测试异常")
    public TeoanLogDTO testTeoanException(@RequestBody TeoanLogDTO teoanLogDTO) {
        throw new RuntimeException("测试异常");
    }


    @GetMapping("/testOrdinaryLog")
    @TeoanLog(operSource = "teoanLog", operName = "测试普通日志", severity = LogSeverity.INFO, description = "测试普通日志")
    public void testOrdinaryLog() {
        log.info("测试普通日志");
    }
}

```

#### 正常的请求

若请求过程中未发生任何异常，则收集的日志被注解函数的入参和出参。根据引入的依赖写入到对应的持久化中间件中。

##### Es中的记录

Es中会默认创建**around_log**索引，并写入如下记录。

```json
{
  "_index": "around_log",
  "_type": "_doc",
  "_id": "sqkHGosBknlQqFw9OlK_",
  "_score": 1,
  "_source": {
    "_class": "io.github.teoan.log.core.domain.AroundLogDO",
    "result": {
      "_class": "io.github.teoan.log.samples.dto.TeoanLogDTO",
      "testString": "testString",
      "testInteger": 1,
      "testMap": {
        "test": "test111111"
      },
      "testList": [
        "test",
        "test11111"
      ],
      "testTime": 1609430400000
    },
    "execTime": 0,
    "operSource": "teoanLog",
    "severity": "信息日志",
    "operName": "测试",
    "description": "测试日志打印",
    "url": "http://localhost:8080/testTeoanLog",
    "httpMethod": "POST",
    "className": "io.github.teoan.log.samples.controller.TeoanLogController",
    "method": "testTeoanLog",
    "ip": "127.0.0.1",
    "args": [
      {
        "_class": "io.github.teoan.log.samples.dto.TeoanLogDTO",
        "testString": "testString",
        "testInteger": 1,
        "testMap": {
          "test": "test111111"
        },
        "testList": [
          "test",
          "test11111"
        ],
        "testTime": 1609430400000
      }
    ],
    "dateTime": "2023-10-10T22:38:55.386"
  }
}
```

##### MongoDB中的记录

若引入的是MongoDB的依赖，则对应数据库中会创建集合**around_log**并写入如下数据：

```json
{
  "_id": ObjectId(
  "654a4ec9dea1a96201fb5b45"
  ),
  "result": {
    "testString": "testString",
    "testInteger": 1,
    "testMap": {
      "test": "test111111"
    },
    "testList": [
      "test",
      "test11111"
    ],
    "testTime": ISODate(
    "2020-12-31T16:00:00Z"
    ),
    "_class": "io.github.teoan.log.samples.dto.TeoanLogDTO"
  },
  "execTime": NumberLong(25),
  "operSource": "teoanLog",
  "severity": "信息日志",
  "operName": "测试",
  "description": "测试日志打印",
  "url": "http://localhost:8080/testTeoanLog",
  "httpMethod": "POST",
  "className": "io.github.teoan.log.samples.controller.TeoanLogController",
  "method": "testTeoanLog",
  "ip": "127.0.0.1",
  "args": [
    {
      "testString": "testString",
      "testInteger": 1,
      "testMap": {
        "test": "test111111"
      },
      "testList": [
        "test",
        "test11111"
      ],
      "testTime": ISODate(
      "2020-12-31T16:00:00Z"
      ),
      "_class": "io.github.teoan.log.samples.dto.TeoanLogDTO"
    }
  ],
  "dateTime": ISODate(
  "2023-11-07T14:50:29.312Z"
  ),
  "_class": "io.github.teoan.log.core.domain.AroundLogDO"
}
```

#### 异常的请求

若请求发生异常，则写入的是对应的异常信息。会记录完整的异常栈信息。

##### Es中的记录

异常的日志对应写入的是**throwing_log**索引，内容如下：

```json
{
  "_index": "throwing_log",
  "_type": "_doc",
  "_id": "d11GzosBfuhKCMIOEEvp",
  "_score": 1,
  "_source": {
    "_class": "io.github.teoan.log.core.domain.ThrowingLogDO",
    "stackTraceString": "java.lang.RuntimeException: 测试异常\n\tat io.github.teoan.log.samples.controller.TeoanLogController.testTeoanException(TeoanLogController.java:31)\n\tat io.github.teoan.log.samples.controller.TeoanLogController$$FastClassBySpringCGLIB$$5a040b2.invoke(<generated>)\n\tat org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:793)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\n\tat org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:64)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\n\tat org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:89)\n\tat io.github.teoan.log.core.aspect.TeoanLogAspect.doAround(TeoanLogAspect.java:66)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:634)\n\tat org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:624)\n\tat org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:72)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\n\tat org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\n\tat org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:708)\n\tat io.github.teoan.log.samples.controller.TeoanLogController$$EnhancerBySpringCGLIB$$fa12b640.testTeoanException(<generated>)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1072)\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:965)\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\n\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:555)\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:623)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:209)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:96)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:481)\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:130)\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:926)\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1791)\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n\tat java.lang.Thread.run(Thread.java:750)\n",
    "operSource": "teoanLog",
    "severity": "信息日志",
    "operName": "测试异常",
    "description": "测试异常",
    "url": "http://localhost:8080/testTeoanException",
    "httpMethod": "POST",
    "className": "io.github.teoan.log.samples.controller.TeoanLogController",
    "method": "testTeoanException",
    "ip": "127.0.0.1",
    "args": [
      {
        "_class": "io.github.teoan.log.samples.dto.TeoanLogDTO",
        "testString": "testString",
        "testInteger": 1,
        "testMap": {
          "test": "test"
        },
        "testList": [
          "test",
          "test"
        ],
        "testTime": 1609430400000
      }
    ],
    "dateTime": "2023-11-14T22:39:40.159"
  }
}
```

##### MongoDB中的记录

同样的MongoDB中对应的集合名称也是**throwing_log**，内容如下：

```json
{
  "_id": ObjectId(
  "655386ac869a45790915cc1f"
  ),
  "stackTraceString": "java.lang.RuntimeException: 测试异常\n\tat io.github.teoan.log.samples.controller.TeoanLogController.testTeoanException(TeoanLogController.java:31)\n\tat io.github.teoan.log.samples.controller.TeoanLogController$$FastClassBySpringCGLIB$$5a040b2.invoke(<generated>)\n\tat org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:793)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\n\tat org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:64)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\n\tat org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:89)\n\tat io.github.teoan.log.core.aspect.TeoanLogAspect.doAround(TeoanLogAspect.java:66)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:634)\n\tat org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:624)\n\tat org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:72)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\n\tat org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\n\tat org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:708)\n\tat io.github.teoan.log.samples.controller.TeoanLogController$$EnhancerBySpringCGLIB$$fa12b640.testTeoanException(<generated>)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1072)\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:965)\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\n\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:555)\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:623)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:209)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:96)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:481)\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:130)\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:926)\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1791)\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n\tat java.lang.Thread.run(Thread.java:750)\n",
  "operSource": "teoanLog",
  "severity": "信息日志",
  "operName": "测试异常",
  "description": "测试异常",
  "url": "http://localhost:8080/testTeoanException",
  "httpMethod": "POST",
  "className": "io.github.teoan.log.samples.controller.TeoanLogController",
  "method": "testTeoanException",
  "ip": "127.0.0.1",
  "args": [
    {
      "testString": "testString",
      "testInteger": 1,
      "testMap": {
        "test": "test"
      },
      "testList": [
        "test",
        "test"
      ],
      "testTime": ISODate(
      "2020-12-31T16:00:00Z"
      ),
      "_class": "io.github.teoan.log.samples.dto.TeoanLogDTO"
    }
  ],
  "dateTime": ISODate(
  "2023-11-14T14:39:40.159Z"
  ),
  "_class": "io.github.teoan.log.core.domain.ThrowingLogDO"
}
```

### 与LogBack结合使用

在项目的resources目录下新增logback.xml配置，配置中需增加LogAppender的配置，如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration debug="true">
    <appender name="TEOAN_LOG" class="io.github.teoan.log.core.appender.TeoanLogAppender"/>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>


    <root level="info">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="TEOAN_LOG" />
    </root>
</configuration>
```

如果能在你的项目代码中使用了类似以下的日志打印方式，那么就无需改动已有的代码，就能将日志也写入到对应的持久化中间件中去。

```java

/**
 * @author Teoan
 * @since 2023/9/12 22:39
 */
@RestController
@Slf4j
public class TeoanLogController {


    @GetMapping("/testOrdinaryLog")
    @TeoanLog(operSource = "teoanLog", operName = "测试普通日志", severity = LogSeverity.INFO, description = "测试普通日志")
    public void testOrdinaryLog() {
        log.info("测试普通日志");
    }
}

```

## 配置说明

TeoanLog提供了以下配置项，可以在SpringBoot项目的配置文件中进行配置。

```yml
teoan:
  log:
    batch: 20                #普通日志批处理数量
    enabled:
      elasticsearch: true    #是否启用ES日志处理
      mongodb: true          #是否启用mongoDB日志处理
      print: true            #是否启用TeoanLog注解普通日志打印
    task:
      core: 5                #日志处理线程数量
      max: 20                #日志处理线程最大数量
      queue: 500             #日志处理队列最大数量
```

## 拓展日志处理的实现

利用Spring容器，日志的拓展其实很简单，只需要在你的项目中， 继承LogHandle抽象类，然后重写方法即可。以PrintSamplesLogHandle为例：

```java

/**
 * @author Teoan
 * @since 2023/9/14 21:58
 */
@Slf4j
@Component
public class PrintSamplesLogHandle extends LogHandle {


    @PostConstruct
    void init() {
        log.info("Teoan Log PrintLogHandle initialization completed.");
    }

    /**
     * 处理环绕通知
     *
     */
    @Override
    public void doAround(AroundLog aroundLog) throws Throwable {
        log.info("测试拓展日志打印：AroundLog：[{}]",aroundLog.toString());
    }

    /**
     * 处理异常返回通知
     *
     */
    @Override
    public void doAfterThrowing(ThrowingLog throwingLog) {
        log.info("测试拓展日志打印：ThrowingLog：[{}]",throwingLog.toString());
    }
}

```