package com.example.bankcards.util;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;

public class StringCreatorUtils {

    private static final String HASH_SALT = "bank_rest_salt";

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

    public static String hashCard(String cardNumber) {
        return DigestUtils.sha256Hex(HASH_SALT + cardNumber);
    }
}
