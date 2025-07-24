package com.store.security.store_security.mapper;

import com.store.security.store_security.dto.StockDto;
import com.store.security.store_security.entity.StockEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = StockArticleMapper.class)
public interface StockMapper {

	StockEntity toEntity(StockDto stockDto);

	StockDto toDto(StockEntity stockEntity);
}
