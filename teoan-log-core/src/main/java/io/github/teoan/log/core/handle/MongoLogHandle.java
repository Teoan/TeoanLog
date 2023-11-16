package io.github.teoan.log.core.handle;

import io.github.teoan.log.core.domain.AroundLogDO;
import io.github.teoan.log.core.domain.ThrowingLogDO;
import io.github.teoan.log.core.entity.AroundLog;
import io.github.teoan.log.core.entity.OrdinaryLog;
import io.github.teoan.log.core.entity.ThrowingLog;
import io.github.teoan.log.core.repository.mongo.MongoAroundLogRepository;
import io.github.teoan.log.core.repository.mongo.MongoOrdinaryLogRepository;
import io.github.teoan.log.core.repository.mongo.MongoThrowingLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author Teoan
 * @since 2023/9/19 22:02
 */
@Slf4j
public class MongoLogHandle extends LogHandle {


    @Resource
    MongoAroundLogRepository aroundLogRepository;

    @Resource
    MongoThrowingLogRepository throwingLogRepository;


    @Resource
    MongoOrdinaryLogRepository ordinaryLogRepository;


    @PostConstruct
    void init() {
        log.info("Teoan Log MongoLogHandle initialization completed.");
    }
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
    protected void saveOrdinaryLog(List<OrdinaryLog> ordinaryLogList) {
        ordinaryLogRepository.saveAll(getOrdinaryLogDOList(ordinaryLogList));
    }
}
