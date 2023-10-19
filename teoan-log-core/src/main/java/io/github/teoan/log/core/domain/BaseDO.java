package io.github.teoan.log.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

/**
 * @author Teoan
 * @since 2023/10/19 18:35
 */
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDO {
    /**
     * 主建
     */
    @Id
    private String id;
}
