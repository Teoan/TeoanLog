package io.github.teoan.log.core.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.teoan.log.core.annotation.TeoanLog;
import io.github.teoan.log.core.handle.LogHandle;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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
     * 配置切入点
     */
    @Pointcut("@annotation(io.github.teoan.log.core.annotation.TeoanLog)")
    public void teoanLog() {
    }


    @Around("teoanLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //拦截方法执行前
        long startTime = System.currentTimeMillis();
        //记录请求记录
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(attributes)) {
            request = attributes.getRequest();
        }
        //执行拦截方法
        Object result = joinPoint.proceed();
        for (LogHandle logHandle : logHandleList) {
            taskExecutor.execute(() -> {
                try {
                    logHandle.doAround(joinPoint, request, System.currentTimeMillis() - startTime, result);
                } catch (Throwable e) {
                    log.error("LogHandle Error,LogHandle name[{}]", logHandle.getClass().getName(), e);
                }
            });

        }
        return result;
    }


    @AfterThrowing(value = "teoanLog()", throwing = "throwable")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        for (LogHandle logHandle : logHandleList) {
            taskExecutor.execute(() -> {
                logHandle.doAfterThrowing(joinPoint, throwable);
            });
        }
    }

}
