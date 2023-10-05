package io.github.teoan.log.core.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.teoan.log.core.entity.BaseLog;
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

        printBaseLog(aroundLog);
        // 打印请求入参
        log.info("Request Args    : {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(aroundLog.getArgs()));
        // 打印出参
        log.info("Response Args   : {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(aroundLog.getResult()));
        // 执行耗时
        log.info("Time-Consuming  : {} ms", aroundLog.getExecTime());
    }

    /**
     * 处理异常返回通知
     *
     */
    @Override
    public void doAfterThrowing(ThrowingLog throwingLog) {
        printBaseLog(throwingLog);
        log.error("Exception Messages      : {}", throwingLog.getThrowable().getMessage());
        log.error("Exception      : ", throwingLog.getThrowable());
    }



    private void printBaseLog(BaseLog baseLog){
        // 打印请求相关参数
        // 打印日志级别
        log.info("SEVERITY        : {}", baseLog.getSeverity());
        // 打印日志操作模块
        log.info("OPER SOURCE     : {}", baseLog.getOperSource());
        // 打印日志操作名称
        log.info("OPER NAME       : {}", baseLog.getOperName());
        // 打印日志操作描述
        log.info("DESCRIPTION     : {}", baseLog.getDescription());
        // 打印请求 url
        log.info("URL             : {}", baseLog.getUrl());
        // 打印 Http method
        log.info("HTTP Method     : {}", baseLog.getHttpMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method    : {}.{}", baseLog.getClassName(), baseLog.getMethod());
        // 打印请求的 IP
        log.info("IP              : {}", baseLog.getIp());
    }
}
