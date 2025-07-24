package com.store.security.store_security.entity.key;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineKeyEmbeddable implements Serializable {

    private long idOrder;

    private long idArticle;

}
