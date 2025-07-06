package com.example.bankcards.util;
import lombok.experimental.UtilityClass;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

@UtilityClass
public class StringMaskedUtils {

    @Autowired
    private StringEncryptor stringEncryptor;

    public String maskedNumberCard(String numberCard){
        numberCard = stringEncryptor.decrypt(numberCard);
        String masked = numberCard.substring(numberCard.lastIndexOf(" ")).trim();
        return String.format("**** **** **** %s",masked);
    }

    public String createOwner(String... owner){
        return String.join(" ", owner);
    }
}
