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

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "source_card_id")
    private Card sourceCard;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "target_card_id")
    private Card targetCard;

    private BigDecimal amount;

    @Column(name = "transfer_time")
    private LocalDateTime transferTime;

    @Column(name = "status_transfer")
    private StatusTransfer statusTransfer;
}
