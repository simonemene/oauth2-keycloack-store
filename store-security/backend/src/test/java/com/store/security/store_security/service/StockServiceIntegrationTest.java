package com.store.security.store_security.service;

import com.store.security.store_security.StoreSecurityApplicationTests;
import com.store.security.store_security.dto.ArticleDto;
import com.store.security.store_security.dto.StockArticleDto;
import com.store.security.store_security.dto.StockDto;
import com.store.security.store_security.entity.ArticleEntity;
import com.store.security.store_security.entity.StockArticleEntity;
import com.store.security.store_security.entity.StockEntity;
import com.store.security.store_security.exceptions.ArticleException;
import com.store.security.store_security.exceptions.StockException;
import com.store.security.store_security.mapper.ArticleMapper;
import com.store.security.store_security.mapper.StockArticleMapper;
import com.store.security.store_security.mapper.StockMapper;
import com.store.security.store_security.repository.ArticleRepository;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.StockArticleRepository;
import com.store.security.store_security.repository.StockRepository;
import com.store.security.store_security.service.impl.StockService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class StockServiceIntegrationTest extends StoreSecurityApplicationTests {

	@Autowired
	private StockService stockService;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private StockArticleRepository stockArticleRepository;

	@Autowired
	private StockMapper stockMapper;

	@Autowired
	private StockArticleMapper stockArticleMapper;

	@Autowired
	private ArticleMapper articleMapper;

	//METHOD ALL STOCK

	@Test
	@Transactional
	public void getAllStock()
	{
		//given
		StockEntity stockEntity = StockEntity.builder().build();
		ArticleEntity articleEntity = ArticleEntity.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleEntity).quantity(10).build();
		stockEntity.setStockArticles(List.of(stockArticleEntity));
		stockArticleEntity.setStock(stockEntity);
		articleRepository.save(articleEntity);
		stockEntity = stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		//when
		StockDto stock = stockService.getStock();

		//then
		Assertions.assertThat(stock).isNotNull();
		stockEntity = stockRepository.findById(stockEntity.getId()).orElseThrow();
		StockDto expectedDto = stockMapper.toDto(stockEntity);
		Assertions.assertThat(stock).usingRecursiveComparison()
				.isEqualTo(expectedDto);
	}

	@Test
	public void getAllStockFail()
	{
		//given
		//when
		//then
		Assertions.assertThatThrownBy(()->stockService.getStock())
				.isInstanceOf(StockException.class)
				.hasMessageContaining("Stock not found");
	}

	//METHOD STOCK BY ARTICLE

	@Test
	@Transactional
	public void getStockByArticle()
	{
		//given
		StockEntity stockEntity = StockEntity.builder().build();
		ArticleEntity articleEntity = ArticleEntity.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleEntity).quantity(10).build();
		stockEntity.setStockArticles(List.of(stockArticleEntity));
		stockArticleEntity.setStock(stockEntity);
		ArticleEntity article = articleRepository.save(articleEntity);
		stockEntity = stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		//when
		StockDto stockDto = stockService.getStockByArticle(article.getId());
		//then
		Assertions.assertThat(stockDto).isNotNull();
		Assertions.assertThat(stockDto).usingRecursiveComparison()
				.isEqualTo(stockMapper.toDto(stockEntity));
	}

	@Test
	public void getStockByArticleFailArticle()
	{
		//given
		//when
		//then
		Assertions.assertThatThrownBy(()->stockService.getStockByArticle(3L))
				.isInstanceOf(ArticleException.class)
				.hasMessageContaining("[ARTICLE: 3] Article not found");
	}

	@Test
	public void getStockByArticleFailStock()
	{
		//given
		ArticleEntity articleEntity = ArticleEntity.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		ArticleEntity article = articleRepository.save(articleEntity);
		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleEntity).quantity(10).build();
		stockArticleRepository.save(stockArticleEntity);
		//when
		//then
		Assertions.assertThatThrownBy(()->stockService.getStockByArticle(article.getId()))
				.isInstanceOf(StockException.class)
				.hasMessageContaining(String.format("[ARTICLE: %s] Stock not found",article.getId()));
	}

	//METHOD LOAD ARTICLE

	@Test
	public void loadArticle()
	{
		//given
		ArticleDto articleDto = ArticleDto.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		StockEntity stockEntity = StockEntity.builder().build();
		stockRepository.save(stockEntity);
		//when
		ArticleDto result = stockService.loadArticle(articleDto);
		//then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getId()).isGreaterThan(0L);
		Assertions.assertThat(result).usingRecursiveComparison().ignoringFields("id","tmstInsert")
				.isEqualTo(articleDto);
	}

	@Test
	@Transactional
	public void loadArticleExistArticle()
	{
		//given
		StockEntity stockEntity = StockEntity.builder().build();
		ArticleEntity articleEntity = ArticleEntity.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleEntity).quantity(10).build();
		stockEntity.setStockArticles(List.of(stockArticleEntity));
		stockArticleEntity.setStock(stockEntity);
		articleRepository.save(articleEntity);
		stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		//when
		//then
		Assertions.assertThatThrownBy(()->stockService.loadArticle(articleMapper.toDto(articleEntity)))
				.isInstanceOf(ArticleException.class)
				.hasMessageContaining("[ARTICLE: car] Article exists");
		Iterable<ArticleEntity> article = articleRepository.findAll();
		Assertions.assertThat(article).hasSize(1);
	}


	//METHOD DECREMENT ARTICLE

	@Test
	public void decrementArticle()
	{
		//given
		StockEntity stockEntity = StockEntity.builder().build();
		ArticleEntity articleEntity = ArticleEntity.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleEntity).quantity(10).build();
		stockEntity.setStockArticles(List.of(stockArticleEntity));
		stockArticleEntity.setStock(stockEntity);
		ArticleEntity article = articleRepository.save(articleEntity);
		stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		//when
		StockArticleDto result = stockService.decrementArticle(article.getId(),1);
		//then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getQuantity()).isEqualTo(9);
	}

	@Test
	public void decrementArticleFailed()
	{
		//given
		StockEntity stockEntity = StockEntity.builder().build();
		ArticleEntity articleEntity = ArticleEntity.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleEntity).quantity(10).build();
		stockEntity.setStockArticles(List.of(stockArticleEntity));
		stockArticleEntity.setStock(stockEntity);
		ArticleEntity article = articleRepository.save(articleEntity);
		stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		//when
		//then
		Assertions.assertThatThrownBy(()->stockService.decrementArticle(article.getId(),11))
				.isInstanceOf(StockException.class)
				.hasMessageContaining(String.format("[ARTICLE: %s QUANTITY: %s] Stock not updated",article.getId(),11));
        Optional<StockArticleEntity> check = stockArticleRepository.findByArticle_Id(article.getId());
		StockArticleEntity checkResult = check.stream().filter(articleCheck->
				articleCheck.getArticle().getName().equals("car")).findFirst().get();
		Assertions.assertThat(checkResult.getQuantity()).isEqualTo(10);

	}

	//METHOD SAVE ARTICLE QUANTITY

	@Test
	public void saveArticleQuantity()
	{
		//given
		StockEntity stockEntity = StockEntity.builder().build();
		ArticleEntity articleEntity = ArticleEntity.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleEntity).quantity(10).build();
		stockEntity.setStockArticles(List.of(stockArticleEntity));
		stockArticleEntity.setStock(stockEntity);
		ArticleEntity article = articleRepository.save(articleEntity);
		stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		//when
		StockArticleDto result = stockService.saveArticleQuantity(article.getId(),1);
		//then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getQuantity()).isEqualTo(11);
	}

	@Test
	public void saveArticleQuantityFailed()
	{
		//given
		StockEntity stockEntity = StockEntity.builder().build();
		ArticleEntity articleEntity = ArticleEntity.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleEntity).quantity(0).build();
		stockEntity.setStockArticles(List.of(stockArticleEntity));
		stockArticleEntity.setStock(stockEntity);
		ArticleEntity article = articleRepository.save(articleEntity);
		stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		//when
		//then
		Assertions.assertThatThrownBy(()->stockService.saveArticleQuantity(article.getId(),0))
				.isInstanceOf(StockException.class)
				.hasMessageContaining(String.format("[ARTICLE: %s QUANTITY: %s] Stock not updated",article.getId(),0));
		Optional<StockArticleEntity> check = stockArticleRepository.findByArticle_Id(article.getId());
		StockArticleEntity checkResult = check.stream().filter(articleCheck->
				articleCheck.getArticle().getName().equals("car")).findFirst().get();
		Assertions.assertThat(checkResult.getQuantity()).isEqualTo(0);

	}

	//METHOD DELETE ARTICLE

	@Test
	public void deleteArticle()
	{
		//given
		StockEntity stockEntity = StockEntity.builder().build();
		ArticleEntity articleEntity = ArticleEntity.builder().name("car").price(new BigDecimal(1)).description("card description")
				.tmstInsert(LocalDateTime.of(2025,12,1,1,1)).build();
		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleEntity).quantity(10).build();
		stockEntity.setStockArticles(List.of(stockArticleEntity));
		stockArticleEntity.setStock(stockEntity);
		ArticleEntity article = articleRepository.save(articleEntity);
		stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		//when
		boolean result = stockService.deleteArticle(article.getId());
		//then
		Assertions.assertThat(result).isTrue();
		Optional<StockArticleEntity> check = stockArticleRepository.findByArticle_Id(article.getId());
		Assertions.assertThat(check.isPresent()).isFalse();
	}

	@Test
	public void deleteArticleFailed()
	{
		//given
		//when
		//then
		Assertions.assertThatThrownBy(()->stockService.deleteArticle(2L))
				.isInstanceOf(ArticleException.class)
				.hasMessageContaining(String.format("[ARTICLE %s] Article not found",2L));
	}


}
