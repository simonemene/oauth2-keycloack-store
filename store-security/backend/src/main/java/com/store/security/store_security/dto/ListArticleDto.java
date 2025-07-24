package com.store.security.store_security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(
		name = "List Article",
		description ="Schema to list article"
)
@Data
@Builder
public class ListArticleDto {

	@Schema(
			description = "List of article", example = "[" +
			"{\"id\":1,\"name\":\"Article 1\",\"description\":\"Description 1\",\"price\":10.00,\"tmstInsert\":\"2022-01-01T00:00:00\"}" +
			"]"
	)
	private List<ArticleDto> articles;
}
