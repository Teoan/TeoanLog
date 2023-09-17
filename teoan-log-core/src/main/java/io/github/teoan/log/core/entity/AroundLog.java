package io.github.teoan.log.core.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 日志对象
 * @author Teoan
 * @since 2023/9/16 09:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AroundLog extends BaseLog {

    /**
     * 方法执行结果
     */
    Object result;

    /**
     * 执行时间
     */
    Long execTime;

}
