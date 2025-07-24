package com.store.security.store_security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ArticleDto {

    private Long id;

    private String name;

    private String description;

    @Digits(integer = 10,fraction = 2,message = "Price for article invalid")
    private BigDecimal price;

    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime tmstInsert;

}
