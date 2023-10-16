package io.github.teoan.log.core.handle;

import io.github.teoan.log.core.entity.AroundLog;
import io.github.teoan.log.core.entity.ThrowingLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志处理入口
 * @author Teoan
 * @since 2023/10/16 22:08
 */
@Component
@Slf4j
public class LogHandler {


    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @Resource
    List<LogHandle> logHandleList;



    /**
     * 处理环绕通知
     */
   public void doAround(AroundLog aroundLog){
       for (LogHandle logHandle : logHandleList) {
           taskExecutor.execute(() -> {
               try {
                   logHandle.doAround(aroundLog);
               } catch (Throwable e) {
                   log.error("LogHandle Error,LogHandle name[{}]", logHandle.getClass().getName(), e);
               }
           });

       }
    }

    /**
     * 处理异常返回通知
     */
    public void doAfterThrowing(ThrowingLog throwingLog){
        for (LogHandle logHandle : logHandleList) {
            taskExecutor.execute(() -> {
                logHandle.doAfterThrowing(throwingLog);
            });
        }
    }


}
