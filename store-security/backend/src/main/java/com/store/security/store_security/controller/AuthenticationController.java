package com.store.security.store_security.controller;

import com.store.security.store_security.dto.LoginUser;
import com.store.security.store_security.dto.LoginUserJwt;
import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.properties.StoreProperties;
import com.store.security.store_security.service.IRegistrationService;
import com.store.security.store_security.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Tag(
        name = "Authentication management",
        description = "REST API for authentication management"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final IRegistrationService registrationService;

    private final IUserService userService;

    private final AuthenticationManager authenticationManager;

    private final StoreProperties storeProperties;

    @Operation(
            summary = "Registration user",
            description = "REST API to registration user"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 : CREATED"
    )
    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody @Valid UserDto userDto) {
        UserDto result = registrationService.registrationUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

    @Operation(
            summary = "Authentication user with basic auth",
            description = "REST API to authentication user with basic auth"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 : OK"
    )
    @GetMapping("/user")
    public ResponseEntity<UserDto> userAuth()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.findUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserJwt> loginUser(@RequestBody LoginUser loginUser)
    {
        String jwt = "";
        UsernamePasswordAuthenticationToken user = UsernamePasswordAuthenticationToken.unauthenticated(loginUser.username(),loginUser.password());
        Authentication authentication = authenticationManager.authenticate(user);
        if(authentication.isAuthenticated())
        {
            try{
                String secret = storeProperties.jwtSecretKeyValue();
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                jwt = Jwts.builder().issuer("store-security")
                        .subject("JWT Token")
                        .claim("username",authentication.getName())
                        .claim("authorities", authentication.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                        .issuedAt(new Date())
                        .expiration(new Date((new Date().getTime() + 60000)))
                        .signWith(secretKey)
                        .compact();

                return ResponseEntity.ok(new LoginUserJwt(HttpStatus.OK.toString(),jwt));
            }catch(Exception e)
            {
                throw new BadCredentialsException(String.format("Bad credentials %s for user %s",e.getMessage(),authentication.getName()));
            }
        }
        throw new BadCredentialsException(String.format("Bad credentials for user %s",authentication.getName()));

    }

}
