package io.github.teoan.log.core.handle;

import io.github.teoan.log.core.entity.AroundLog;
import io.github.teoan.log.core.entity.ThrowingLog;
import org.aspectj.lang.JoinPoint;

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
    void doAround(AroundLog aroundLog) throws Throwable;

    /**
     * 处理异常返回通知
     */
    void doAfterThrowing(ThrowingLog throwingLog);
}
