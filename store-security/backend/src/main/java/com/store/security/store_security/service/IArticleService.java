package com.store.security.store_security.service;

import com.store.security.store_security.dto.ArticleDto;
import com.store.security.store_security.dto.ListArticleDto;

public interface IArticleService {

    public ListArticleDto allArticle();

    public ArticleDto getArticle(Long id);
}
