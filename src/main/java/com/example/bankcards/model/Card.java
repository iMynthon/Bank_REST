package com.example.bankcards.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "cards")
public class Card {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "number_card")
    private String numberCard;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_system")
    private PaymentSystem paymentSystem;

    @Column(name = "validity_period_from")
    private LocalDateTime validityPeriodFrom;

    @Column(name = "validity_period_to")
    private LocalDateTime validityPeriodTo;

    @Column(name = "is_active")
    private Boolean isActive;

    @PrePersist
    public void createdValidityPeriod(){
        if(validityPeriodFrom == null) {
            validityPeriodFrom = LocalDateTime.now(Clock.systemUTC());
            paymentSystem.getCreateValidityPeriod(this, validityPeriodFrom);
        }
    }

}
