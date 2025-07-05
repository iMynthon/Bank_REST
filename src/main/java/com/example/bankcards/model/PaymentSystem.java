package com.example.bankcards.model;
import java.time.LocalDateTime;
import java.util.function.BiConsumer;

public enum PaymentSystem {

    VISA((card, time) -> card.setValidityPeriodTo(time.plusYears(5))),
    MASTER_CARD((card, time) -> card.setValidityPeriodTo(time.plusYears(4))),
    MIR((card, time) -> card.setValidityPeriodTo(time.plusYears(6)));

    private final BiConsumer<Card, LocalDateTime> createValidityPeriod;

    PaymentSystem(BiConsumer<Card, LocalDateTime> createValidityPeriod) {
        this.createValidityPeriod = createValidityPeriod;
    }

    public void getCreateValidityPeriod(Card card,LocalDateTime time) {
        createValidityPeriod.accept(card,time);
    }
}
