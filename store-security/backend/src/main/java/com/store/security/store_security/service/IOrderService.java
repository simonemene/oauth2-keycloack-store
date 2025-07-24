package com.store.security.store_security.service;

import com.store.security.store_security.dto.AllOrderDto;
import com.store.security.store_security.dto.ArticlesOrderDto;
import com.store.security.store_security.exceptions.OrderException;

public interface IOrderService {

	public ArticlesOrderDto orderArticles(ArticlesOrderDto articlesOrderDto)
			throws OrderException;

	public AllOrderDto allOrderByUser(Long id) throws OrderException;


}
