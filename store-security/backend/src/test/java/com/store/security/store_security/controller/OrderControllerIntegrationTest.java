package com.store.security.store_security.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.security.store_security.StoreSecurityApplicationTests;
import com.store.security.store_security.constants.RoleConstants;
import com.store.security.store_security.controladvice.GenericExceptionHandler;
import com.store.security.store_security.dto.AllArticleOrderDto;
import com.store.security.store_security.dto.AllOrderDto;
import com.store.security.store_security.dto.ArticleDto;
import com.store.security.store_security.dto.ArticlesOrderDto;
import com.store.security.store_security.entity.*;
import com.store.security.store_security.entity.key.OrderLineKeyEmbeddable;
import com.store.security.store_security.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

@AutoConfigureMockMvc
@Import(GenericExceptionHandler.class)
public class OrderControllerIntegrationTest extends StoreSecurityApplicationTests {

	@Autowired
	private  OrderController orderController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderLineRepository orderLineRepository;

	@Autowired
	private TrackRepository trackRepository;


	@Autowired
	private StockArticleRepository stockArticleRepository;


	@Test
	@WithMockUser(username = "utente@gmail.com", roles = "USER")
	public void createOrder() throws Exception {
		//given

		AuthoritiesEntity authorities = AuthoritiesEntity.builder()
				.authority("ROLE_USER").build();
		authoritiesRepository.save(authorities);
		UserEntity user = UserEntity.builder()
				.tmstInsert(LocalDateTime.now())
				.authoritiesList(Set.of(authorities))
				.username("utente@gmail.com").age(21).build();
		userRepository.save(user);


		ArticleDto articleDto = ArticleDto.builder()
				.name("car").description("test").price(BigDecimal.TEN).tmstInsert(
						LocalDateTime.now()).id(1L).build();
		ArticleEntity article = ArticleEntity.builder().name(articleDto.getName())
				.description(articleDto.getDescription()).price(articleDto.getPrice())
				.tmstInsert(articleDto.getTmstInsert()).build();
		articleRepository.save(article);
		StockArticleEntity articleEntity = StockArticleEntity.builder().article(article).quantity(1).build();
		stockArticleRepository.save(articleEntity);

		AllArticleOrderDto allArticleOrderDto = AllArticleOrderDto.builder().quantity(1).articleDto(articleDto).build();
		List<AllArticleOrderDto> allArticleOrderDtos = List.of(allArticleOrderDto);
		ArticlesOrderDto articles = ArticlesOrderDto.builder().idOrder(1L).articles(allArticleOrderDtos).username("utente@gmail.com").build();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(articles);
		//when
		mockMvc.perform(MockMvcRequestBuilders.post("/api/orders").contentType("application/json").content(json)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isCreated());
		//then
		Iterable<OrderEntity> orders = orderRepository.findAll();
		OrderEntity order = orders.iterator().next();
		Assertions.assertThat(order.getUser().getUsername()).isEqualTo(articles.getUsername());
		List<OrderLineEntity> orderLines = orderLineRepository.findByOrder_Id(order.getId());
		Assertions.assertThat(orderLines.size()).isEqualTo(1);
		Assertions.assertThat(orderLines.getFirst().getArticle().getName()).isEqualTo(article.getName());
		Assertions.assertThat(orderLines.getFirst().getOrder().getUser().getUsername()).isEqualTo(articles.getUsername());
		Iterable<TrackEntity> trackEntity = trackRepository.findAll();
		Assertions.assertThat(trackEntity.iterator().next().getOrder().getId()).isEqualTo(order.getId());
		List<TrackEntity> listTrack = StreamSupport.stream(trackEntity.spliterator(),false).toList();
		Assertions.assertThat(listTrack.size()).isEqualTo(1);
	}

	@Test
	@WithMockUser(username = "utente@gmail.com", roles = "USER")
	public void getAllOrder() throws Exception {
		//given
		AuthoritiesEntity auth = AuthoritiesEntity.builder().authority(RoleConstants.USER.getRole()).build();
		authoritiesRepository.save(auth);
		UserEntity user = UserEntity.builder().authoritiesList(Set.of(auth)).username("utente@gmail.com").password("1234")
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
		//then
		MvcResult mvc = mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}",user.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		String response = mvc.getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		AllOrderDto allOrderDto = mapper.readValue(response, new TypeReference<AllOrderDto>() {});
		Assertions.assertThat(allOrderDto.getOrders().size()).isEqualTo(1);
		Assertions.assertThat(allOrderDto.getOrders().get(0).getArticles().size()).isEqualTo(1);
		Assertions.assertThat(allOrderDto.getOrders().get(0).getArticles().get(0).getArticleDto().getName()).isEqualTo("car");
		Assertions.assertThat(allOrderDto.getOrders().get(0).getUsername()).isEqualTo("utente@gmail.com");
	}
}
