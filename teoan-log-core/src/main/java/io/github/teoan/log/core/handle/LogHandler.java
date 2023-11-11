package io.github.teoan.log.core.handle;

import io.github.teoan.log.core.entity.AroundLog;
import io.github.teoan.log.core.entity.OrdinaryLog;
import io.github.teoan.log.core.entity.ThrowingLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;

/**
 * 日志处理入口
 *
 * @author Teoan
 * @since 2023/10/16 22:08
 */
@Component
@Slf4j
public class LogHandler {


    @Resource(name = "teoanLogTaskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Resource
    List<LogHandle> logHandleList;

    /**
     * 应用名称
     */
    @Value(value = "${spring.application.name}")
    String appName;


    /**
     * 处理环绕通知
     */
    public void doAround(AroundLog aroundLog) {
        doLogHandle(logHandle -> {
            try {
                logHandle.doAround(aroundLog);
            } catch (Throwable e) {
                log.error("LogHandle Error,LogHandle name[{}]", logHandle.getClass().getName(), e);
            }
        });
    }

    /**
     * 处理异常返回通知
     */
    public void doAfterThrowing(ThrowingLog throwingLog) {
        doLogHandle(logHandle -> logHandle.doAfterThrowing(throwingLog));
    }


    /**
     * 批量保存普通日志
     */
    public void saveOrdinaryLog(List<OrdinaryLog> ordinaryLogList) {
        ordinaryLogList.forEach(ordinaryLog -> ordinaryLog.setOperSource(appName));
        doLogHandle(logHandle -> logHandle.saveOrdinaryLog(ordinaryLogList));
    }


    /**
     * 统一抽象处理方法
     */
    private void doLogHandle(Consumer<LogHandle> consumer) {
        for (LogHandle logHandle : logHandleList) {
            taskExecutor.execute(() -> {
                try {
                    consumer.accept(logHandle);
                } catch (Throwable e) {
                    log.error("LogHandle Error,LogHandle name[{}]", logHandle.getClass().getName(), e);
                }
            });
        }
    }
}
