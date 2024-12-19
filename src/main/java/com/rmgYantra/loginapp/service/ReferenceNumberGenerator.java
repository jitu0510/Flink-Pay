package com.rmgYantra.loginapp.service;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class ReferenceNumberGenerator {

    public String generateReferenceNumber() {
        // Get the current date in the format "yyMMddHHmm"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");
        String timestamp = dateFormat.format(new Date());

        // Generate a 5-digit random number
        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000);

        // Combine to form a 17-character reference number
        return "20P" + timestamp + randomNumber;
    }
}

