package io.github.teoan.log.core.aspect;

import io.github.teoan.log.core.annotation.TeoanLog;
import io.github.teoan.log.core.entity.AroundLog;
import io.github.teoan.log.core.entity.BaseLog;
import io.github.teoan.log.core.entity.ThrowingLog;
import io.github.teoan.log.core.handle.LogHandle;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Teoan
 * @description 日志切面处理
 * @date 2021/6/25 16:06
 */
@Component
@Aspect
@Slf4j
public class TeoanLogAspect {
    private HttpServletRequest request = null;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @Resource
    List<LogHandle> logHandleList;

    /**
     * 应用名称
     */
    @Value(value = "${spring.application.name}")
    String appName;


    /**
     * 配置切入点
     */
    @Pointcut("@annotation(io.github.teoan.log.core.annotation.TeoanLog)")
    public void teoanLog() {
    }


    @Around("teoanLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //拦截方法执行前
        StopWatch stopWatch = new StopWatch();
        //执行拦截方法 记录耗时
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        AroundLog aroundLog = buildBaseLog(joinPoint, new AroundLog());
        aroundLog.setExecTime(stopWatch.getTotalTimeMillis());
        aroundLog.setResult(result);
        for (LogHandle logHandle : logHandleList) {
            taskExecutor.execute(() -> {
                try {
                    logHandle.doAround(aroundLog);
                } catch (Throwable e) {
                    log.error("LogHandle Error,LogHandle name[{}]", logHandle.getClass().getName(), e);
                }
            });

        }
        return result;
    }


    @AfterThrowing(value = "teoanLog()", throwing = "throwable")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        ThrowingLog throwingLog = buildBaseLog(joinPoint, new ThrowingLog());
        throwingLog.setThrowable(throwable);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String stackTraceString = sw.toString();
        throwingLog.setStackTraceString(stackTraceString);
        for (LogHandle logHandle : logHandleList) {
            taskExecutor.execute(() -> {
                logHandle.doAfterThrowing(throwingLog);
            });
        }
    }


    /**
     * 构建日志对象
     */
    private <T extends BaseLog> T buildBaseLog(JoinPoint joinPoint, T t) {
        //记录请求记录
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(attributes)) {
            request = attributes.getRequest();
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取接入点方法
        Method method = methodSignature.getMethod();
        TeoanLog loggerAnnotation = method.getAnnotation(TeoanLog.class);
        t.setSeverity(loggerAnnotation.severity());
        t.setOperSource(ObjectUtils.isEmpty(loggerAnnotation.operSource())? appName : loggerAnnotation.operSource());
        t.setOperName(loggerAnnotation.operName());
        t.setDescription(loggerAnnotation.description());
        t.setIp(ObjectUtils.isEmpty(request) ? "" : request.getRemoteAddr());
        t.setUrl(ObjectUtils.isEmpty(request) ? "" : request.getRequestURL().toString());
        t.setHttpMethod(ObjectUtils.isEmpty(request) ? "" : request.getMethod());
        t.setClassName(methodSignature.getDeclaringTypeName());
        t.setMethod(methodSignature.getName());
        t.setArgs(joinPoint.getArgs());
        t.setDateTime(LocalDateTime.now());
        return t;

    }

}
