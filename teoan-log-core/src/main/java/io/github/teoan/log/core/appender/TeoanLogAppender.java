package io.github.teoan.log.core.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import io.github.teoan.log.core.entity.OrdinaryLog;
import io.github.teoan.log.core.handle.LogHandler;
import io.github.teoan.log.core.utils.ApplicationContextProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * logback日志拓展
 *
 * @author Teoan
 * @since 2023/10/18 22:42
 */
public class TeoanLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private LogHandler logHandler;


    private List<OrdinaryLog> ordinaryLogList = new ArrayList<>();


    private Environment environment;

    /**
     * 批处理数量
     */
    private Integer batchSaveNumber = 5;

    @Override
    protected void append(ILoggingEvent loggingEvent) {
        init();
        OrdinaryLog ordinaryLog = new OrdinaryLog();
        BeanUtils.copyProperties(loggingEvent, ordinaryLog);
        ordinaryLog.setDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(loggingEvent.getTimeStamp())
                , ZoneId.systemDefault()));
        ordinaryLog.setLevel(loggingEvent.getLevel().levelStr);
        ordinaryLog.setMessage(loggingEvent.getFormattedMessage());
        // 批量保存
        ordinaryLogList.add(ordinaryLog);
        if (ObjectUtils.isEmpty(logHandler)) {
            return;
        }
        if (ordinaryLogList.size() >= batchSaveNumber) {
            logHandler.saveOrdinaryLog(new ArrayList<>(ordinaryLogList));
            ordinaryLogList.clear();
        }

    }


    /**
     * 初始化数据
     */
    private void init() {
        if (ObjectUtils.isEmpty(logHandler)) {
            logHandler = ApplicationContextProvider.getBean(LogHandler.class);
        }
        if (ObjectUtils.isEmpty(environment)) {
            environment = ApplicationContextProvider.getBean(Environment.class);
            if (!ObjectUtils.isEmpty(environment)) {
                batchSaveNumber = environment.getProperty("teoan.log.batch", Integer.class, 5);
            }
        }

    }
}