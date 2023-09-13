package io.github.teoan.log.samples.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 测试DTO
 * @author Teoan
 * @since 2023/9/12 22:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeoanLogDTO {


    private String testString;

    private Integer testInteger;

    private Map<String, Object> testMap;

    private List<Object> testList;

    private LocalDate testTime;

}
