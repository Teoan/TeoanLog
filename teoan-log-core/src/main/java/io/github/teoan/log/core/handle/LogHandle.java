package io.github.teoan.log.core.handle;

import io.github.teoan.log.core.domain.OrdinaryLogDO;
import io.github.teoan.log.core.entity.AroundLog;
import io.github.teoan.log.core.entity.OrdinaryLog;
import io.github.teoan.log.core.entity.ThrowingLog;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 日志处理接口
 *
 * @author Teoan
 * @since 2023/9/14 21:31
 */
public abstract class LogHandle {


    /**
     * 处理环绕通知
     */
    protected void doAround(AroundLog aroundLog) throws Throwable {

    }

    /**
     * 处理异常返回通知
     */
    protected void doAfterThrowing(ThrowingLog throwingLog) {

    }

    /**
     * 普通日志打印
     */
    protected void saveOrdinaryLog(List<OrdinaryLog> ordinaryLogList) {

    }

    /**
     * 获取转换后的对象
     */
    protected List<OrdinaryLogDO> getOrdinaryLogDOList(List<OrdinaryLog> ordinaryLogList){
        return ordinaryLogList.stream().map(ordinaryLog -> {
            OrdinaryLogDO ordinaryLogDO = new OrdinaryLogDO();
            BeanUtils.copyProperties(ordinaryLog, ordinaryLogDO);
            return ordinaryLogDO;
        }).collect(Collectors.toList());
    }

}
