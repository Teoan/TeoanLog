package io.github.teoan.log.core.handle;

import io.github.teoan.log.core.domain.AroundLogDO;
import io.github.teoan.log.core.domain.ThrowingLogDO;
import io.github.teoan.log.core.entity.AroundLog;
import io.github.teoan.log.core.entity.OrdinaryLog;
import io.github.teoan.log.core.entity.ThrowingLog;
import io.github.teoan.log.core.repository.es.EsAroundLogRepository;
import io.github.teoan.log.core.repository.es.EsOrdinaryLogRepository;
import io.github.teoan.log.core.repository.es.EsThrowingLogRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Teoan
 * @since 2023/9/19 22:02
 */
@Component
@ConditionalOnClass(ElasticsearchRestTemplate.class)
public class EsLogHandle extends LogHandle {


    @Resource
    EsAroundLogRepository aroundLogRepository;

    @Resource
    EsThrowingLogRepository throwingLogRepository;

    @Resource
    EsOrdinaryLogRepository ordinaryLogRepository;

    /**
     * 处理环绕通知
     *
     * @param aroundLog
     */
    @Override
    public void doAround(AroundLog aroundLog) {
        AroundLogDO aroundLogDO = new AroundLogDO();
        BeanUtils.copyProperties(aroundLog,aroundLogDO);
        aroundLogDO.setSeverity(aroundLog.getSeverity().getSeverity());
        aroundLogRepository.save(aroundLogDO);
    }

    /**
     * 处理异常返回通知
     *
     * @param throwingLog
     */
    @Override
    public void doAfterThrowing(ThrowingLog throwingLog) {
        ThrowingLogDO throwingLogDO =new ThrowingLogDO();
        BeanUtils.copyProperties(throwingLog,throwingLogDO);
        throwingLogDO.setSeverity(throwingLog.getSeverity().getSeverity());
        throwingLogRepository.save(throwingLogDO);
    }


    /**
     * 普通日志打印
     *
     */
    @Override
    void saveOrdinaryLog(List<OrdinaryLog> ordinaryLogList) {
        ordinaryLogRepository.saveAll(getOrdinaryLogDOList(ordinaryLogList));
    }
}
