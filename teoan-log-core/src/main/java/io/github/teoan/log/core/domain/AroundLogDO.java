package io.github.teoan.log.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 存储到es的对象
 *
 * @author Teoan
 * @since 2023/9/19 21:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document(indexName = "around_log")
public class AroundLogDO extends BaseLogDO implements Serializable, MongoDBDO {



    /**
     * 方法执行结果
     */
    @Field(type = FieldType.Object)
    private Object result;

    /**
     * 执行耗时
     */
    @Field(type = FieldType.Long)
    private Long execTime;
}
