package io.github.teoan.log.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 存储到es的对象
 * @author Teoan
 * @since 2023/9/19 21:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document(indexName = "throwing_log")
public class ThrowingLogDO extends BaseLogDO implements Serializable {

    /**
     * 主建
     */
    @Id
    private String id;


    /**
     *  异常栈
     */
    @Field(type = FieldType.Text)
    private String stackTraceString;
}
