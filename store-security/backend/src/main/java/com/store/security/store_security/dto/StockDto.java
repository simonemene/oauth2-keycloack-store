package com.store.security.store_security.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StockDto {

	private Long id;

	private List<StockArticleDto> stockArticles;
}
