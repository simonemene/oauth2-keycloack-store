package com.store.security.store_security.service;

import com.store.security.store_security.dto.*;
import com.store.security.store_security.entity.*;
import com.store.security.store_security.entity.key.OrderLineKeyEmbeddable;
import com.store.security.store_security.exceptions.ArticleException;
import com.store.security.store_security.exceptions.OrderException;
import com.store.security.store_security.mapper.ArticleMapper;
import com.store.security.store_security.mapper.OrderMapper;
import com.store.security.store_security.repository.*;
import com.store.security.store_security.service.impl.OrderService;
import com.store.security.store_security.service.impl.StockService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

public class OrderServiceUnitTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private TrackRepository trackRepository;

	@Mock
	private OrderLineRepository orderLineRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ArticleMapper articleMapper;

	@Mock
	private ArticleRepository articleRepository;

	@Mock
	private OrderMapper orderMapper;

	@Mock
	private StockService stockService;

	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		orderService = new OrderService(orderRepository,
				trackRepository,orderLineRepository,userRepository,
				articleMapper,articleRepository,orderMapper,stockService);
	}

	@Test
	public void orderFailArticles() throws OrderException {
		//given
		ArticleDto articleDto = ArticleDto.builder().name("car").build();
		AllArticleOrderDto allArticleOrderDto = AllArticleOrderDto.builder().quantity(1).articleDto(articleDto).build();
		List<AllArticleOrderDto> articles = List.of(allArticleOrderDto);
		ArticlesOrderDto articlesOrderDto = ArticlesOrderDto.builder().username("username").articles(articles).build();
		Mockito.when(articleRepository.findByName(Mockito.anyString())).thenReturn(
				ArticleEntity.builder().name("car").id(0L).build());

		//when
		//then
		Assertions.assertThatThrownBy(()->orderService.orderArticles(articlesOrderDto))
				.isInstanceOf(ArticleException.class)
				.hasMessageContaining(String.format("[ARTICLE: %s] NOT FOUND",articleDto.getName()));
	}

	@Test
	public void orderFailOrderSave() throws OrderException {
		//given
		UserEntity user = UserEntity.builder().username("username").build();
		Mockito.when(userRepository.findByUsername("username")).thenReturn(
				Optional.ofNullable(user));

		ArticleDto articleDto = ArticleDto.builder().name("car").tmstInsert(LocalDateTime.of(2022,1,1,1,1)).build();
		AllArticleOrderDto allArticleOrderDto = AllArticleOrderDto.builder().quantity(2).articleDto(articleDto).build();
		List<AllArticleOrderDto> articles = List.of(allArticleOrderDto);
		ArticlesOrderDto articlesOrderDto = ArticlesOrderDto.builder().username("username").articles(articles).build();
		Mockito.when(articleRepository.findByName("car")).thenReturn(
				ArticleEntity.builder().name("car").id(1L).build());

		Mockito.when(orderRepository.save(Mockito.any()))
				.thenReturn(OrderEntity.builder().id(0L).build());

		//when
		//then
		Assertions.assertThatThrownBy(()->orderService.orderArticles(articlesOrderDto))
				.isInstanceOf(OrderException.class)
				.hasMessageContaining(String.format("[USER: %s ARTICLES: %s] ORDER NOT SAVED","username","[car]"));
	}

	@Test
	public void orderLineFailed() throws OrderException {
		//given
		UserEntity user = UserEntity.builder().username("username").build();
		Mockito.when(userRepository.findByUsername("username")).thenReturn(
				Optional.ofNullable(user));

		ArticleDto articleDto = ArticleDto.builder().name("car").tmstInsert(LocalDateTime.of(2022,1,1,1,1)).build();
		AllArticleOrderDto allArticleOrderDto = AllArticleOrderDto.builder().quantity(2).articleDto(articleDto).build();
		List<AllArticleOrderDto> articles = List.of(allArticleOrderDto);
		ArticlesOrderDto articlesOrderDto = ArticlesOrderDto.builder().username("username").articles(articles).build();
		Mockito.when(articleRepository.findByName("car")).thenReturn(
				ArticleEntity.builder().name("car").id(1L).build());

		Mockito.when(orderRepository.save(Mockito.any()))
				.thenReturn(OrderEntity.builder().id(1L).build());

		OrderLineKeyEmbeddable idLine = OrderLineKeyEmbeddable.builder().idArticle(0L).idOrder(1L).build();
		Mockito.when(orderLineRepository.save(Mockito.any())).thenReturn(OrderLineEntity.builder().id(idLine).build());

		//when
		//then
		Assertions.assertThatThrownBy(()->orderService.orderArticles(articlesOrderDto))
				.isInstanceOf(OrderException.class)
				.hasMessageContaining(String.format("[ORDER: %s ARTICLES: %s] ORDER LINE NOT SAVED","1","car"));
	}

	@Test
	public void orderTrackFailed() throws OrderException {
		//given
		UserEntity user = UserEntity.builder().username("username").build();
		Mockito.when(userRepository.findByUsername("username")).thenReturn(
				Optional.ofNullable(user));

		ArticleDto articleDto = ArticleDto.builder().name("car").tmstInsert(LocalDateTime.of(2022,1,1,1,1)).build();
		AllArticleOrderDto allArticleOrderDto = AllArticleOrderDto.builder().quantity(2).articleDto(articleDto).build();
		List<AllArticleOrderDto> articles = List.of(allArticleOrderDto);
		ArticlesOrderDto articlesOrderDto = ArticlesOrderDto.builder().username("username").articles(articles).build();
		Mockito.when(articleRepository.findByName("car")).thenReturn(
				ArticleEntity.builder().name("car").id(1L).build());

		Mockito.when(orderRepository.save(Mockito.any()))
				.thenReturn(OrderEntity.builder().id(1L).build());

		OrderLineKeyEmbeddable idLine = OrderLineKeyEmbeddable.builder().idArticle(1L).idOrder(1L).build();
		Mockito.when(orderLineRepository.save(Mockito.any())).thenReturn(OrderLineEntity.builder().id(idLine).build());

		Mockito.when(trackRepository.save(Mockito.any())).thenReturn(TrackEntity.builder().id(0L).build());

		//when
		//then
		Assertions.assertThatThrownBy(()->orderService.orderArticles(articlesOrderDto))
				.isInstanceOf(OrderException.class)
				.hasMessageContaining(String.format("[ORDER: %s] TRACK NOT SAVED","1"));
	}


}