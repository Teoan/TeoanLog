package io.github.teoan.log.samples.controller;

import io.github.teoan.log.core.annotation.TeoanLog;
import io.github.teoan.log.core.enums.LogSeverity;
import io.github.teoan.log.samples.dto.TeoanLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Teoan
 * @since 2023/9/12 22:39
 */
@RestController
@Slf4j
public class TeoanLogController {


    @PostMapping("/testTeoanLog")
    @TeoanLog(operName = "测试", severity = LogSeverity.INFO, description = "测试日志打印")
    public TeoanLogDTO testTeoanLog(@RequestBody TeoanLogDTO teoanLogDTO) {
        return teoanLogDTO;
    }


    @PostMapping("/testTeoanException")
    @TeoanLog(operSource = "teoanLog", operName = "测试异常", severity = LogSeverity.INFO, description = "测试异常")
    public TeoanLogDTO testTeoanException(@RequestBody TeoanLogDTO teoanLogDTO) {
        throw new RuntimeException("测试异常");
    }



    @GetMapping("/testOrdinaryLog")
    @TeoanLog(operSource = "teoanLog", operName = "测试普通日志", severity = LogSeverity.INFO, description = "测试普通日志")
    public void testOrdinaryLog() {
        log.info("测试普通日志");
    }
}
