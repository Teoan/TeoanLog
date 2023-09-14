package io.github.teoan.log.core.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.teoan.log.core.annotation.TeoanLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Teoan
 * @since 2023/9/14 21:58
 */
@Component
@Slf4j
public class PrintLogHandle implements LogHandle {


    @Resource
    ObjectMapper objectMapper;

    /**
     * 处理环绕通知
     *
     */
    @Override
    public void doAround(ProceedingJoinPoint joinPoint, HttpServletRequest request,Long execTime,Object result) throws Throwable {
        //记录请求记录
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(!ObjectUtils.isEmpty(attributes)){
            request = attributes.getRequest();
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取接入点方法
        Method method = methodSignature.getMethod();
        TeoanLog loggerAnnotation = method.getAnnotation(TeoanLog.class);
        // 打印请求相关参数
        log.info("========================================== Start ==========================================");
        // 打印日志级别
        log.info("SEVERITY        : {}", loggerAnnotation.severity().getSeverity());
        // 打印日志操作模块
        log.info("OPER SOURCE     : {}", loggerAnnotation.operSource());
        // 打印日志操作名称
        log.info("OPER NAME       : {}", loggerAnnotation.operName());
        // 打印日志操作描述
        log.info("DESCRIPTION     : {}", loggerAnnotation.description());
        // 打印请求 url
        log.info("URL             : {}", request.getRequestURL().toString());
        // 打印 Http method
        log.info("HTTP Method     : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method    : {}.{}", methodSignature.getDeclaringTypeName(), methodSignature.getName());
        // 打印请求的 IP
        log.info("IP              : {}", request.getRemoteAddr());
        // 打印请求入参
        log.info("Request Args    : {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(joinPoint.getArgs()));

        // 打印出参
        log.info("Response Args   : {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
        // 执行耗时
        log.info("Time-Consuming  : {} ms", execTime);
        log.info("=========================================== End ===========================================");
    }

    /**
     * 处理异常返回通知
     *
     */
    @Override
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        log.error("========================================== Error ===========================================");
        log.error("Exception Messages      : {}", throwable.getMessage());
        // 执行耗时
        log.error("=========================================== End ===========================================");
    }
}
