package io.github.teoan.log.core.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志处理接口
 *
 * @author Teoan
 * @since 2023/9/14 21:31
 */
public interface LogHandle {


    /**
     * 处理环绕通知
     */
    void doAround(ProceedingJoinPoint joinPoint, HttpServletRequest request, Long execTime, Object result) throws Throwable;

    /**
     * 处理异常返回通知
     */
    void doAfterThrowing(JoinPoint joinPoint, Throwable throwable);
}
