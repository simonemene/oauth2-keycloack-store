package com.store.security.store_security.repository;

import com.store.security.store_security.entity.AuthoritiesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthoritiesRepository extends CrudRepository<AuthoritiesEntity,Long> {

    Optional<AuthoritiesEntity> findByAuthority(String authority);
}
