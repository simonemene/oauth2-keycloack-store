package com.store.security.store_security.service;

import com.store.security.store_security.entity.StockArticleEntity;
import com.store.security.store_security.exceptions.StockException;
import com.store.security.store_security.mapper.ArticleMapper;
import com.store.security.store_security.mapper.StockArticleMapper;
import com.store.security.store_security.mapper.StockMapper;
import com.store.security.store_security.repository.ArticleRepository;
import com.store.security.store_security.repository.StockArticleRepository;
import com.store.security.store_security.repository.StockRepository;
import com.store.security.store_security.service.impl.StockService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class StockServiceUnitTest {

	@InjectMocks
	private StockService stockService;

	@Mock
	private StockArticleRepository stockArticleRepository;

	@Mock
	private StockRepository stockRepository;

	@Mock
	private ArticleRepository articleRepository;

	@Mock
	private StockMapper stockMapper;

	@Mock
	private StockArticleMapper stockArticleMapper;

	@Mock
	private ArticleMapper articleMapper;

	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		stockService = new StockService(stockRepository, articleRepository, stockArticleRepository, stockMapper, stockArticleMapper, articleMapper);
	}


	@Test
	public void deleteArticleFailed()
	{
		//given
		Mockito.when(stockArticleRepository.findByArticle_Id(2L)).thenReturn(
				Optional.of(StockArticleEntity.builder().id(1L).quantity(101).build()));
		//when
		//then
		Assertions.assertThatThrownBy(() -> stockService.deleteArticle(2L))
				.isInstanceOf(StockException.class)
				.hasMessageContaining(String.format("[ARTICLE: %s] Article not deleted",2L));
	}

	@Test
	public void savedArticleQuantityFailed()
	{
		//given
		Mockito.when(stockArticleRepository.findByArticle_Id(2L))
				.thenReturn(Optional.ofNullable(
						StockArticleEntity.builder().id(2L).quantity(101).build()));
		Mockito.when(stockArticleRepository.save(Mockito.any(StockArticleEntity.class)))
				.thenReturn(StockArticleEntity.builder().id(0L).quantity(101).build());
		//when
		//then
		Assertions.assertThatThrownBy(() -> stockService.saveArticleQuantity(2L,11))
				.isInstanceOf(StockException.class)
				.hasMessageContaining(String.format("[ARTICLE: %s QUANTITY: %s] Stock not updated",2L,11));
	}

	@Test
	public void decrementArticleFailed()
	{
		//given
		Mockito.when(stockArticleRepository.findByArticle_Id(2L))
				.thenReturn(Optional.ofNullable(
						StockArticleEntity.builder().id(2L).quantity(101).build()));
		Mockito.when(stockArticleRepository.save(Mockito.any(StockArticleEntity.class)))
				.thenReturn(StockArticleEntity.builder().id(0L).quantity(101).build());
		//when
		//then
		Assertions.assertThatThrownBy(() -> stockService.decrementArticle(2L,11))
				.isInstanceOf(StockException.class)
				.hasMessageContaining(String.format("[ARTICLE: %s QUANTITY: %s] Stock not updated",2L,11));
	}
}
