package com.example.bankcards.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "cards_transfers")
public class CardTransfer {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "source_hash_card")
    private String sourceHashCard;

    @Column(name = "target_hash_card")
    private String targetHashCard;

    private BigDecimal amount;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "transfer_time")
    private LocalDateTime transferTime;

    @Column(name = "status_transfer")
    private StatusTransfer statusTransfer;
}
