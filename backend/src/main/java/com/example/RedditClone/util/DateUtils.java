package com.example.RedditClone.util;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;

import static java.time.Duration.between;

@UtilityClass
public class DateUtils {

    public static String calcDuration(Instant createdDate){
        Duration between = between(createdDate, Instant.now());
        if(between.toMinutes() < 1){
            return between.getSeconds() + " segundos atrás";
        } else if(between.toMinutes() < 60){
            return between.toMinutes() + " minutos atrás";
        }
        return between.toHours() + " horas atrás";
    }
}
