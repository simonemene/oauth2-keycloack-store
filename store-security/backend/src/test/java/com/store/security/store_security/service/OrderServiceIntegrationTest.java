package com.store.security.store_security.service;

import com.store.security.store_security.StoreSecurityApplicationTests;
import com.store.security.store_security.constants.RoleConstants;
import com.store.security.store_security.dto.*;
import com.store.security.store_security.entity.*;
import com.store.security.store_security.entity.key.OrderLineKeyEmbeddable;
import com.store.security.store_security.enums.StatusTrackEnum;
import com.store.security.store_security.exceptions.OrderException;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.mapper.ArticleMapper;
import com.store.security.store_security.repository.*;
import com.store.security.store_security.service.impl.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class OrderServiceIntegrationTest extends StoreSecurityApplicationTests {

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderLineRepository orderLineRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private StockArticleRepository stockArticleRepository;


	@Test
	public void addOrder() throws OrderException {
		//given
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("username", "password"));
		UserEntity user = UserEntity.builder().authoritiesList(Set.of(AuthoritiesEntity.builder()
				.authority(RoleConstants.USER.getRole()).build())).username("username").password("1234")
				.age(23).tmstInsert(LocalDateTime.now()).authoritiesList(Set.of()).build();
		userRepository.save(user);

		List<AllArticleOrderDto> articles = new ArrayList<>();

		ArticleDto article1 = ArticleDto.builder().name("car").tmstInsert(LocalDateTime.now())
				.description("test").price(new BigDecimal(10)).build();
		ArticleDto article2 = ArticleDto.builder().name("table").tmstInsert(LocalDateTime.now())
				.description("test1").price(new BigDecimal(15)).build();


		ArticleEntity articleResult = articleRepository.save(articleMapper.toEntity(article1));
		ArticleEntity articleResult1 = articleRepository.save(articleMapper.toEntity(article2));



		AllArticleOrderDto articleOrderDto = AllArticleOrderDto.builder().articleDto(article1).quantity(3).build();
		AllArticleOrderDto articleOrderDto1 = AllArticleOrderDto.builder().articleDto(article2).quantity(31).build();
		articles.add(articleOrderDto1);
		articles.add(articleOrderDto);


		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleResult).quantity(10).build();
		StockArticleEntity stockArticleEntity2 = StockArticleEntity.builder().article(articleResult1).quantity(10).build();
		StockEntity stockEntity = StockEntity.builder().stockArticles(List.of(stockArticleEntity,stockArticleEntity2)).build();
		stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		stockArticleRepository.save(stockArticleEntity2);



		ArticlesOrderDto articlesOrderDto = ArticlesOrderDto.builder().articles(articles).username("username").build();
		//when
		articlesOrderDto = orderService.orderArticles(articlesOrderDto);
		//then
		Assertions.assertThat(articlesOrderDto).isNotNull();
		Assertions.assertThat(articlesOrderDto.getArticles()).isNotNull();
		Assertions.assertThat(articlesOrderDto.getArticles().size()).isEqualTo(2);

		ArticleDto checkArticle1 = articlesOrderDto.getArticles().stream()
				.filter(value-> value.getQuantity() == 3)
				.findFirst().get().getArticleDto();

		Assertions.assertThat(checkArticle1.getId()).isGreaterThan(0);
		Assertions.assertThat(articles.stream()
				.filter(check-> check.getQuantity() == 3).findFirst().get().getArticleDto())
				.usingRecursiveComparison().ignoringFields("price","id","description","tmstInsert").isEqualTo(checkArticle1);

		Iterable<OrderEntity> order = orderRepository.findAll();
		Assertions.assertThat(order).hasSize(1);
		OrderEntity orderEntity = order.iterator().next();
		Assertions.assertThat(orderEntity.getUser().getUsername()).isEqualTo("username");


		Iterable<OrderLineEntity> orderLine = orderLineRepository.findAll();
		Assertions.assertThat(orderLine).hasSize(2);
		ArrayList<OrderLineEntity> orderLineEntity = new ArrayList<>();
		Iterator<OrderLineEntity> iterator = orderLine.iterator();
		orderLineEntity.add(iterator.next());
		orderLineEntity.add(iterator.next());
		Assertions.assertThat(orderLineEntity.stream().filter(el->el.getArticle().getName().equals("car")).findFirst().get().getQuantity()).isEqualTo(3);
		Assertions.assertThat(orderLineEntity.stream().filter(el->el.getArticle().getName().equals("table")).findFirst().get().getQuantity()).isEqualTo(31);

		Iterable<TrackEntity> trackLine = trackRepository.findAll();
		Assertions.assertThat(trackLine).hasSize(1);
		TrackEntity trackEntity = trackLine.iterator().next();
		Assertions.assertThat(trackEntity.getStatus()).isEqualTo(
				StatusTrackEnum.ORDER_PLACED.getTrack());

	}

	@Test
	public void addOrderFailedNoUser(){
		//given
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("username", "password"));


		ArticleDto article1 = ArticleDto.builder().name("car").tmstInsert(LocalDateTime.now())
				.description("test").price(new BigDecimal(10)).build();
		ArticleDto article2 = ArticleDto.builder().name("table").tmstInsert(LocalDateTime.now())
				.description("test1").price(new BigDecimal(15)).build();

		ArticleEntity articleResult = articleRepository.save(articleMapper.toEntity(article1));
		ArticleEntity articleResult1 = articleRepository.save(articleMapper.toEntity(article2));


		List<AllArticleOrderDto> articlesOrder = new ArrayList<>();

		AllArticleOrderDto articleOrderDto = AllArticleOrderDto.builder().articleDto(article1).quantity(3).build();
		AllArticleOrderDto articleOrderDto1 = AllArticleOrderDto.builder().articleDto(article2).quantity(31).build();
		articlesOrder.add(articleOrderDto1);
		articlesOrder.add(articleOrderDto);

		ArticlesOrderDto articlesOrderDto = ArticlesOrderDto.builder().articles(articlesOrder).username("username").build();

		StockArticleEntity stockArticleEntity = StockArticleEntity.builder().article(articleResult).quantity(10).build();
		StockArticleEntity stockArticleEntity2 = StockArticleEntity.builder().article(articleResult1).quantity(10).build();
		StockEntity stockEntity = StockEntity.builder().stockArticles(List.of(stockArticleEntity,stockArticleEntity2)).build();
		stockRepository.save(stockEntity);
		stockArticleRepository.save(stockArticleEntity);
		stockArticleRepository.save(stockArticleEntity2);



		//when
		//then
		Assertions.assertThatThrownBy(()->orderService.orderArticles(articlesOrderDto))
				.isInstanceOf(UserException.class)
				.hasMessageContaining(String.format("[USER OBJECT %s USER SECURITY %s] NOT FOUND","username","username"));

		Iterable<OrderEntity> order = orderRepository.findAll();
		Assertions.assertThat(order).hasSize(0);
		Iterable<OrderLineEntity> orderLine = orderLineRepository.findAll();
		Assertions.assertThat(orderLine).hasSize(0);
	}


	@Test
	public void addOrderFailedArticlesQuantity(){
		//given
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("username", "password"));
		UserEntity user = UserEntity.builder().username("username").password("1234")
				.age(23).tmstInsert(LocalDateTime.now()).authoritiesList(Set.of()).build();
		userRepository.save(user);


		ArticleDto article1 = ArticleDto.builder().name("car").tmstInsert(LocalDateTime.now())
				.description("test").price(new BigDecimal(10)).build();
		ArticleDto article2 = ArticleDto.builder().name("table").tmstInsert(LocalDateTime.now())
				.description("test1").price(new BigDecimal(15)).build();

		ArticleEntity savedArticle1 = articleRepository.save(articleMapper.toEntity(article1));
		ArticleEntity savedArticle2 = articleRepository.save(articleMapper.toEntity(article2));

		List<AllArticleOrderDto> articlesOrder = new ArrayList<>();

		AllArticleOrderDto articleOrderDto = AllArticleOrderDto.builder().articleDto(article1).quantity(null).build();
		AllArticleOrderDto articleOrderDto1 = AllArticleOrderDto.builder().articleDto(article2).quantity(31).build();
		articlesOrder.add(articleOrderDto1);
		articlesOrder.add(articleOrderDto);

		ArticlesOrderDto articlesOrderDto = ArticlesOrderDto.builder().articles(articlesOrder).username("username").build();
		//when
		//then
		Assertions.assertThatThrownBy(()->orderService.orderArticles(articlesOrderDto))
				.isInstanceOf(OrderException.class)
				.hasMessageContaining("INVALID QUANTITY");

		Iterable<OrderEntity> order = orderRepository.findAll();
		Assertions.assertThat(order).hasSize(0);
		Iterable<OrderLineEntity> orderLine = orderLineRepository.findAll();
		Assertions.assertThat(orderLine).hasSize(0);
	}

	@Test
	public void findAllOrder() throws OrderException {
		//given
		AuthoritiesEntity auth = AuthoritiesEntity.builder().authority(RoleConstants.USER.getRole()).build();
		authoritiesRepository.save(auth);
		UserEntity user = UserEntity.builder().authoritiesList(Set.of(auth)).username("username").password("1234")
				.tmstInsert(LocalDateTime.of(2022,1,1,1,1)).authoritiesList(Set.of(auth)).build();
		UserEntity user1 = UserEntity.builder().authoritiesList(Set.of(auth)).username("username1").password("1234")
				.tmstInsert(LocalDateTime.of(2022,1,1,1,1)).authoritiesList(Set.of(auth)).build();
		user = userRepository.save(user);
		user1 =userRepository.save(user1);
		//articles
		ArticleEntity article = ArticleEntity.builder().name("car").tmstInsert(LocalDateTime.of(2022,1,1,1,1))
				.description("test").price(new BigDecimal(10)).build();
		ArticleEntity article1 = ArticleEntity.builder().name("table").tmstInsert(LocalDateTime.of(2022,1,1,1,1))
				.description("test1").price(new BigDecimal(15)).build();
		article = articleRepository.save(article);
		article1 = articleRepository.save(article1);
		//orders
		OrderEntity order = OrderEntity.builder().user(user).tmstInsert(LocalDateTime.of(2022,1,1,1,1)).build();
		OrderEntity order2 = OrderEntity.builder().user(user1).tmstInsert(LocalDateTime.of(2022,1,1,1,1)).build();
		order = orderRepository.save(order);
		order2 = orderRepository.save(order2);
		//orderlines
		OrderLineKeyEmbeddable orderLineKey = OrderLineKeyEmbeddable.builder().idArticle(article.getId()).idOrder(
				order.getId()).build();
		OrderLineKeyEmbeddable orderLineKey1 = OrderLineKeyEmbeddable.builder().idArticle(article1.getId()).idOrder(
				order2.getId()).build();
		OrderLineEntity orderLine = OrderLineEntity.builder().id(orderLineKey).article(article).order(order).quantity(3).build();
		OrderLineEntity orderLine1 = OrderLineEntity.builder().id(orderLineKey1).article(article1).order(order2).quantity(31).build();
		orderLineRepository.save(orderLine);
		orderLineRepository.save(orderLine1);
		//when
		AllOrderDto orders = orderService.allOrderByUser(user.getId());
		//then
		Assertions.assertThat(orders.getOrders()).hasSize(1);
		ArticlesOrderDto articlesOrderDto = orders.getOrders().get(0);
		Assertions.assertThat(articlesOrderDto).isNotNull();
		Assertions.assertThat(articlesOrderDto.getUsername()).isEqualTo("username");
		Assertions.assertThat(articlesOrderDto.getArticles()).hasSize(1);
		Integer quantity = articlesOrderDto.getArticles().getFirst().getQuantity();
		ArticleDto articleDto = articlesOrderDto.getArticles().getFirst().getArticleDto();
		Assertions.assertThat(quantity).isEqualTo(3);
		Assertions.assertThat(articleDto).isNotNull();
		Assertions.assertThat(articleDto.getName()).isEqualTo("car");
		Assertions.assertThat(articleDto.getDescription()).isEqualTo("test");
		Assertions.assertThat(articleDto.getPrice()).isEqualByComparingTo(
				String.valueOf(10));


	}







}
