package com.store.security.store_security.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "track")
@Entity
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_order",nullable = false, unique = true)
    private OrderEntity order;

    @Column(name = "status", nullable = false)
    private String status;



}
