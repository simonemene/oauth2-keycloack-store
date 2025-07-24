package com.store.security.store_security.repository;

import com.store.security.store_security.entity.TrackEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends CrudRepository<TrackEntity, Long> {

	public TrackEntity findByOrder_Id(Long idOrder);
}
