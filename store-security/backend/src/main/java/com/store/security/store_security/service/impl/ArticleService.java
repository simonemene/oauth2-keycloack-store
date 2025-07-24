package com.store.security.store_security.service.impl;

import com.store.security.store_security.dto.ArticleDto;
import com.store.security.store_security.dto.ListArticleDto;
import com.store.security.store_security.entity.ArticleEntity;
import com.store.security.store_security.exceptions.ArticleException;
import com.store.security.store_security.mapper.ArticleMapper;
import com.store.security.store_security.repository.ArticleRepository;
import com.store.security.store_security.service.IArticleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class ArticleService implements IArticleService {

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    @Override
    public ListArticleDto allArticle() {
        Iterable<ArticleEntity> articles = articleRepository.findAll();
        return ListArticleDto.builder().articles(
                StreamSupport.stream(articles.spliterator(),false)
                        .map(articleMapper::toDto).toList()).build();
    }

    @Override
    public ArticleDto getArticle(Long id) {
        ArticleEntity articleEntity =  articleRepository.findById(id).orElseThrow(()->new ArticleException("Article not found"));
        return articleMapper.toDto(articleEntity);
    }
}
