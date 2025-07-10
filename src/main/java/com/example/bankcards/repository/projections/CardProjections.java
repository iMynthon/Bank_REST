package com.example.bankcards.repository.projections;

import java.math.BigDecimal;

public interface CardProjections {
    BigDecimal getScore();
    String getHashCardNumber();
    Boolean getIsActive();
}
