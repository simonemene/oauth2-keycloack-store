package com.store.security.store_security.mapper;

import com.store.security.store_security.dto.StockArticleDto;
import com.store.security.store_security.dto.StockDto;
import com.store.security.store_security.entity.StockArticleEntity;
import com.store.security.store_security.entity.StockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {ArticleMapper.class})
public interface StockArticleMapper {

    @Mapping(target = "stock", ignore = true)
    StockArticleEntity toEntity(StockArticleDto stockDto);

    @Mapping(target = "stock.id", ignore = true)
    StockArticleDto toDto(StockArticleEntity stockEntity);

    default Long mapStockToId(StockEntity stock) {
        return stock == null ? null : stock.getId();
    }
}
