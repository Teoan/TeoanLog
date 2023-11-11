package io.github.teoan.log.core.config;

import lombok.Data;

/**
 * @author Teoan
 * @since 2023/11/11 17:15
 */
@Data
public class EnabledProperties {

    /**
     * 是否开启es日志收集
     */
    Boolean elasticsearch = true;

    /**
     * 是否开启mongodb日志收集
     */
    Boolean mongodb = true;

    /**
     * 是否开启普通日志打印
     */
    Boolean print = true;


}
