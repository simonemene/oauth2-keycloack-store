package com.store.security.store_security.mapper;

import com.store.security.store_security.dto.ArticleDto;
import com.store.security.store_security.entity.ArticleEntity;
import com.store.security.store_security.entity.StockArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {StockArticleMapper.class})
public interface ArticleMapper {

	ArticleEntity toEntity(ArticleDto articleDto);

	ArticleDto toDto(ArticleEntity articleEntity);
}
