package com.store.security.store_security.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stock_article")
@Entity
public class StockArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StockEntity stock;

    @ManyToOne
    private ArticleEntity article;

    private int quantity;
}

