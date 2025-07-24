package com.store.security.store_security.repository;

import com.store.security.store_security.entity.StockArticleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockArticleRepository extends CrudRepository<StockArticleEntity, Long> {

    public Optional<StockArticleEntity> findByArticle_Id(Long articleId);

    public Optional<StockArticleEntity> findByArticle_Name(String name);
}
