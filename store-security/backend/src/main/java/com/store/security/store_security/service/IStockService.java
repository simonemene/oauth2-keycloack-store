package com.store.security.store_security.service;

import com.store.security.store_security.dto.AllStockDto;
import com.store.security.store_security.dto.ArticleDto;
import com.store.security.store_security.dto.StockArticleDto;
import com.store.security.store_security.dto.StockDto;
import com.store.security.store_security.entity.ArticleEntity;
import com.store.security.store_security.entity.StockArticleEntity;
import com.store.security.store_security.entity.StockEntity;

import java.util.List;

public interface IStockService {

	public StockDto getStock();

	public StockDto getStockByArticle(Long idArticle);

	public ArticleDto loadArticle(ArticleDto articleDto);

	public StockArticleDto decrementArticle(Long id, Integer quantity);

	public StockArticleDto saveArticleQuantity(Long id, Integer quantity);

	public boolean deleteArticle(Long id);

}
