package com.store.security.store_security.config;

import com.store.security.store_security.constants.RoleConstants;
import com.store.security.store_security.dto.StockDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.StockEntity;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.StockRepository;
import com.store.security.store_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataConfigInit implements CommandLineRunner {

    private final AuthoritiesRepository authoritiesRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    @Override
    public void run(String... args) throws Exception {
        AuthoritiesEntity authorities = new AuthoritiesEntity();
        authorities.setAuthority(RoleConstants.ADMIN.getRole());
        authorities = authoritiesRepository.save(authorities);

        UserEntity user = new UserEntity();
        user.setUsername("admin@example.it");
        user.setPassword(
                "{bcrypt}$2a$12$TbCpjrh02.ulo/tG.pT/6eYKz06hqJJDLCqzX8Xzdl3TzZM10cZgW");
        user.setAge(20);
        user.setTmstInsert(LocalDateTime.now());
        user.setAuthoritiesList(Set.of(authorities));

        StockEntity stock = StockEntity.builder().build();
        stockRepository.save(stock);
        userRepository.save(user);

        // USER
        AuthoritiesEntity authoritiesUser = new AuthoritiesEntity();
        authoritiesUser.setAuthority(RoleConstants.USER.getRole());
        authoritiesRepository.save(authoritiesUser);
        // TRACK
        AuthoritiesEntity authoritiesTrack = new AuthoritiesEntity();
        authoritiesTrack.setAuthority(RoleConstants.TRACK.getRole());
        authoritiesTrack = authoritiesRepository.save(authoritiesTrack);

        UserEntity tracker = new UserEntity();
        tracker.setUsername("track@example.it");
        tracker.setPassword(
                "{bcrypt}$2a$12$vEjmF.7P/Kwyk72m7h/wwe.ihKl1i0uH4FWy004Zo482obiNnBf7.");
        tracker.setAge(20);
        tracker.setTmstInsert(LocalDateTime.now());
        tracker.setAuthoritiesList(Set.of(authoritiesTrack));

        userRepository.save(tracker);
    }
}

