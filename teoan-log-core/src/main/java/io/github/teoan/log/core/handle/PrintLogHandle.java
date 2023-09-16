package io.github.teoan.log.core.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.teoan.log.core.entity.AroundLog;
import io.github.teoan.log.core.entity.ThrowingLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    public void doAround(AroundLog aroundLog) throws Throwable {
        // 打印请求相关参数
        log.info("========================================== Start ==========================================");
        // 打印日志级别
        log.info("SEVERITY        : {}", aroundLog.getSeverity());
        // 打印日志操作模块
        log.info("OPER SOURCE     : {}", aroundLog.getOperSource());
        // 打印日志操作名称
        log.info("OPER NAME       : {}", aroundLog.getOperName());
        // 打印日志操作描述
        log.info("DESCRIPTION     : {}", aroundLog.getDescription());
        // 打印请求 url
        log.info("URL             : {}", aroundLog.getUrl());
        // 打印 Http method
        log.info("HTTP Method     : {}", aroundLog.getHttpMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method    : {}.{}", aroundLog.getClassName(), aroundLog.getMethod());
        // 打印请求的 IP
        log.info("IP              : {}", aroundLog.getIp());
        // 打印请求入参
        log.info("Request Args    : {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(aroundLog.getArgs()));

        // 打印出参
        log.info("Response Args   : {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(aroundLog.getResult()));
        // 执行耗时
        log.info("Time-Consuming  : {} ms", aroundLog.getExecTime());
        log.info("=========================================== End ===========================================");
    }

    /**
     * 处理异常返回通知
     *
     */
    @Override
    public void doAfterThrowing(ThrowingLog throwingLog) {
        log.error("========================================== Error ===========================================");
        log.error("Exception Messages      : {}", throwingLog.getThrowable().getMessage());
        // 执行耗时
        log.error("=========================================== End ===========================================");
    }
}
