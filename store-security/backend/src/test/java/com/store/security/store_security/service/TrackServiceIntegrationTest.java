package com.store.security.store_security.service;

import com.store.security.store_security.StoreSecurityApplicationTests;
import com.store.security.store_security.dto.TrackDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.OrderEntity;
import com.store.security.store_security.entity.TrackEntity;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.mapper.OrderMapper;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.OrderRepository;
import com.store.security.store_security.repository.TrackRepository;
import com.store.security.store_security.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Set;

public class TrackServiceIntegrationTest extends StoreSecurityApplicationTests {

	@Autowired
	private ITrackService trackService;

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository	userRepository;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	@Test
	public void getTrackByOrder() {
		//given
		AuthoritiesEntity authoritiesEntity = AuthoritiesEntity.builder().authority("ROLE_USER").build();
		authoritiesEntity = authoritiesRepository.save(authoritiesEntity);
		UserEntity user = UserEntity.builder().username("username")
				.tmstInsert(LocalDateTime.of(2022,1,1,1,1))
				.authoritiesList(Set.of(authoritiesEntity))
				.age(32).build();
		user = userRepository.save(user);
		OrderEntity order = OrderEntity.builder().tmstInsert(
				LocalDateTime.of(2022,1,1,1,1))
				.user(user).build();
		order = orderRepository.save(order);
		TrackEntity track = TrackEntity.builder().order(order).status("status").build();
		trackRepository.save(track);
		//when
		TrackDto trackDto = trackService.getTrackByOrder(1L);
		//then
		Assertions.assertThat(trackDto).isNotNull();
		Assertions.assertThat(trackDto.getId()).isEqualTo(track.getId());
		Assertions.assertThat(trackDto.getOrder().getId()).isEqualTo(order.getId());
		Assertions.assertThat(trackDto.getStatus()).isEqualTo(track.getStatus());
	}

	@Test
	public void setTrack()
	{
		//given
		AuthoritiesEntity authoritiesEntity = AuthoritiesEntity.builder().authority("ROLE_USER").build();
		authoritiesEntity = authoritiesRepository.save(authoritiesEntity);
		UserEntity user = UserEntity.builder().username("username")
				.tmstInsert(LocalDateTime.of(2022,1,1,1,1))
				.authoritiesList(Set.of(authoritiesEntity))
				.age(32).build();
		user = userRepository.save(user);
		OrderEntity order = OrderEntity.builder().tmstInsert(
						LocalDateTime.of(2022,1,1,1,1))
				.user(user).build();
		order = orderRepository.save(order);
		TrackEntity track = TrackEntity.builder().order(order).status("status").build();
		trackRepository.save(track);
		//when
		TrackDto trackDto = trackService.setTrack(order.getId(),TrackDto.builder().order(orderMapper.toDto(order)).status("PREPARED").build());
		//then
		Assertions.assertThat(trackDto).isNotNull();
		Assertions.assertThat(trackDto.getId()).isEqualTo(track.getId());
		Assertions.assertThat(trackDto.getOrder().getId()).isEqualTo(order.getId());
		Assertions.assertThat(trackDto.getStatus()).isEqualTo("PREPARED");
	}
}
