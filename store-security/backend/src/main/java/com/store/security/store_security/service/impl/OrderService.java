package com.store.security.store_security.service.impl;

import com.store.security.store_security.annotation.LogExecutionTime;
import com.store.security.store_security.dto.AllArticleOrderDto;
import com.store.security.store_security.dto.AllOrderDto;
import com.store.security.store_security.dto.ArticleDto;
import com.store.security.store_security.dto.ArticlesOrderDto;
import com.store.security.store_security.entity.*;
import com.store.security.store_security.entity.key.OrderLineKeyEmbeddable;
import com.store.security.store_security.enums.StatusTrackEnum;
import com.store.security.store_security.exceptions.ArticleException;
import com.store.security.store_security.exceptions.OrderException;
import com.store.security.store_security.exceptions.StockException;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.mapper.ArticleMapper;
import com.store.security.store_security.mapper.OrderMapper;
import com.store.security.store_security.repository.*;
import com.store.security.store_security.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {

	private final OrderRepository orderRepository;

	private final TrackRepository trackRepository;

	private final OrderLineRepository orderLineRepository;

	private final UserRepository userRepository;

	private final ArticleMapper articleMapper;

	private final ArticleRepository articleRepository;

	private final OrderMapper orderMapper;

	private final StockService stockService;

	@LogExecutionTime
	@Transactional
	@Override
	public ArticlesOrderDto orderArticles(ArticlesOrderDto articlesOrderDto)
			throws OrderException {
		AllArticleOrderDto allArticleOrderDtos = AllArticleOrderDto.builder().build();
		ArticlesOrderDto result = ArticlesOrderDto.builder().build();

		for(AllArticleOrderDto article : articlesOrderDto.getArticles())
		{
			if(null == article || null == article.getArticleDto())
			{
				throw new OrderException("INVALID QUANTITY");
			}
		}
		for (int i = 0; i < articlesOrderDto.getArticles().size(); i++) {
			ArticleEntity article = articleRepository.findByName(articlesOrderDto.getArticles().get(i).getArticleDto().getName());
			if (article.getId() <= 0) {
				throw new ArticleException(String.format("[ARTICLE: %s] NOT FOUND",articlesOrderDto.getArticles().get(i).getArticleDto().getName()));
			}
			ArticleDto updatedDto = ArticleDto.builder()
					.id(article.getId())
					.name(article.getName())
					.build();
			articlesOrderDto.getArticles().get(i).setArticleDto(updatedDto);
			try {
				stockService.decrementArticle(article.getId(), 1);
			} catch (StockException e) {
				throw new OrderException("Stock not sufficient for article: " + article.getName());
			}
		}

		UserEntity user = userRepository.findByUsername(articlesOrderDto.getUsername())
				.orElseThrow(()-> new UserException(String.format("[USER OBJECT %s USER SECURITY %s] NOT FOUND"
						, articlesOrderDto.getUsername(), SecurityContextHolder.getContext().getAuthentication().getName())));
		OrderEntity order = OrderEntity.builder().user(user).tmstInsert(
				LocalDateTime.now()).build();
		order = orderRepository.save(order);


		if(order.getId() <= 0)
		{
			List<String> codeArticle = new ArrayList<>();
			for(AllArticleOrderDto articles : articlesOrderDto.getArticles()) {
				codeArticle.add(articles.getArticleDto().getName());
			}
			throw new OrderException(String.format("[USER: %s ARTICLES: %s] ORDER NOT SAVED",articlesOrderDto.getUsername(),codeArticle));
		}
		result.setIdOrder(order.getId());

		for(AllArticleOrderDto articles : articlesOrderDto.getArticles())
		{
			OrderLineKeyEmbeddable orderLineKeyEmbeddable = OrderLineKeyEmbeddable.builder()
					.idOrder(order.getId()).idArticle(articles.getArticleDto().getId()).build();
			OrderLineEntity orderLineEntity = OrderLineEntity.builder().id(orderLineKeyEmbeddable).article(articleMapper.toEntity(articles.getArticleDto()))
					.order(order).quantity(articles.getQuantity()).build();
			orderLineEntity = orderLineRepository.save(orderLineEntity);

			if(orderLineEntity.getId().getIdOrder() <= 0 || orderLineEntity.getId().getIdArticle() <= 0)
			{
				throw new OrderException(String.format("[ORDER: %s ARTICLES: %s] ORDER LINE NOT SAVED",order.getId(),articles.getArticleDto().getName()));
			}
			AllArticleOrderDto resultArticle = AllArticleOrderDto.builder().articleDto(ArticleDto.builder().id(orderLineEntity.getId().getIdArticle()).name(articles.getArticleDto().getName()).build()).quantity(orderLineEntity.getQuantity()).build();
            result.addArticle(resultArticle);


		}

		TrackEntity trackEntity = TrackEntity.builder().order(order).status(
				StatusTrackEnum.ORDER_PLACED.getTrack()).build();
		trackEntity = trackRepository.save(trackEntity);
		if(trackEntity.getId() <= 0)
		{
			throw new OrderException(String.format("[ORDER: %s] TRACK NOT SAVED",order.getId()));
		}


		return ArticlesOrderDto.builder().articles(result.getArticles()).username(SecurityContextHolder.getContext().getAuthentication().getName()).build();
	}

	@Override
	public AllOrderDto allOrderByUser(Long id) throws OrderException {
		List<OrderEntity> orders = orderRepository.findByUserId(id);
		AllOrderDto allOrderDto = AllOrderDto.builder().build();

		if(!orders.isEmpty()) {


			for (OrderEntity order : orders) {
				ArticlesOrderDto articlesOrders = ArticlesOrderDto.builder().build();
				List<OrderLineEntity> ordersLine = orderLineRepository.findByOrder_Id(order.getId());


				List<AllArticleOrderDto> allArticleOrderDtos = new ArrayList<>();
				for (OrderLineEntity orderLine : ordersLine) {
					AllArticleOrderDto allArticleOrderDto = AllArticleOrderDto.builder().articleDto(articleMapper.toDto(orderLine.getArticle())).quantity(orderLine.getQuantity()).build();
					allArticleOrderDtos.add(allArticleOrderDto);
				}
				if (!allArticleOrderDtos.isEmpty()) {
					articlesOrders.setArticles(allArticleOrderDtos);
					articlesOrders.setIdOrder(order.getId());
					articlesOrders.setUsername(order.getUser().getUsername());
				} else {
					throw new OrderException(String.format("[USER: %s] ORDER NOT ADD", order.getUser().getUsername()));
				}
				allOrderDto.addOrders(articlesOrders);

			}
		}else
		{
			throw new OrderException(String.format("[USER: %s] ORDER NOT ADD", SecurityContextHolder.getContext().getAuthentication().getName()));
		}
		return allOrderDto;
	}

}
