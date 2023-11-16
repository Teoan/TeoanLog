package io.github.teoan.log.samples.handle;

import io.github.teoan.log.core.entity.AroundLog;
import io.github.teoan.log.core.entity.ThrowingLog;
import io.github.teoan.log.core.handle.LogHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Teoan
 * @since 2023/11/16 21:58
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
     */
    @Override
    public void doAround(AroundLog aroundLog) throws Throwable {
        log.info("测试拓展日志打印：AroundLog：[{}]", aroundLog.toString());
    }

    /**
     * 处理异常返回通知
     */
    @Override
    public void doAfterThrowing(ThrowingLog throwingLog) {
        log.info("测试拓展日志打印：ThrowingLog：[{}]", throwingLog.toString());
    }
}
