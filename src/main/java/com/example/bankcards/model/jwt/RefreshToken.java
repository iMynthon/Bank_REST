package com.example.bankcards.model.jwt;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


import java.util.UUID;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@RedisHash(value = "RefreshToken")
public class RefreshToken {

    @Id
    private String id;
    private UUID userId;
    private String phoneNumber;
    @Indexed
    private String value;
    @TimeToLive
    private Long expiration;
}
