package com.example.bankcards.util;

import java.time.LocalDateTime;

public class StringMaskedUtils {

    public static String maskedNumberCard(String numberCard){
        String masked = numberCard.substring(numberCard.lastIndexOf(" ")).trim();
        return String.format("**** **** **** %s",masked);
    }

    public static String createOwner(String... owner){
        return String.join(" ", owner);
    }

    public static String createValidityPeriod(LocalDateTime periodFrom,LocalDateTime periodTo){
      return periodFrom.toString() + " " + periodTo.toString();
    }
}
