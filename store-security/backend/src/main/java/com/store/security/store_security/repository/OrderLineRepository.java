package com.store.security.store_security.repository;

import com.store.security.store_security.entity.OrderLineEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderLineRepository extends CrudRepository<OrderLineEntity, Long> {

	public Optional<OrderLineEntity> findByArticle_IdAndOrder_User_Id(Long articleId, Long userId);
	public List<OrderLineEntity> findByOrder_Id(Long orderId);
}
