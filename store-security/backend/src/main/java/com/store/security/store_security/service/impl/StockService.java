package com.store.security.store_security.service.impl;

import com.store.security.store_security.annotation.LogExecutionTime;
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
import com.store.security.store_security.repository.StockArticleRepository;
import com.store.security.store_security.repository.StockRepository;
import com.store.security.store_security.service.IStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class StockService implements IStockService {

	private final StockRepository stockRepository;

	private final ArticleRepository articleRepository;

	private final StockArticleRepository stockArticleRepository;

	private final StockMapper stockMapper;

	private final StockArticleMapper stockArticleMapper;

	private final ArticleMapper articleMapper;


	@Transactional(readOnly = true)
	@Override
	public StockDto getStock() {
		Iterable<StockEntity> allStock  = stockRepository.findAll();
		StockEntity stockEntity = null;
		if(allStock.iterator().hasNext())
		{
			stockEntity = allStock.iterator().next();

		}
		if(null != stockEntity)
		{
			return stockMapper.toDto(stockEntity);
		}
		throw new StockException("Stock not found");
	}

	@Transactional(readOnly = true)
	@Override
	public StockDto getStockByArticle(Long idArticle) {
		StockArticleEntity stockArticleEntity = stockArticleRepository.findByArticle_Id(idArticle)
				.orElseThrow(()->new ArticleException(String.format("[ARTICLE: %s] Article not found",idArticle)));
		StockEntity stockEntity = stockArticleEntity.getStock();
		if(null != stockEntity && stockEntity.getId()>0)
		{
			return stockMapper.toDto(stockEntity);
		}
		throw new StockException(String.format("[ARTICLE: %s] Stock not found",idArticle));
	}

	@LogExecutionTime
	@Transactional
	@Override
	public ArticleDto loadArticle(ArticleDto articleDto) {
		Optional<StockArticleEntity> stockArticleEntityCheck = stockArticleRepository.findByArticle_Name(articleDto.getName());
		if(stockArticleEntityCheck.isPresent())
		{
			throw new ArticleException(String.format("[ARTICLE: %s] Article exists",articleDto.getName()));
		}
		Optional<StockEntity> stockOptional = Optional.ofNullable(StreamSupport.stream(stockRepository.findAll().spliterator(), false)
				.findFirst().orElseThrow(() -> new StockException("Stock not found")));
		if(stockOptional.isEmpty())
		{
			throw new StockException("Stock not found");
		}
		ArticleEntity article = articleMapper.toEntity(articleDto);
		article.setTmstInsert(LocalDateTime.now());
		article = articleRepository.save(article);
		if(article.getId()<=0)
		{
			throw new ArticleException(String.format("[ARTICLE: %s] Article not saved",article.getId()));
		}
		StockArticleEntity stockArticleEntity = stockArticleRepository.save(StockArticleEntity.builder()
				.article(article).stock(stockOptional.get()).quantity(0).build());
		if(stockArticleEntity.getId()<=0)
		{
			throw new StockException(String.format("[ARTICLE: %s] Stock not saved",article.getId()));
		}
		return articleMapper.toDto(article);

	}

	@Transactional
	@Override
	public StockArticleDto decrementArticle(Long id,Integer quantity) {
		StockArticleEntity stockArticleEntity = stockArticleRepository.findByArticle_Id(id)
				.orElseThrow(()->new ArticleException(String.format("[ARTICLE %s] Article not found",id)));
		int calculatedQuantity = stockArticleEntity.getQuantity() - quantity;
		if(stockArticleEntity.getQuantity() < 0 || calculatedQuantity < 0)
		{
			throw new StockException(String.format("[ARTICLE: %s QUANTITY: %s] Stock not updated",id,quantity));
		}
		stockArticleEntity.setQuantity(calculatedQuantity);
		stockArticleEntity = stockArticleRepository.save(stockArticleEntity);
		if(stockArticleEntity.getId()>0)
		{
			return stockArticleMapper.toDto(stockArticleEntity);
		}
		throw new StockException(String.format("[ARTICLE: %s QUANTITY: %s] Stock not updated",id,quantity));
	}

	@Transactional
	@Override
	public StockArticleDto saveArticleQuantity(Long id, Integer quantity) {
		StockArticleEntity stockArticleEntity = stockArticleRepository.findByArticle_Id(id)
				.orElseThrow(()->new ArticleException(String.format("[ARTICLE %s] Article not found",id)));
		int calculatedQuantity = stockArticleEntity.getQuantity() + quantity;
		if(stockArticleEntity.getQuantity() < 0 || calculatedQuantity <= 0)
		{
			throw new StockException(String.format("[ARTICLE: %s QUANTITY: %s] Stock not updated",id,quantity));
		}
		stockArticleEntity.setQuantity(calculatedQuantity);
		stockArticleEntity = stockArticleRepository.save(stockArticleEntity);
		if(stockArticleEntity.getId()>0)
		{
			return stockArticleMapper.toDto(stockArticleEntity);
		}
		throw new StockException(String.format("[ARTICLE: %s QUANTITY: %s] Stock not updated",id,quantity));
	}

	@Transactional
	@Override
	public boolean deleteArticle(Long id) {
		StockArticleEntity stockArticleEntity = stockArticleRepository.findByArticle_Id(id)
				.orElseThrow(()->new ArticleException(String.format("[ARTICLE %s] Article not found",id)));
		stockArticleRepository.delete(stockArticleEntity);
		if(stockArticleRepository.findByArticle_Id(id).isEmpty())
		{
			return true;
		}
		throw new StockException(String.format("[ARTICLE: %s] Article not deleted",id));
	}
}
