package com.store.security.store_security.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AllArticleOrderDto {

	private ArticleDto articleDto;

	@Min(value = 1, message = "Quantity for article invalid")
	private Integer quantity;
}
