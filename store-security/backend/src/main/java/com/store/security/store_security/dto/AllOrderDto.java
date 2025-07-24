package com.store.security.store_security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllOrderDto {

	private List<ArticlesOrderDto> orders;

	public void addOrders(ArticlesOrderDto articlesOrderDto) {
		if(null == orders)
		{
			orders = new ArrayList<>();
			orders.add(articlesOrderDto);
		}else
		{
			orders.add(articlesOrderDto);
		}
	}
}
