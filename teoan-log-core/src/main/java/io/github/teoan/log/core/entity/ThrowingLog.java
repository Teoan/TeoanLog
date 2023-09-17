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
public class ThrowingLog extends BaseLog {

    /**
     *  异常
     */
    Throwable throwable;

}
